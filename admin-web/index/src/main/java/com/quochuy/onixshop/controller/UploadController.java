package com.quochuy.onixshop.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/cloudinary")
public class UploadController {

    @Autowired
    private Cloudinary cloudinary;

    // Upload ảnh danh mục
    @PostMapping("/uploadCategory")
    public ResponseEntity<?> uploadImageCategory(@RequestParam("file") MultipartFile file) {
        return uploadToCloudinary(file, "CuoiKi1-25/Category", "cate_");
    }

    // Upload ảnh sản phẩm bán chạy
    @PostMapping("/uploadBestSeller")
    public ResponseEntity<?> uploadImageBestSeller(@RequestParam("file") MultipartFile file) {
        return uploadToCloudinary(file, "CuoiKi1-25/BestSeller", "bestSell_");
    }

    // Upload ảnh logo
    @PostMapping("/uploadLogo")
    public ResponseEntity<?> uploadImageLogo(@RequestParam("file") MultipartFile file) {
        return uploadToCloudinary(file, "CuoiKi1-25/Logo", "logo_");
    }

    // Upload ảnh chi tiết sản phẩm
    @PostMapping("/uploadItem")
    public ResponseEntity<?> uploadImageItem(@RequestParam("file") MultipartFile file) {
        return uploadToCloudinary(file, "CuoiKi1-25/Items", "item_");
    }

    // Delete ảnh theo publicId
    @PostMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestBody Map<String, String> request) {
        try {
            String publicId = request.get("publicId");
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Xóa ảnh thất bại");
        }
    }

    // upload lên Cloudinary
    private ResponseEntity<?> uploadToCloudinary(MultipartFile file, String folder, String prefix) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) throw new RuntimeException("Không thể lấy tên ảnh gốc");

            String name = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
            String random = UUID.randomUUID().toString().substring(0, 6);
            String publicId = prefix + name + "_" + random;

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folder,
                    "public_id", publicId,
                    "quality", "auto"
            ));

            return ResponseEntity.ok(uploadResult.get("secure_url"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Upload thất bại");
        }
    }
}