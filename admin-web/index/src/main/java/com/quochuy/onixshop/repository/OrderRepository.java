package com.quochuy.onixshop.repository;

import com.google.firebase.database.*;
import com.quochuy.onixshop.model.OrderModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class OrderRepository {
    private final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders");

    // 
    public CompletableFuture<List<OrderModel>> getAllOrders() {
        CompletableFuture<List<OrderModel>> future = new CompletableFuture<>();

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<OrderModel> list = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    OrderModel order = child.getValue(OrderModel.class);
                    list.add(order);
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

        public CompletableFuture<Double> getTotalRevenue() {
            CompletableFuture<Double> future = new CompletableFuture<>();
        
            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    double total = 0;
                    for (DataSnapshot orderSnap : snapshot.getChildren()) {
                        OrderModel order = orderSnap.getValue(OrderModel.class);
                        if (order != null) {
                            total += order.getTotalPrice();
                        }
                    }
                    future.complete(total);
                }
        
                @Override
                public void onCancelled(DatabaseError error) {
                    future.completeExceptionally(new RuntimeException(error.getMessage()));
                }
            });
        
            return future;
        }
    

    // Cập nhật trạng thái đơn hàng
    public void updateOrderStatus(String orderId, String status) {
        FirebaseDatabase.getInstance().getReference("orders").child(orderId).child("orderStatus").setValueAsync(status);
    }
    
}
