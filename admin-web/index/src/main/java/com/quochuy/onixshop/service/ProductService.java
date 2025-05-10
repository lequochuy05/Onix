package com.quochuy.onixshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.api.client.util.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quochuy.onixshop.model.CategoryModel;
import com.quochuy.onixshop.model.ItemsModel;
import com.quochuy.onixshop.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Cloudinary cloudinary;

    public List<ItemsModel> getFilteredProducts(String category, String search, int page, int size) throws Exception{
           
            List<CategoryModel> categories = productRepository.getAllCategories().get();
            List<ItemsModel> items = productRepository.getAllItems().get();

            List<ItemsModel> filtered = new ArrayList<>();

            for(ItemsModel item : items){
                for(CategoryModel cate: categories){
                    if(String.valueOf(cate.getId()).equals(item.getCategoryId())){
                        item.setCategoryName(cate.getTitle());
                    item.setCategoryImg(cate.getPicUrl());
                    break;
                    } 
                }

                boolean matchCategory = (category == null || category.isEmpty() || item.getCategoryId().equals(category));
                boolean matchSearch = (search == null || search.isEmpty() || item.getTitle().toLowerCase().contains(search.toLowerCase()));

                if (matchCategory && matchSearch) {
                    filtered.add(item);
                }
            }

            // Phân trang
            int from = Math.max(0,(page - 1)*size);
            int to = Math.min(from + size, filtered.size());
            return filtered.subList(from, to);
        }

    public List<CategoryModel> getCategories() throws Exception {
        return productRepository.getAllCategories().get();
        
    }

    public List<ItemsModel> getBestSellers() throws Exception {
        return productRepository.getAllItems().get().stream()
            .filter(ItemsModel::isBestSeller)
            .collect(Collectors.toList());
    }

    public ItemsModel getItemById(String id) throws Exception {
        return productRepository.getItemById(id).get();
    }

    public void addProduct(ItemsModel item) {
        String id = UUID.randomUUID().toString();
        item.setId(id); 
        productRepository.addItem(id, item);
    }

    public void updateProduct(String id, ItemsModel product) {
        productRepository.addItem(id, product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteItem(id);
    }

    public void deleteImageFromCloudinary(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) return;
        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String extractPublicId(String url) {
        try {
            String[] parts = url.split("/upload/");
            if (parts.length < 2) return null;
    
            String path = parts[1].replaceAll("v[0-9]+/", ""); // Bỏ version nếu có
            return path.substring(0, path.lastIndexOf('.'));   // Bỏ đuôi .jpg, .png,...
        } catch (Exception e) {
            return null;
        }
    }
}