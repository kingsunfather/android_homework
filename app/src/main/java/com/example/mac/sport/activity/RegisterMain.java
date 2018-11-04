package com.example.mac.sport.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mac.sport.NetWork.NetInterface;
import com.example.mac.sport.R;
import com.example.mac.sport.entity.Result;
import com.example.mac.sport.entity.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterMain extends AppCompatActivity{
    private FloatingActionButton fab;
    private CardView cvAdd;
    private Button register;
    private EditText username_edit;
    private EditText password_edit;
    private EditText ensure_password_edit;
    private String username;
    private String pass;
    private String ensure_pass;
    private static final String baseUrl = "http://wz.oranme.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        ShowEnterAnimation();
        initView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
        username_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    username=username_edit.getText().toString();
                    if(!username.equals("")){
                        //检测姓名重复
                        Toast.makeText(RegisterMain.this,"检验名字是否重复",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        ensure_password_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                pass=password_edit.getText().toString();
                ensure_pass=ensure_password_edit.getText().toString();
                if(!hasFocus){
                    if(!pass.equals(ensure_pass)&&!ensure_pass.equals("")){
                        Toast.makeText(RegisterMain.this,"两次输入密码不同",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=username_edit.getText().toString();
                pass=password_edit.getText().toString();
                ensure_pass=ensure_password_edit.getText().toString();
                User user=new User(username,pass);
                //NetImp.register(user);
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                NetInterface netInterface=retrofit.create(NetInterface.class);
                Call<Result> call=netInterface.register(user);
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        System.out.println(response.body());
                        Result res=response.body();
                        if(res.getCode().equals("200")){
                            Intent intent=new Intent();
                            intent.setClass(RegisterMain.this,LoginMain.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("email",username);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(RegisterMain.this,res.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        System.out.print("-----------------------------------------------------------------");
                        System.out.println(t.getMessage());
                    }
                });
            }
        });
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        cvAdd = findViewById(R.id.cv_add);
        register=findViewById(R.id.bt_go);
        username_edit=findViewById(R.id.et_username);
        password_edit=findViewById(R.id.et_password);
        ensure_password_edit=findViewById(R.id.et_repeatpassword);
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterMain.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

}

