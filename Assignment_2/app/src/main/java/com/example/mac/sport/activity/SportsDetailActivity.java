package com.example.mac.sport.activity;

import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mac.sport.R;
import com.example.mac.sport.utils.ActivityUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SportsDetailActivity extends AppCompatActivity{
    private SurfaceView surfaceView;
    private SeekBar seekBar;
    private SurfaceHolder holder;
    private MediaPlayer mediaPlayer;
    private Button stop;
    private TextView now_time;
    private TextView back_text;
    private TextView sport_intro;

    private boolean isPause=false;
    private Thread thread;
    private String all_time;
    private boolean start=false;
    private Timer timer;
    private boolean isSeekBarChanging;//进度条是否被拖动
    private JSONObject detailInfo;

    //接受多线程信息，安卓中不允许主线程实现UI更新
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            seekBar.setProgress(msg.what);
            now_time.setText(formatTime(msg.what)+"/"+all_time);

        }
    };

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.sports_detail_info);
        ActivityUtils.StatusBarLightMode(this);
        ActivityUtils.setStatusBarColor(this, R.color.colorPrimary);//设置状态栏颜色
        String temp=getIntent().getStringExtra("detailInfo");
        try{
            detailInfo=new JSONObject(temp);
        }catch (Exception e){
        }
        initView();
        initListener();
        initData();
    }

    public void initView(){
        surfaceView=findViewById(R.id.surfaceView);
        seekBar=findViewById(R.id.progressBar);
        mediaPlayer=new MediaPlayer();
        stop=findViewById(R.id.stop);
        now_time=findViewById(R.id.now_time);
        back_text=findViewById(R.id.back);
        sport_intro=findViewById(R.id.sport_intro);


        try{
            //可以为url
            AssetFileDescriptor fileDescriptor=getAssets().openFd("test.mp4");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),fileDescriptor.getLength());
            holder=surfaceView.getHolder();
            holder.addCallback(new MyCallBack());
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    thread=new Thread(new SeekBarThread());
                    thread.start();
                    start=true;
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //各种事件
    public void initListener(){
        //返回界面
        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SportsDetailActivity.this.finish();
            }
        });

        //进度条拖动
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarChanging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarChanging=false;
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        //暂停，继续
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPause){
                    mediaPlayer.pause();
                    Drawable drawable=getResources().getDrawable(R.drawable.start);
                    stop.setBackground(drawable);
                    isPause=true;
                }
                else{
                    Drawable drawable=getResources().getDrawable(R.drawable.stop);
                    stop.setBackground(drawable);
                    mediaPlayer.start();
                    isPause=false;
                }
            }
        });
    }

    //初始化数据
    public void initData(){
        try{
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            all_time=formatTime(mediaPlayer.getDuration());
            now_time.setText("0:00/"+all_time);
            sport_intro.setText(detailInfo.getString("introduce"));
        }catch (Exception e){

        }
    }

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    //时间格式转换,int->string
    private String formatTime(int length) {
        Date date = new Date(length);//调用Date方法获值
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");//规定需要形式
        String TotalTime = simpleDateFormat.format(date);//转化为需要形式
        return TotalTime;
    }

    //使用handler每秒更新进度条和时间显示
    class SeekBarThread implements Runnable {
        @Override
        public void run() {
            while (start) {
                // 将SeekBar位置设置到当前播放位置
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
                try {
                    // 每100毫秒更新一次位置
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPause(){
        start=false;
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onPause();
    }

}
