package com.quochuy.onixshop.controller;

import com.quochuy.onixshop.model.OrderModel;
import com.quochuy.onixshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Chi tiêt đơn hàng
    @GetMapping("/admin/orders/{id}")
    public String orderDetail(@PathVariable String id, Model model) throws Exception {
        List<OrderModel> orders = orderService.getAllOrders();
        OrderModel order = orders.stream().filter(o -> o.getOrderId().equals(id)).findFirst().orElse(null);
        if (order == null) return "redirect:/admin/orders";

        model.addAttribute("order", order);
        return "admin/order-detail";
    }

    // Cập nhật trang thái đơn hàng
    @PostMapping("/admin/orders/update-status")
    public String updateStatus(@RequestParam String orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders/" + orderId;
    }

    // tìm kiếm đơn hàng và phân trang
    @GetMapping("/admin/orders")
    public String orderList(
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "") String status,
        @RequestParam(defaultValue = "") String dateFrom,
        @RequestParam(defaultValue = "") String dateTo,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "5") int size,
        Model model
    ) throws Exception {
        List<OrderModel> orders = orderService.getFilteredOrders(search, status, dateFrom, dateTo, page, size);
        int totalPages = orderService.getTotalPages(search, status, dateFrom, dateTo, size);
    
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("search", search);
        model.addAttribute("status", status);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
    
        return "admin/orders";
    }
    



}
