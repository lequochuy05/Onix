package com.quochuy.onixshop.repository;

import com.google.firebase.database.*;
import com.quochuy.onixshop.model.CategoryModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class CategoryRepository {
    private final DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category");

    public CompletableFuture<List<CategoryModel>> getAllCategories() {
        CompletableFuture<List<CategoryModel>> future = new CompletableFuture<>();
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<CategoryModel> list = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    CategoryModel cat = child.getValue(CategoryModel.class);
                    list.add(cat);
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

    public void saveCategory(CategoryModel cat) {
        categoryRef.child(String.valueOf(cat.getId())).setValueAsync(cat);
    }

    public void deleteCategory(int id) {
        categoryRef.child(String.valueOf(id)).removeValueAsync();
    }

    public CompletableFuture<CategoryModel> getById(int id) {
        CompletableFuture<CategoryModel> future = new CompletableFuture<>();
        categoryRef.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                CategoryModel cat = snapshot.getValue(CategoryModel.class);
                future.complete(cat);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        return future;
    }
}
