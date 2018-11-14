package com.example.mac.sport.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private String email=null;
    private static final String baseUrl = "http://wz.oranme.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        if(getIntent().hasExtra("email"))
            email=getIntent().getStringExtra("email");
        initView();
        setListener();
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btGo = findViewById(R.id.bt_go);
        cv = findViewById(R.id.cv);
        fab = findViewById(R.id.fab);
        if(email!=null)
            etUsername.setText(email);
    }

    private void setListener(){
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //登录
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                NetInterface netInterface=retrofit.create(NetInterface.class);
                email=etUsername.getText().toString();
                String password=etPassword.getText().toString();
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
                                Toast.makeText(LoginMain.this,result.get("message").toString(),Toast.LENGTH_LONG).show();
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



