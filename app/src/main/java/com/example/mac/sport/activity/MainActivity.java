package com.example.mac.sport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;


import com.baidu.mapapi.SDKInitializer;
import com.example.mac.sport.NetWork.NetInterface;
import com.example.mac.sport.R;
import com.example.mac.sport.adapter.ViewPagerAdapter;
import com.example.mac.sport.fragment.TodayFragment;
import com.example.mac.sport.fragment.HomeFragment;
import com.example.mac.sport.fragment.UserFragment;
import com.example.mac.sport.utils.ActivityUtils;
import com.example.mac.sport.utils.BottomNavigationViewHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.toolbartext)
    TextView toolbartext;
    private MenuItem menuItem;
    private boolean mIsExit;
    private static final String baseUrl = "http://wz.oranme.com/";
    //用户email
    public static String email=null;
    //运动的信息
    public static JSONObject sports=new JSONObject();
    private  ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        email=getIntent().getStringExtra("email");
        ButterKnife.bind(this);
        ActivityUtils.StatusBarLightMode(this);
        ActivityUtils.setStatusBarColor(this, R.color.colorPrimary);//设置状态栏颜色
        initData();
    }

    private void initView() {

        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(navigation);

        //设置ViewPageAdapter，运动，今日，我的，三个碎片
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new TodayFragment());
        adapter.addFragment(new UserFragment());

        //设置页面中间的ViewPager的适配器
        viewpager.setAdapter(adapter);
        //一开始加载的界面是运动界面
        viewpager.setCurrentItem(0);

        //缓存3个页面，来解决点击“我的”回来，首页空白的问题，
        // 存在的问题！！如果有的页面不需要缓存该如何自动刷新，可以利用eventbus传参来进行该页面的操作
        //viewpager.setOffscreenPageLimit(3);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_sports:
                        viewpager.setCurrentItem(0);//运动
                        toolbartext.setText("运动");
                        return true;
                    case R.id.navigation_today:
                        viewpager.setCurrentItem(1);//今日
                        toolbartext.setText("今日");
                        return true;
                    case R.id.navigation_user:
                        viewpager.setCurrentItem(2);//我的
                        toolbartext.setText("我");
                        return true;
                }
                return false;
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //运动页面的刷新
    private void initData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetInterface netInterface = retrofit.create(NetInterface.class);
        JSONObject jsonObject = new JSONObject();

        //获取所有的运动
        try {
            Call<ResponseBody> call = netInterface.getAllCategorys(jsonObject);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String res = new String(response.body().bytes());
                        JSONObject result = new JSONObject(res);
                        sports = result;
                        initView();
                        //adapter.refreshMofifiedPageData(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } catch (Exception e) {
        }
    }
    /**
     * 双击退出应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    public String getEmail(){
        return email;
    }

    public JSONObject getSports(){
        return sports;
    }
}

