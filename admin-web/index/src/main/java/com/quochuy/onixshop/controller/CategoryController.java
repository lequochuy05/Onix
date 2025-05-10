package com.quochuy.onixshop.controller;

import com.quochuy.onixshop.model.CategoryModel;
import com.quochuy.onixshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    // Lấy danh sách danh mục
    @GetMapping
    public String list(Model model) throws Exception {
        List<CategoryModel> categories = service.getAll();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }

    // Thêm danh mục
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new CategoryModel());
        return "admin/add-category";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute CategoryModel cat) {
        service.save(cat);
        return "redirect:/admin/categories";
    }

    // Sửa danh mục
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) throws Exception {
        CategoryModel cat = service.getById(id);
        model.addAttribute("category", cat);
        return "admin/edit-category";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute CategoryModel cat) {
        service.save(cat);
        return "redirect:/admin/categories";
    }

    // Xóa danh mục
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable int id) {
        try {
            CategoryModel cat = service.getById(id);
            if (cat != null && cat.getPicUrl() != null) {
                service.deleteImageFromCloudinary(cat.getPicUrl()); // XÓA CLOUDINARY
            }

            service.delete(id); // XÓA FIREBASE
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}
