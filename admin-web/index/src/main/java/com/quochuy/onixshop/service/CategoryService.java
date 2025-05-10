package com.quochuy.onixshop.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quochuy.onixshop.model.CategoryModel;
import com.quochuy.onixshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;
    @Autowired
    private Cloudinary cloudinary;

    //lấy tất cả data
    public List<CategoryModel> getAll() throws Exception {
        return repo.getAllCategories().get();
    }

    //Lưu danh mục
    public void save(CategoryModel cat) {
        repo.saveCategory(cat);
    }

    //Xóa danh mục
    public void delete(int id) {
        repo.deleteCategory(id);
    }

    // Lấy danh mục theo id
    public CategoryModel getById(int id) throws Exception {
        return repo.getById(id).get();
    }

      //Xóa ảnh trên Cloudinary
    public void deleteImageFromCloudinary(String imageUrl) {
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
    
            String path = parts[1].replaceAll("v\\d+/", ""); // Xóa version v1234
            return path.substring(0, path.lastIndexOf(".")); // Xóa phần mở rộng
        } catch (Exception e) {
            return null;
        }
    }
}
