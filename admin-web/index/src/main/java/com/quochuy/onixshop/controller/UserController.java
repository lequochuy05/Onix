package com.quochuy.onixshop.controller;

import com.quochuy.onixshop.model.UserModel;
import com.quochuy.onixshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String verified,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ) throws Exception {

        List<UserModel> users = userService.getUsers(search, verified, page, size);
        int totalPages = userService.getTotalPages(size);

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("search", search);
        model.addAttribute("verified", verified);

        return "admin/users";
    }

    @PostMapping("/ban/{id}")
    public String banUser(@PathVariable String id, @RequestParam boolean ban) throws Exception {
        userService.setUserBanned(id, ban);
        return "redirect:/admin/users";
    }

}
