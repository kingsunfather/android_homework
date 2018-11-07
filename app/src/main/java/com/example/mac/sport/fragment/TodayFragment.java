package com.example.mac.sport.fragment;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import com.example.mac.sport.NetWork.NetInterface;
import com.example.mac.sport.R;
import com.example.mac.sport.activity.MainActivity;
import com.example.mac.sport.adapter.QuickAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 今日
 */
public class TodayFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<String> mData=new ArrayList<>();
    private QuickAdapter<String> quickAdapter;
    private static final String baseUrl = "http://wz.oranme.com/";
    JSONObject mySports=new JSONObject();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.fragment_today, container, false);
        View view = inflater.inflate(R.layout.fragment_today_new, container, false);
        recyclerView=view.findViewById(R.id.today_list);
        initView();
        initData();
        return view;
    }

    //从服务器获取数据
    public void initData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetInterface netInterface = retrofit.create(NetInterface.class);
        JSONObject jsonObject = new JSONObject();
        //获取自己所有的运动
        try {
            jsonObject.put("email", MainActivity.email);
            Call<ResponseBody> call = netInterface.getUserSports(jsonObject);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String res = new String(response.body().bytes());
                        JSONObject result = new JSONObject(res);
                        mySports=result;
                        JSONArray array=result.getJSONArray("data");
                        for(int i=0;i<array.length();i++)
                            mData.add(array.getJSONObject(i).toString());
                        quickAdapter.updateData(mData);
                        quickAdapter.notifyDataSetChanged();
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

    public void initView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext() );
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        //设置Adapter
        quickAdapter=new QuickAdapter<String>(mData) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.today_item_view;
            }
            @Override
            public void convert(QuickAdapter.VH holder, String data, int position) {
                try{
                    JSONObject jsonObject=new JSONObject(data);
                    holder.setText(R.id.today_sports_name,jsonObject.getString("sportsName"));
                    holder.setText(R.id.today_coach_name,jsonObject.getString("coach"));
                    holder.setText(R.id.today_course_detail,jsonObject.getString("introduce"));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(),"hahaa",Toast.LENGTH_LONG).show();
                        }
                    });
                }catch (Exception e){
                }
            }
        };
        recyclerView.setAdapter(quickAdapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
