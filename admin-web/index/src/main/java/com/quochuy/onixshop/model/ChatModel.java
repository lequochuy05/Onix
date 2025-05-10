package com.quochuy.onixshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatModel {
    private String message;
    private boolean user;
    private String userId;
    private long timestamp;

}
