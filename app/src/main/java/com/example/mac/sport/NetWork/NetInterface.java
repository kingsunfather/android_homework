package com.example.mac.sport.NetWork;

import com.example.mac.sport.entity.Result;
import com.example.mac.sport.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NetInterface {
    @POST("register")
    Call<Result> register(@Body User user);
}
