package com.quochuy.onixshop.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.quochuy.onixshop.model.UserModel;
import com.quochuy.onixshop.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //Lấy danh sách user có phân trang
    public List<UserModel> getUsers(String search, String verified, int page, int size) throws Exception{
        List<UserModel> userList = userRepository.getAllUsers().get();

        Map<String, UserRecord> authMap = fetchAuthInfo();
        // Gắn thông tin xác thực vào model
        for(UserModel user: userList){
            UserRecord record = authMap.get(user.getEmail());
            if(record!=null){
                user.setEmailVerified(record.isEmailVerified());
                user.setCreationTime(record.getUserMetadata().getCreationTimestamp());
            }
        }

        //Tìm kiếm
        if(search!=null && !search.isEmpty()){
            userList = userList.stream()
                        .filter(u -> (u.getEmail() != null && u.getEmail().toLowerCase().contains(search.toLowerCase()))
                            || (u.getFirstName() + " " + u.getLastName()).toLowerCase().contains(search.toLowerCase()))
                        .collect(Collectors.toList());
        }

        //Lọc trạng thái xác thực
        if ("verified".equals(verified)) {
            userList = userList.stream().filter(UserModel::isEmailVerified).collect(Collectors.toList());
        } else if ("unverified".equals(verified)) {
            userList = userList.stream().filter(u -> !u.isEmailVerified()).collect(Collectors.toList());
        }

        // Phân trang
        int from = Math.max(0, (page - 1) * size);
        int to = Math.min(from + size, userList.size());
        if (from > to) return Collections.emptyList();
        return userList.subList(from, to);
    }

    public int getTotalPages(int size) throws Exception {
        int total = userRepository.getAllUsers().get().size();
        return (int) Math.ceil((double) total / size);
    }

    // Lấy metadata từ Firebase Authentication
    private Map<String, UserRecord> fetchAuthInfo() throws Exception {
        Map<String, UserRecord> map = new HashMap<>();
        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        while (page != null) {
            for (UserRecord user : page.getValues()) {
                map.put(user.getEmail(), user);
            }
            page = page.getNextPage();
        }
        return map;
    }


    public void setUserBanned(String userId, boolean banStatus) throws Exception {
        List<UserModel> users = userRepository.getAllUsers().get();
        for (UserModel user : users) {
            if (user.getId().equals(userId)) {
                user.setBanned(banStatus);
                userRepository.updateUser(userId, user); // phương thức này cần thêm ở bước sau
                break;
            }
        }
    }
}

