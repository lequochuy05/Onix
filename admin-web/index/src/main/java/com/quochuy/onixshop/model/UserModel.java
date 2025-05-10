package com.quochuy.onixshop.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private List<String> address;
    private String dob;
    private String img;
    private long creationTime;
    private boolean emailVerified;
    private boolean banned = false;
}
