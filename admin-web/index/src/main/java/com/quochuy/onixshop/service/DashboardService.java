package com.quochuy.onixshop.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quochuy.onixshop.model.DashboardModel;
import com.quochuy.onixshop.repository.DashboardRepository;
import com.quochuy.onixshop.repository.OrderRepository;

@Service
public class DashboardService {
    
    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private OrderRepository orderRepository;


    public DashboardModel loadDashboardData() {

        CompletableFuture<Long> categoryFuture = dashboardRepository.countCategories();
        CompletableFuture<Long> bestSellerFuture = dashboardRepository.countBestSellers();
        CompletableFuture<Long> productFuture = dashboardRepository.countItems();
        CompletableFuture<Long> orderFuture = dashboardRepository.countOrders();
        CompletableFuture<Long> userFuture = dashboardRepository.countUsers();
        

        CompletableFuture.allOf(categoryFuture,bestSellerFuture ,productFuture, orderFuture, userFuture).join();

        DashboardModel model = new DashboardModel();
        try {
            model.setCategoryCount(categoryFuture.get());
            model.setBestSellerCount(bestSellerFuture.get());
            model.setProductCount(productFuture.get());
            model.setOrderCount(orderFuture.get());
            model.setUserCount(userFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
