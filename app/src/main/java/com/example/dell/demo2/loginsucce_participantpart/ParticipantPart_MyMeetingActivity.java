package com.example.dell.demo2.loginsucce_participantpart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.adapter.MeetingAdapter;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ParticipantPart_MyMeetingActivity extends AppCompatActivity {
    private TextView tv_default;
    private ListView lv_ongoing_meeting,lv_over_meeting;
    private Gson gson;
    private MeetingAdapter meetingAdapter;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_part__my_meeting);
        init();
        Intent i=getIntent();
        String responseJsonStr= i.getStringExtra("responseJsonStr");
        List<List<MeetingBean>> list=new ArrayList<List<MeetingBean>>();
        List<MeetingBean> meetingOverList=new ArrayList<MeetingBean>();
        List<MeetingBean> ongoingMeetingList=new ArrayList<MeetingBean>();
        System.out.println("这次难道没有回忆了："+responseJsonStr);
        if(responseJsonStr.equals("")||responseJsonStr==null){
            lv_ongoing_meeting.setVisibility(View.GONE);
            lv_over_meeting.setVisibility(View.GONE);
            tv_default.setText("暂无会议信息！");
        }else{
            list= gson.fromJson(responseJsonStr, new TypeToken<List<List<MeetingBean>>>(){}.getType());
            meetingOverList=list.get(0);
            ongoingMeetingList=list.get(1);
            Toast.makeText(ParticipantPart_MyMeetingActivity.this, "lallal::"+ongoingMeetingList.size(), Toast.LENGTH_SHORT).show();
            /*if(meetingOverList==null&&meetingOverList.isEmpty()){
                //仅仅判断了是否有正在进行的会议，而没有判断和展示已经结束了的会议
                lv_ongoing_meeting.setVisibility(View.GONE);
                lv_over_meeting.setVisibility(View.GONE);
                tv_default.setVisibility(View.VISIBLE);
                tv_default.setText("暂无会议信息！");
            }else{*/
                lv_ongoing_meeting.setVisibility(View.VISIBLE);
                lv_over_meeting.setVisibility(View.GONE);//当两个list同时存在时，添加item点击事件无效
                tv_default.setVisibility(View.GONE);
                meetingAdapter = new MeetingAdapter(this, ongoingMeetingList);
                lv_ongoing_meeting.setAdapter(meetingAdapter);
                System.out.println("进了else participant界面呐！！");
                System.out.println("进了else participant界面呐！！");
                System.out.println("进了else 加载了适配器！！");
            //}




        }

        final List<MeetingBean> finalOngoingMeetingList = ongoingMeetingList;

        lv_ongoing_meeting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //添加item点击事件，展示会议详情，有会议资料则可下载..
                System.out.println("点击了会议啦阿拉啦啦啦啦啦");
                System.out.println("点击了会议啦阿拉啦啦啦啦啦");
                //System.out.println(finalOngoingMeetingList.get(i).getMeetingAgenda());

                MeetingBean meeting= finalOngoingMeetingList.get(i);
                int id = meeting.getMeetingId();
                Intent intent=new Intent(ParticipantPart_MyMeetingActivity.this,ParticipantPart_MeetingDetailActivity.class);
                intent.putExtra("meeting",meeting);
                intent.putExtra("meetingId",id+"");
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });

       /* lv_over_meeting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("点击了会议啦阿拉啦啦啦啦啦");
                System.out.println("点击了会议啦阿拉啦啦啦啦啦");
                System.out.println("点击了会议啦阿拉啦啦啦啦啦");
            }
        });*/



    }


    public void init(){
        tv_default=findViewById(R.id.tv_participant_default);
        lv_ongoing_meeting=findViewById(R.id.lv_ongoing_meeting);
        lv_over_meeting=findViewById(R.id.lv_over_meeting);
        gson=new Gson();
        phone=getIntent().getStringExtra("phone");
    }
}
