package com.example.mac.sport.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mac.sport.NetWork.NetInterface;
import com.example.mac.sport.R;
import com.example.mac.sport.activity.MainActivity;
import com.example.mac.sport.adapter.RecycleViewGridAdapter;
import com.example.mac.sport.adapter.TabFragmentAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 运动碎片
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.tab_viewpager)
    ViewPager tabViewpager;
    Unbinder unbinder;
//    @BindView(R.id.iv_fenlei)
//    ImageView mIvFenlei;
    public static JSONObject sports=new JSONObject();
    private static final String baseUrl = "http://wz.oranme.com/";
    TabFragmentAdapter tabFragmentAdapter;


    private List<Fragment> mFragmentArrays = new ArrayList<>(); //表示乒乓球，羽毛球，篮球，足球，瑜伽的碎片列表
    private List<String> mTabs = new ArrayList<>();  //表示乒乓球，羽毛球，篮球，足球，瑜伽的 类别名字 列表
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //解决点击“我的”回来方法二，首页空白的问题，推荐的方法
        if (view != null) {
            unbinder = ButterKnife.bind(this, view);//必须加，不然报ButterKnife的异常
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }

        //如果之前没有创建过这个碎片的话，用inflater从xml布局文件中实例化
        view = inflater.inflate(R.layout.fragment_sports, container, false);

        unbinder = ButterKnife.bind(this, view);//这里也得有，不然报ButterKnife的异常
        initData();
        initView(view);
        return view;
    }

    public void initData(){
        sports=MainActivity.sports;
    }

    //刷新
    public void updateData(){
        tablayout.removeAllTabs();
        tabViewpager.removeAllViews();
        if (mFragmentArrays != null) {
            mFragmentArrays.clear();
            mTabs.clear();
        }
        //替换成从服务器接口请求数据就成动态了
        try{
            JSONArray array=sports.getJSONArray("data");
            System.out.println("---------------------------------------------------------");
            System.out.println(sports);
            for(int i=0;i<array.length();i++){
                JSONObject temp=array.getJSONObject(i);
                mTabs.add(temp.getString("categoryName"));
            }
        }catch (Exception e){

        }
        //动态添加Fragment
        for (int i = 0; i < mTabs.size(); i++) {
            Fragment fragment = new TabFragment();
            //这里使用fragment.setArguments(bundle)来传递构建fragment的参数信息，为什么不用fragment的构造方法呢？
            // 因为如果activity在横竖屏切换的时候，会重新构建fragmentManager，进而会重新构建fragment，此时会丢失原来实例化的信息。
            //bundle.putInt中，其中position与i是map关系
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            mFragmentArrays.add(fragment);
        }
        tabFragmentAdapter.setData(mFragmentArrays,mTabs);
        tabFragmentAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
     //   mIvFenlei.setOnClickListener(this);
        tablayout.removeAllTabs();
        tabViewpager.removeAllViews();
        if (mFragmentArrays != null) {
            mFragmentArrays.clear();
            mTabs.clear();
        }
        //替换成从服务器接口请求数据就成动态了
        try{
            JSONArray array=sports.getJSONArray("data");
            System.out.println("---------------------------------------------------------");
            System.out.println(sports);
            for(int i=0;i<array.length();i++){
                JSONObject temp=array.getJSONObject(i);
                mTabs.add(temp.getString("categoryName"));
            }
        }catch (Exception e){

        }
        //动态添加Fragment
        for (int i = 0; i < mTabs.size(); i++) {
            Fragment fragment = new TabFragment();
            //这里使用fragment.setArguments(bundle)来传递构建fragment的参数信息，为什么不用fragment的构造方法呢？
            // 因为如果activity在横竖屏切换的时候，会重新构建fragmentManager，进而会重新构建fragment，此时会丢失原来实例化的信息。
            //bundle.putInt中，其中position与i是map关系
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            mFragmentArrays.add(fragment);
        }
        //用来适配运动界面的adapter，用来加载管理有几个运动项目，每个运动项目的碎片，每个运动项目的名称
        tabFragmentAdapter=new TabFragmentAdapter(getFragmentManager(), mFragmentArrays, mTabs);
        tabViewpager.setAdapter(tabFragmentAdapter);
        //将tablayout和viewPager建立联系，一一对应，上面滑动的时候viewPager也跟着滑动，下面viewPager滑动的时候，上面的tabLayout也跟着滑动
        tablayout.setupWithViewPager(tabViewpager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.iv_fenlei:
//                startPopuwindows(view);
//                break;
        }
    }

    //一下展开全部的运动分类
    private void startPopuwindows(View view1) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.layout_main_popuwindows,null);
        RecyclerView recyclerView=view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),5));
        RecycleViewGridAdapter gridAdapter=new RecycleViewGridAdapter(R.layout.item_gride_fenlei,mTabs);
        recyclerView.setAdapter(gridAdapter);

        final PopupWindow popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.showAsDropDown(view1);

        gridAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(),"点击了"+mTabs.get(position), Toast.LENGTH_SHORT).show();
                tabViewpager.setCurrentItem(position);
                popupWindow.dismiss();
            }
        });
        gridAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                tabViewpager.setCurrentItem(position);
                popupWindow.dismiss();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //黄油刀解绑
        unbinder.unbind();
    }
    public JSONObject getSports(){
        return sports;
    }
}
