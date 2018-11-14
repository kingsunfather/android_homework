package com.example.mac.sport.entity;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("code")
    private String code ;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private String data;

    public Result(String code,String message,String data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
