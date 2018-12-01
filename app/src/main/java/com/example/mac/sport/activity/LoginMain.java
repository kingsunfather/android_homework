package com.example.mac.sport.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.example.mac.sport.NetWork.NetInterface;
import com.example.mac.sport.R;
import com.example.mac.sport.entity.Result;
import com.example.mac.sport.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginMain extends AppCompatActivity{
    private EditText etUsername;
    private EditText etPassword;
    private Button btGo;
    private CardView cv;
    private FloatingActionButton fab;
    private TextInputLayout login_pass_label;
    private TextInputLayout login_email_label;
    private String email=null;
    private static final String baseUrl = "http://wz.oranme.com/";
//    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.login_main);
        if(getIntent().hasExtra("email"))
            email=getIntent().getStringExtra("email");
        initView();
        setListener();
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        login_pass_label=findViewById(R.id.login_pass_label);
        login_email_label=findViewById(R.id.login_email_label);
        btGo = findViewById(R.id.bt_go);
        cv = findViewById(R.id.cv);
        fab = findViewById(R.id.fab);
        if(email!=null)
            etUsername.setText(email);

//            // 检查是否获得了权限（Android6.0运行时权限）
//            if (ContextCompat.checkSelfPermission(LoginMain.this,
//                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//                // 没有获得授权，申请授权
//                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginMain.this,
//                        Manifest.permission.CALL_PHONE)) {
//                    // 返回值：
////                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
////                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
////                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
//                    // 弹窗需要解释为何需要该权限，再次请求授权
//                    Toast.makeText(LoginMain.this, "请授权！", Toast.LENGTH_LONG).show();
//
//                    // 帮跳转到该应用的设置界面，让用户手动授权
//                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                    intent.setData(uri);
//                    startActivity(intent);
//                }else{
//                    // 不需要解释为何需要该权限，直接请求授权
//                    ActivityCompat.requestPermissions(LoginMain.this,
//                            new String[]{Manifest.permission.CALL_PHONE},
//                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
//                }
//            }else {
//                // 已经获得授权，可以打电话
//            }

    }

    private void setListener(){
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=etUsername.getText().toString();
                String password=etPassword.getText().toString();
                if(email.length()==0){
                    login_email_label.setError("请输入邮箱");
                    return;
                }
                if(password.length()==0){
                    login_pass_label.setError("请输入密码");
                    return;
                }
                //登录
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                NetInterface netInterface=retrofit.create(NetInterface.class);
                User user=new User(email,password);
                Call<ResponseBody> call=netInterface.login(user);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try
                        {
                            String res=new String(response.body().bytes());
                            JSONObject result=new JSONObject(res);
                            if(result.getString("code").equals("200")){
                                Explode explode = new Explode();
                                explode.setDuration(500);
                                getWindow().setExitTransition(explode);
                                getWindow().setEnterTransition(explode);
//                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginMain.this);
                                Intent i2 = new Intent(LoginMain.this,MainActivity.class);
                                i2.putExtra("email",email);
                                startActivity(i2);
                            }
                            else
                                login_pass_label.setError("账号或密码错误");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginMain.this, fab, fab.getTransitionName());
                startActivity(new Intent(LoginMain.this, RegisterMain.class), options.toBundle());
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onRestart() {
        super.onRestart();
        fab.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }
}



