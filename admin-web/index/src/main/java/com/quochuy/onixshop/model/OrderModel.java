package com.quochuy.onixshop.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
    private String userFirstName;
    private String userLastName;    
    private String orderId;
    private String userId;
    private List<OrderItemModel> items;
    private double totalPrice;
    private String orderStatus;
    private String orderDate;
    private String deliveryAddress;

}
