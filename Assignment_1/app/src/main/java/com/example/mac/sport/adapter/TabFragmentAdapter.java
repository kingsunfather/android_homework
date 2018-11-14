package com.example.mac.sport.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者：KingSun
 * 时间：2018/10/02 2050
 * 注释：Tablayout里viewpager的适配器，用来适配运动界面的adapter，用来加载管理有几个运动项目，每个运动项目的碎片，每个运动项目的名称
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {
    private  List<Fragment> mFragmentList;
    private  List<String> mTabTitle;

    public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> tabTitle) {
        super(fm);
        this.mFragmentList=fragmentList;
        this.mTabTitle=tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle.get(position % mTabTitle.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    public void setData(List<Fragment> fragmentList, List<String> tabTitle){
        this.mFragmentList=fragmentList;
        this.mTabTitle=tabTitle;
    }
}