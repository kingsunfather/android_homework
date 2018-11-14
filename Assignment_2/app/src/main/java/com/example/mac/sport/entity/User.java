package com.example.mac.sport.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public User(String email,String password){
        this.email=email;
        this.password=password;
    }

}
