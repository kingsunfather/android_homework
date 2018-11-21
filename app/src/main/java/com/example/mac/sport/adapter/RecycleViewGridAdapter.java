package com.example.mac.sport.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mac.sport.R;

import java.util.List;


/**
 * Created by KingSun on 2018/10/02 1102.
 * 这个适配器用来适配点击全部运动分类图标的时候一下就全部显示在页面上方的内容
 */

public class RecycleViewGridAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public RecycleViewGridAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_tv,item);
        helper.addOnClickListener(R.id.item_tv);
    }
}
