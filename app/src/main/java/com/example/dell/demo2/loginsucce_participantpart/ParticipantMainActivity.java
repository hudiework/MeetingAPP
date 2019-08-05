package com.example.dell.demo2.loginsucce_participantpart;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.listview.SearchMeetingActivity;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingFragment;
import com.example.dell.demo2.loginsucce_meetingpart.MessageFragment;
import com.example.dell.demo2.supervisor.OkHttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ParticipantMainActivity extends AppCompatActivity {

    private EditText et_searchMeeting;
    private Spinner sp;
    private String[] list;
    private int position;
    private static final String HTTPPATH = Url.URLa+"SearchServlet";
    private List<MeetingBean> meetings = new ArrayList<>();
    private String participantResponseJsonStr;
    private String phone;
    private Bundle bundle;

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
                    //首先有一个Fragment对象 调用这个对象的setArguments(bundle)传递数据

                    //向容器内加入Fragment，一般使用add或者replace方法实现，需要传入容器的id和Fragment的实例。
                    MfragmentTransaction1.replace(R.id.fragment_buju,meetingFragment);
                    //提交事务，调用commit方法提交。
                    MfragmentTransaction1.commit();

                    return true;
                case R.id.navigation_add:
                    FragmentManager FM2 = getFragmentManager();
                    FragmentTransaction MfragmentTransaction2 =FM2.beginTransaction();
                    ApplyMeetingFragment applyMeetingFragment = new ApplyMeetingFragment();
                    applyMeetingFragment.setArguments(bundle);
                    MfragmentTransaction2.replace(R.id.fragment_buju,applyMeetingFragment);
                    MfragmentTransaction2.commit();;
                    return true;
                case R.id.navigation_message:
                    FragmentManager FM3 = getFragmentManager();
                    FragmentTransaction MfragmentTransaction3 =FM3.beginTransaction();
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setArguments(bundle);
                    MfragmentTransaction3.replace(R.id.fragment_buju,messageFragment);
                    MfragmentTransaction3.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_main);
        phone=getIntent().getStringExtra("phone");
        bundle = new Bundle();
        bundle.putString("phone",phone);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /*
     * 初始化控件
    public void initCtrl(){
        et_searchMeeting=findViewById(R.id.et_searchMeeting);
        sp=findViewById(R.id.sp);
        list=new String[]{"会议号","会议名"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position =i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_search:{
                String sRegex = "^[1-9]\\d*$";
                Pattern p = Pattern.compile(sRegex);
                Matcher m = p.matcher(et_searchMeeting.getText().toString());
                if(position==0 && !m.matches()){
                    Toast.makeText(ParticipantMainActivity.this,"请输入整数！",Toast.LENGTH_LONG).show();
                }else{
                    final String str="type="+position+"&content="+et_searchMeeting.getText().toString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = handler.obtainMessage();
                            try {
                                Log.e("*****",str);
                                message.obj= HttpUtils.httpURLConnectionGetData(HTTPPATH,
                                        str, "utf-8", "text/html");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            handler.sendMessage(message);
                        }
                    }).start();

                }
            }break;
        }

    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = msg.obj.toString();
            Log.e("**********",str);
            Gson gson = new Gson();
            meetings= gson.fromJson(str, new TypeToken<List<MeetingBean>>() {
            }.getType());
            if(meetings.size()==0){
                Toast.makeText(ParticipantMainActivity.this,"无此会议",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent=new Intent(ParticipantMainActivity.this, SearchMeetingActivity.class);
                intent.putExtra("meetings",str);
                startActivity(intent);
            }

        }
    };*/

}
