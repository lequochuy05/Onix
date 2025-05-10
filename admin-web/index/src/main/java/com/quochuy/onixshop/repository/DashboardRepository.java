package com.quochuy.onixshop.repository;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Repository
public class DashboardRepository {
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    
    public CompletableFuture<Long> countCategories(){
        return countChildren(dbRef.child("Category"));
    }

    public CompletableFuture<Long> countBestSellers() {
        CompletableFuture<Long> future = new CompletableFuture<>();
        dbRef.child("Items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long count = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Boolean bestSeller = child.child("bestSeller").getValue(Boolean.class);
                    if (Boolean.TRUE.equals(bestSeller)) {
                        count++;
                    }
                }
                future.complete(count);
            }
    
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }
    

    public CompletableFuture<Long> countItems(){
        return countChildren(dbRef.child("Items"));
    }
    public CompletableFuture<Long> countOrders(){
        return countChildren(dbRef.child("orders"));
    }
    public CompletableFuture<Long> countUsers(){
        return countChildren(dbRef.child("UserAccount"));
    }
    

    private CompletableFuture<Long> countChildren(DatabaseReference child) {
        CompletableFuture<Long> future = new CompletableFuture<>();

        child.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                future.complete(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
            
        });

        return future;

    }
    
}