package com.quochuy.onixshop.controller;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quochuy.onixshop.model.ChatModel;
import com.quochuy.onixshop.model.UserModel;
import com.quochuy.onixshop.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public String listUsers(Model model) throws Exception {
        Map<String, UserModel> users = chatService.getOnlyChattingUsers(); // lấy danh sách user từ Firebase
        model.addAttribute("users", users); // truyền vào view Thymeleaf
        return "admin/chats"; // trả về view danh sách người dùng
    }

    @GetMapping("/{userId}")
    public String viewChat(@PathVariable String userId, Model model) throws Exception {
        List<ChatModel> messages = chatService.getMessagesByUserId(userId);// lấy danh sách tin nhắn từ Firebase
        Map<String, UserModel> users = chatService.getOnlyChattingUsers();// lấy danh sách user từ Firebase
        // Kiểm tra xem người dùng có tồn tại không
        UserModel user = users.get(userId);
        if (user == null) {
            model.addAttribute("error", "Người dùng không tồn tại.");
            return "redirect:/admin/chats";
        }
        // truyền vào view Thymeleaf
        model.addAttribute("messages", messages);
        model.addAttribute("users", users);
        model.addAttribute("userId", userId);
        model.addAttribute("userImg", user.getImg());
        model.addAttribute("userName", user.getFirstName() + " " + user.getLastName());
        
        return "admin/chat-detail"; 
    }

    @PostMapping("/{userId}/send")
    @ResponseBody
    public String sendMessage(@PathVariable String userId, @RequestParam String message) {
        chatService.sendMessageToUser(userId, message); // gửi tin nhắn đến người dùng
       
        return "OK"; // cho frontend biết gửi thành công
    }

        
    @GetMapping("/{userId}/stream")
    public SseEmitter streamMessages(@PathVariable String userId) {
        return chatService.streamMessagesFromUser(userId);
    }
    
}
