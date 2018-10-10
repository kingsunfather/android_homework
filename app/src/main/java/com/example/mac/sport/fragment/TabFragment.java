package com.example.mac.sport.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.mac.sport.R;
import com.example.mac.sport.activity.MainActivity;
import com.example.mac.sport.adapter.RecycleViewAdapter;
import com.example.mac.sport.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kingsun on 2018/10/02 1001
 * 这个fragment用来控制例如：足球，乒乓球，羽毛球的每个界面布局和内容
 */

public class TabFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private List<String> mdata = new ArrayList<>();
    private List<String> imageUrl = new ArrayList<>();
    int mPosition; //表示自己是属于足球or乒乓球or羽毛球
    private RecycleViewAdapter mAdapter;
    private Banner mBanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmeny_tab, container, false);
        //在绑定碎片的时候，黄油刀会放回一个unbinder，为了在destroy这个碎片的时候用Unbinder里面的unbind方法进行解绑
        //在activity 的destroy里面会自动解除绑定，但是在fragment里面不会自动解除绑定
        unbinder = ButterKnife.bind(this, rootView);

        //从bundle里面得到自己是属于足球or乒乓球or篮球
        mPosition = getArguments().getInt("position");

        //这里将每个运动类别的视图都做的一样，要是想每个运动视图做的不一样的话，那么可以将mPosition当作参数传入initView()方法里面
        initView();
        initData();

        return rootView;
    }


    private void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAdapter = new RecycleViewAdapter(R.layout.home_item_view, mdata);

        View top = getLayoutInflater().inflate(R.layout.layout_banner, (ViewGroup) mRecyclerView.getParent(), false);
        mBanner = top.findViewById(R.id.banner);

        //设置头部内容
        mAdapter.addHeaderView(top);
        //开启默认的滑动加载动画
        mAdapter.openLoadAnimation();

        //给mRecycle增加点击事件的监听
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }

            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position){
                super.onItemChildClick(adapter, view, position);
                int itemViewId = view.getId();
                switch (itemViewId){
                    case R.id.isLike:{
                        Toast ss= Toast.makeText(getContext(),"参加状态切换",Toast.LENGTH_LONG);
                        ss.show();
                        break;
                }


            }
        }});

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            mdata.add("TabFragment_" + mPosition + "第" + i + "条数据");
        }

        mAdapter.setNewData(mdata);//模拟网络请求成功后要调用这个方法刷新数据

        //只在第一个页面加上banner，根据需求来添加banner
        if (mPosition == 0) {
            imageUrl.clear();
            imageUrl.add("http://mpic.tiankong.com/aa4/fd8/aa4fd84a633298f43fe4521ba9a2dcbc/640.jpg");
            imageUrl.add("http://mpic.tiankong.com/34d/ee2/34dee24f36c176651e0b64dbc8f5d170/640.jpg");
            imageUrl.add("http://mpic.tiankong.com/5a4/a2d/5a4a2dc36ad6d42ba95ee8c2afd8e038/640.jpg");
            initBanner(imageUrl);
        } else {
            mBanner.setVisibility(View.GONE);
        }

    }

    //初始化banner的图片，利用url去请求图片，而不用本地保存
    private void initBanner(List<String> imageUrl) {
        mBanner.setImages(imageUrl)
                .setImageLoader(new GlideImageLoader())
                .setDelayTime(3000)
                .start();
        mBanner.setOnBannerListener(new OnBannerListener() {
            //这里可以点击banner然后进行操作，但是还没有写实现的内容，等待第二阶段补上！！
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除黄油刀的绑定
        unbinder.unbind();
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        mBanner.stopAutoPlay();
    }
}
