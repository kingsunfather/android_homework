package com.example.mac.sport.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mac.sport.R;

import java.util.List;


/**
 * Created by KingSun on 2018/10/02 1006.
 */

public class RecycleViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public RecycleViewAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.courseName,item);

        helper.addOnClickListener(R.id.courseName);

    }


}
