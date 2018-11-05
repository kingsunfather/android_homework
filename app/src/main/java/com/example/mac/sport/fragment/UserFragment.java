package com.example.mac.sport.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mac.sport.NetWork.NetInterface;
import com.example.mac.sport.R;
import com.example.mac.sport.activity.LoginMain;
import com.example.mac.sport.activity.MainActivity;
import org.json.JSONObject;
import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 我的
 */


public class UserFragment extends Fragment {

    Button logoutButton;
    Button modifyUser;
    TextView userName;
    private static final String baseUrl = "http://wz.oranme.com/";
    JSONObject userInfo=new JSONObject();
    public boolean hasInfo=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        logoutButton=view.findViewById(R.id.logoutButton);
        modifyUser=view.findViewById(R.id.modifyUser);
        userName=view.findViewById(R.id.userName);
        initView();
        initData();
        return view;
    }

    public void initView(){
        //点击退出
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialog();
            }
        });
    }

    //获取个人信息
    public void initData(){
        //只获取一次
        if(!hasInfo){
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            NetInterface netInterface=retrofit.create(NetInterface.class);
            JSONObject jsonObject=new JSONObject();
            try{
                jsonObject.put("email",MainActivity.email);
                Call<ResponseBody> call=netInterface.myInfo(jsonObject);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            String res=new String(response.body().bytes());
                            JSONObject result=new JSONObject(res);
                            userInfo=result.getJSONObject("data");
                            userName.setText(userInfo.getString("username"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
            }catch (Exception e){

            }
        }
    }

    //确认退出
    private void showNormalDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        normalDialog.setTitle("确认要退出吗?");
        normalDialog.setMessage("");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Retrofit retrofit=new Retrofit.Builder()
                                .baseUrl(baseUrl)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        NetInterface netInterface=retrofit.create(NetInterface.class);
                        JSONObject jsonObject=new JSONObject();
                        try{
                            jsonObject.put("email",MainActivity.email);
                            Call<ResponseBody> call=netInterface.logout(jsonObject);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try{
                                        String res=new String(response.body().bytes());
                                        JSONObject result=new JSONObject(res);
                                        if(result.getString("code").equals("200")){
                                            Intent intent=new Intent();
                                            intent.setClass(getActivity(), LoginMain.class);
                                            startActivity(intent);
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                }
                            });
                        }catch (Exception e){

                        }
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.show();
    }
}
