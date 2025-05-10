package com.quochuy.onixshop.controller;

import com.quochuy.onixshop.model.ItemsModel;
import com.quochuy.onixshop.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/admin/products/api")
    @ResponseBody
    public List<ItemsModel> getProductsAPI(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {
        return productService.getFilteredProducts(category, search, page, size);
    }

    @GetMapping("/admin/products")
    public String productPage(Model model) throws Exception {
        model.addAttribute("categories", productService.getCategories());
        return "admin/products";
    }

    //chuyển đến trang thêm sản phẩm
    @GetMapping("/admin/products/add")
    public String showAddForm() {
        return "admin/add-product";
    }

    // Thêm sản phẩm vào realtimeDB
    @PostMapping("/admin/products/add")
    @ResponseBody
    public String saveProduct(@RequestBody ItemsModel product) {
        try {
            if (product.getTitle().isEmpty() || product.getPrice() <= 0 || product.getRating() < 0 || product.getPicUrl() == null || product.getPicUrl().isEmpty()) {
                return "invalid";
            }
            productService.addProduct(product);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    //Sản phẩm bán chạy
    @GetMapping("/admin/products/best-sellers")
    public String showBestSellers(Model model) throws Exception {
            model.addAttribute("bestSellers", productService.getBestSellers());
            return "admin/bestSellers";
        }

    // Chỉnh sửa sản phẩm
    @GetMapping("/admin/products/edit/{id}")
    public String editForm(@PathVariable String id, Model model) throws Exception {
        ItemsModel product = productService.getItemById(id);
    
        if (product == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm");
        }
    
        model.addAttribute("product", product);
        model.addAttribute("productId", id);
    
        return "admin/edit-product";
    }
    

    @PostMapping("/admin/products/edit/{id}")
    @ResponseBody
    public String updateProduct(@PathVariable String id, @RequestBody ItemsModel product) {
        try {
            if (product.getTitle() == null || product.getTitle().isEmpty() ||
                product.getPrice() <= 0 || product.getRating() < 0 ||
                product.getPicUrl() == null || product.getPicUrl().isEmpty()) {
                return "invalid";
            }
    
            productService.updateProduct(id, product);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    

    @DeleteMapping("/admin/products/delete/{id}")
    @ResponseBody
    public String deleteProduct(@PathVariable String id) {
        try {
            ItemsModel item = productService.getItemById(id); // Lấy ảnh cũ
            if (item != null) {
                 // XÓA LOGO
                productService.deleteImageFromCloudinary(item.getLogo());

                // XÓA TẤT CẢ ẢNH CHI TIẾT
                if (item.getPicUrl() != null) {
                    for (String url : item.getPicUrl()) {
                        productService.deleteImageFromCloudinary(url);
                    }
                }
            }
            productService.deleteProduct(id); // Xóa khỏi Firebase
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    

    

}
