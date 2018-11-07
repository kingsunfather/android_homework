package com.example.mac.sport.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mac.sport.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


/**
 * Created by KingSun on 2018/10/02 1006.
 */

public class RecycleViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public RecycleViewAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        try{
            JSONObject jsonObject=new JSONObject(item);
            helper.setText(R.id.courseName,jsonObject.getString("sportsName"));
            helper.setText(R.id.courseDetail,jsonObject.getString("introduce"));
            helper.setText(R.id.coachName,jsonObject.getString("coach"));
            helper.addOnClickListener(R.id.courseName);
        }catch (Exception e){

        }
    }
}

