package com.example.mac.sport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.mac.sport.R;
import com.example.mac.sport.utils.ActivityUtils;

import org.json.JSONObject;

public class UserInfo extends AppCompatActivity{
    private TextView username;
    private TextView sex;
    private TextView city;
    private TextView introduce;
    private JSONObject userinfo=new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
        ActivityUtils.StatusBarLightMode(this);
        ActivityUtils.setStatusBarColor(this, R.color.colorPrimary);//设置状态栏颜色
        username=findViewById(R.id.username);
        sex=findViewById(R.id.sex);
        city=findViewById(R.id.city);
        introduce=findViewById(R.id.intro);
        String info=getIntent().getStringExtra("info");
        try{
            userinfo=new JSONObject(info);
        }catch (Exception e){
        }
        initView();
    }

    public void initView(){
        try{
            username.setText(userinfo.getString("username"));
            sex.setText(userinfo.getString("sex"));
            city.setText(userinfo.getString("city"));
            introduce.setText(userinfo.getString("introduce"));
        }catch (Exception e){

        }
    }

}
