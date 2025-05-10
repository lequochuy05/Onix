package com.quochuy.onixshop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quochuy.onixshop.model.DashboardModel;
import com.quochuy.onixshop.service.DashboardService;
import com.quochuy.onixshop.service.OrderService;


@Controller
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        DashboardModel dbModel = dashboardService.loadDashboardData();
        model.addAttribute("dbModel", dbModel);
        return "admin/dashboard";
    }

    @GetMapping("/admin/dashboard/revenue-monthly")
    @ResponseBody
    public Map<String, Double> getMonthlyRevenue(@RequestParam(required = false) String year) throws Exception {
        return orderService.getMonthlyRevenueByYear(year);
    }
}
