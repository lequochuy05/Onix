package com.quochuy.onixshop.service;

import com.quochuy.onixshop.model.ChatModel;
import com.quochuy.onixshop.model.UserModel;
import com.quochuy.onixshop.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    // Lấy tất cả tin nhắn của một người dùng
    public List<ChatModel> getMessagesByUserId(String userId) throws Exception {
        List<ChatModel> messages = chatRepository.getMessagesByUserId(userId).get();
        messages.sort((m1, m2) -> Long.compare(m1.getTimestamp(), m2.getTimestamp())); // Sắp xếp theo thời gian
        return messages;
    }

    // Gửi tin nhắn đến một người dùng
    public void sendMessageToUser(String userId, String messageText) {
        ChatModel message = new ChatModel();
        message.setMessage(messageText);
        message.setUser(false); // admin
        message.setUserId(userId);
        message.setTimestamp(System.currentTimeMillis()); // Thời gian hiện tại

        chatRepository.sendMessageToUser(userId, message);
    }

    // Lấy danh sách người dùng đang chat và không bị cấm
    public Map<String, UserModel> getOnlyChattingUsers() throws Exception {
        return chatRepository.getChattingUsersOnly().get();
    }

    //
    public SseEmitter streamMessagesFromUser(String userId) {
    return chatRepository.listenToUserMessages(userId);
}
    
}
