package com.quochuy.onixshop.repository;

import com.google.firebase.database.*;
import com.quochuy.onixshop.model.ChatModel;
import com.quochuy.onixshop.model.UserModel;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Repository
public class ChatRepository {

    private final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats"); 
    private final DatabaseReference uRef = FirebaseDatabase.getInstance().getReference("UserAccount");

    // lấy tất cả tin nhắn của một người dùng
    public CompletableFuture<List<ChatModel>> getMessagesByUserId(String userId) {
        CompletableFuture<List<ChatModel>> future = new CompletableFuture<>();
        chatRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ChatModel> messages = new ArrayList<>();
                for (DataSnapshot msgSnap : snapshot.getChildren()) {
                    ChatModel msg = msgSnap.getValue(ChatModel.class);
                    if (msg != null) messages.add(msg);
                }
                future.complete(messages);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        return future;
    }

    // gửi tin nhắn đến một người dùng
    public void sendMessageToUser(String userId, ChatModel message) {
        chatRef.child(userId).push().setValueAsync(message);
    }

    // lấy danh sách người dùng đang chat và không bị cấm
    public CompletableFuture<Map<String, UserModel>> getChattingUsersOnly() {
        CompletableFuture<Map<String, UserModel>> future = new CompletableFuture<>();
    
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, UserModel> users = new HashMap<>();
                List<CompletableFuture<Void>> tasks = new ArrayList<>();
    
                // duyệt qua tất cả người dùng trong danh sách chat
                for (DataSnapshot child : snapshot.getChildren()) {
                    String userId = child.getKey();
                    CompletableFuture<Void> task = new CompletableFuture<>();
    
                    uRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                UserModel user = dataSnapshot.getValue(UserModel.class);
                                // kiểm tra xem người dùng có tồn tại và không bị cấm
                                if (user != null && !user.isBanned()) {
                                    user.setId(userId);
                                    users.put(userId, user);
                                }
                            } catch (Exception e) {
                                System.err.println("Lỗi đọc user: " + e.getMessage());
                            } finally {
                                task.complete(null);
                            }
                        }
    
                        @Override
                        public void onCancelled(DatabaseError error) {
                            task.completeExceptionally(new RuntimeException(error.getMessage()));
                        }
                    });
    
                    tasks.add(task);
                }
    
                // đợi tất cả các tác vụ hoàn thành
                CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
                        .thenRun(() -> future.complete(users));
            }
    
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });

        // trả về CompletableFuture chứa danh sách người dùng
        return future;
    }
    
    // lắng nghe tin nhắn từ một người dùng cụ thể
    // sử dụng SseEmitter để gửi tin nhắn đến client khi có sự thay đổi
    public SseEmitter listenToUserMessages(String userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        DatabaseReference ref = chatRef.child(userId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ChatModel> messages = new ArrayList<>();
                for (DataSnapshot msgSnap : snapshot.getChildren()) {
                    ChatModel msg = msgSnap.getValue(ChatModel.class);
                    if (msg != null) messages.add(msg);
                }
                try {
                    emitter.send(messages);
                } catch (Exception e) {
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                emitter.completeWithError(error.toException());
            }
        });

        return emitter;
    }

}
