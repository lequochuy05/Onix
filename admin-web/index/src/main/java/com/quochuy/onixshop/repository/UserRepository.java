package com.quochuy.onixshop.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quochuy.onixshop.model.UserModel;

@Repository
public class UserRepository {
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserAccount");

    // Lấy toàn bộ danh sách người dùng 
    public CompletableFuture<List<UserModel>> getAllUsers(){
        CompletableFuture<List<UserModel>> future = new CompletableFuture<>();
        List<UserModel> list = new ArrayList<>();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    UserModel user = child.getValue(UserModel.class);
                    if(user!= null){
                        user.setId(child.getKey());
                        list.add(user);
                    }
                }
                future.complete(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
            
        });
        return future;
    }

    public void updateUser(String userId, UserModel user) {
        userRef.child(userId).setValueAsync(user);
    }
}
