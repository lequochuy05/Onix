package com.quochuy.onixshop.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemModel {
    private String itemId;
    private String itemName;
    private double itemPrice;
    private int itemQuantity;
    private String selectedSize;
    private List<String> picUrl;

}
