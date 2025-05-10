package com.quochuy.onixshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {
    private int id;
    private String title;
    private String picUrl;
}
