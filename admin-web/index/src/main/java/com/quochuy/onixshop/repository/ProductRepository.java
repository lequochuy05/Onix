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
import com.quochuy.onixshop.model.CategoryModel;
import com.quochuy.onixshop.model.ItemsModel;

@Repository
public class ProductRepository 
{
   private final DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference("Items");
   private final DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category");

   public CompletableFuture<List<CategoryModel>> getAllCategories(){
    CompletableFuture<List<CategoryModel>> future = new CompletableFuture<>();

    categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot snapshot) {
            List<CategoryModel> list = new ArrayList<>();
            for(DataSnapshot child: snapshot.getChildren()){
                CategoryModel cate = child.getValue(CategoryModel.class);
                list.add(cate);
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

    public CompletableFuture<List<ItemsModel>> getAllItems(){
        CompletableFuture<List<ItemsModel>> future = new CompletableFuture<>();

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ItemsModel> list = new ArrayList<>();
                for(DataSnapshot child: snapshot.getChildren()){
                    ItemsModel item = child.getValue(ItemsModel.class);
                    if (item != null) {
                        item.setId(child.getKey());
                        list.add(item);
                    }
                }

                future.complete(list);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()))  ;

            } 
        });
        return future;
    }

    public CompletableFuture<ItemsModel> getItemById(String id) {
        CompletableFuture<ItemsModel> future = new CompletableFuture<>();
    
        itemRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ItemsModel item = snapshot.getValue(ItemsModel.class);
                if (item != null) {
                    item.setId(id);
                }
                future.complete(item);
            }
    
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
    
        return future;
    }
    
    public void addItem(String id, ItemsModel item){
        item.setId(id);
        itemRef.child(id).setValueAsync(item); 
    }

    public void deleteItem(String id){
        itemRef.child(id).removeValueAsync();
    }
}