package com.example.mac.sport.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.sport.R;
import com.example.mac.sport.utils.ActivityUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInfo extends AppCompatActivity implements View.OnClickListener{
    private TextView username;
    private TextView sex;
    private TextView city;
    private TextView introduce;
    private JSONObject userinfo=new JSONObject();
    private List<String> sexList=new ArrayList<>(Arrays.asList("男","女","未知"));
    private List<String> cityList=new ArrayList<>(Arrays.asList("北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
        ActivityUtils.StatusBarLightMode(this);
        ActivityUtils.setStatusBarColor(this, R.color.colorPrimary);//设置状态栏颜色
        //事件绑定
        username=findViewById(R.id.username);
        username.setOnClickListener(this);
        sex=findViewById(R.id.sex);
        sex.setOnClickListener(this);
        city=findViewById(R.id.city);
        city.setOnClickListener(this);
        introduce=findViewById(R.id.intro);
        introduce.setOnClickListener(this);
        String info=getIntent().getStringExtra("info");
        try{
            userinfo=new JSONObject(info);
        }catch (Exception e){
        }
        initView();
    }

    public void initView(){
        try{
            if(userinfo.has("username"))
                username.setText(userinfo.getString("username"));
            if(userinfo.has("sex"))
                sex.setText(userinfo.getString("sex"));
            if(userinfo.has("city"))
                city.setText(userinfo.getString("city"));
            if(userinfo.has("introduce"))
                introduce.setText(userinfo.getString("introduce"));
        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View view){
        int viewId=view.getId();
        switch (viewId){
            case R.id.username:
                alert_edit(view,"编辑昵称");
                break;
            case R.id.sex:
                selectTypeDiag(sexList,"选择性别",viewId);
                break;
            case R.id.city:
                selectTypeDiag(cityList,"选择城市",viewId);
                break;
            case R.id.intro:
                alert_edit(view,"编辑简介");
                break;
        }
    }

    //弹出列表框
    private void selectTypeDiag(final List<String> list, String title, final int viewId) {
        int len=list.size();
        final String items[]=new String[len];//不能直接用list.size，会出错
        for (int i=0;i<list.size();i++){
            items[i]=list.get(i);
        }
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle(title); //设置标题
        //builder.setMessage("是否确认退出?"); //设置内容
        //builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (viewId){
                    case R.id.sex:
                        sex.setText(items[which]);
                        break;
                    case R.id.city:
                        city.setText(items[which]);
                        break;
                }
            }
        });
        builder.create();
        builder.create().show();
    }

    //弹出输入框
    public void alert_edit(final View view, String title){
        final EditText et = new EditText(this);
        et.setGravity(Gravity.CENTER);
        new AlertDialog.Builder(this).setTitle(title)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id=view.getId();
                        switch (id){
                            case R.id.username:
                                username.setText(et.getText());
                                break;
                            case R.id.intro:
                                introduce.setText(et.getText());
                                break;
                        }
                    }
                }).setNegativeButton("取消",null).show();
    }

    //修改个人信息接口
    public void editUserInfo(){

    }
}
