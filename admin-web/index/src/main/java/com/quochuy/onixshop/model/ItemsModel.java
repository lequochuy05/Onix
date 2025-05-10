package com.quochuy.onixshop.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemsModel {
    private String categoryId;
    private String description;
    private String title;
    private long price;
    private double rating;
    private List<String> size;
    private List<String> picUrl;
    private String logo;
    private String id;
    private String categoryName;
    private String categoryImg;  
    private boolean bestSeller;
}
