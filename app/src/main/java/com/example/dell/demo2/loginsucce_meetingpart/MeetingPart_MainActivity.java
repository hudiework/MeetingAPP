package com.example.dell.demo2.loginsucce_meetingpart;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.create_meeting.meeting_manage_activity.MeetingAddActivity;
import com.example.dell.demo2.listview.MyMessageActivity;
import com.example.dell.demo2.supervisor.OkHttpUtils;
import com.example.dell.demo2.web_url_help.Url;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class MeetingPart_MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private String phone;
    private Bundle bundle;
    private String str;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_meeting:
                    //获取到FragmentManager，在V4包中通过getSupportFragmentManager，
                    //在系统中原生的Fragment是通过getFragmentManager获得的。
                    FragmentManager FM1 = getFragmentManager();
                    //2.开启一个事务，通过调用beginTransaction方法开启。
                    FragmentTransaction MfragmentTransaction1 =FM1.beginTransaction();
                    //把自己创建好的fragment创建一个对象
                    MeetingFragment meetingFragment = new MeetingFragment();
                    meetingFragment.setArguments(bundle);
                    //向容器内加入Fragment，一般使用add或者replace方法实现，需要传入容器的id和Fragment的实例。
                    MfragmentTransaction1.replace(R.id.fragment_buju,meetingFragment);
                    //提交事务，调用commit方法提交。
                    MfragmentTransaction1.commit();
                    return true;

                case R.id.navigation_message:
                    FragmentManager FM2 = getFragmentManager();
                    FragmentTransaction MfragmentTransaction2 =FM2.beginTransaction();
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setArguments(bundle);
                    MfragmentTransaction2.replace(R.id.fragment_buju,messageFragment);
                    MfragmentTransaction2.commit();
                    return true;
                case R.id.navigation_add:
                    FragmentManager FM3 = getFragmentManager();
                    FragmentTransaction MfragmentTransaction3 =FM3.beginTransaction();
                    AddMeetingFragment mineFragment = new AddMeetingFragment();
                    mineFragment.setArguments(bundle);
                    MfragmentTransaction3.replace(R.id.fragment_buju,mineFragment);
                    MfragmentTransaction3.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_part__main);

        String url= Url.URLa+"ParticipantMeetingServlet";
        Map<String,String> map=new HashMap<String,String>();
        map.put("mobilePhone","18510801374");
        OkHttpUtils.post(url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("错误来了：", "执行错误回调！！！ ", e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseJsonStr= response.body().string();
                System.out.println("获取到参加过的会议啊啊啊啊"+responseJsonStr);
                System.out.println("获取到参加过的会议啊啊啊啊"+responseJsonStr);
                System.out.println("获取到参加过的会议啊啊啊啊"+responseJsonStr);
                Message msg1 = handler.obtainMessage();
                msg1.what=222;
                msg1.obj=responseJsonStr;
                handler.sendMessage(msg1);
            }
        } ,map);


        phone=getIntent().getStringExtra("phone");
       // str=getIntent().getStringExtra("str");

        bundle = new Bundle();
        bundle.putString("phone",phone);
       // bundle.putString("phone",str);
        initView();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initView() {
        mTextMessage = (TextView) findViewById(R.id.message);

    }

   Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = msg.obj.toString();
            System.out.println("获取到：：：：：" + str);
            bundle.putString("str",str);
        }
    };
}



