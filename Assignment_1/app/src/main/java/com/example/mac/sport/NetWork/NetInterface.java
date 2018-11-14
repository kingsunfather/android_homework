package com.example.mac.sport.NetWork;

import android.util.JsonReader;

import com.example.mac.sport.entity.Result;
import com.example.mac.sport.entity.User;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetInterface {
    @POST("register")
    Call<ResponseBody> register(@Body User user);
    @POST("login")
    Call<ResponseBody> login(@Body User user);
    @POST("checkEmail")
    Call<ResponseBody> checkEmail(@Body JSONObject jsonObject);
    @POST("logout")
    Call<ResponseBody> logout(@Body JSONObject jsonObject);
    @POST("me")
    Call<ResponseBody> myInfo(@Body JSONObject jsonObject);
    @POST("getAllCategorys")
    Call<ResponseBody> getAllCategorys(@Body JSONObject jsonObject);
    @POST("getAllSports")
    Call<ResponseBody> getAllSports(@Body JSONObject jsonObject);
    @POST("getUserSports")
    Call<ResponseBody> getUserSports(@Body JSONObject jsonObject);
}
