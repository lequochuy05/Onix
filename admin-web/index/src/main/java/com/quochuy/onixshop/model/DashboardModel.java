package com.quochuy.onixshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardModel {
    private Long categoryCount;
    private Long bestSellerCount;
    private Long productCount;
    private Long orderCount;
    private Long userCount;
}