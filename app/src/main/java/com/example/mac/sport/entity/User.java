package com.example.mac.sport.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    private String name;
    @SerializedName("pass")
    private String pass;

    public User(String name,String pass){
        this.name=name;
        this.pass=pass;
    }

}
