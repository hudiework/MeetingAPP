package com.example.dell.demo2.loginsucce_participantpart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.download.FileListActivity;
import com.example.dell.demo2.download.LoadFileMainActivity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.listview.AllPeopleActivity;
import com.example.dell.demo2.listview.OrderRatingActivity;
import com.example.dell.demo2.listview.PartCommentActivity;
import com.example.dell.demo2.listview.SearchMeetingActivity;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.loginsucce_participantpart.entity.ParticipantBean;
import com.example.dell.demo2.supervisor.OkHttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ParticipantPart_MeetingDetailActivity extends AppCompatActivity {
    public TextView tv_participant_meetingName;//会议名
    private TextView tv_participant_meetingCompere;//主持人
    private  TextView tv_participant_meetingContent;//会议内容
    private TextView tv_participant_meetingDate;//开始时间
    private TextView tv_participant_meetingPlace;//地点
    private TextView tv_participant_meetingAgenda;//会议议程
    private TextView tv_participant_meetingGuide;//参会指南
    private TextView tv_participant_meetingOverDate;//结束时间
    private Button btn_participant_childMeeting;//查看会议子项
    private Button btn_participant_myChildMeeting;//查看我参加的会议子项
    private Button btn_participant_meetingDocument;//查看会议资料
    private Button btn_participant_meetingTalk;//进入坐席解答
    private MeetingBean meeting;
    private LinearLayout ll_childMeeting;
    private LinearLayout ll_myChildMeeting;
    private ListView lv_childMeeting;
    private ListView lv_myChildMeeting;
    private ChildMeetingAdapter childMeetingAdapter;
    private Gson gson;
    private List<ChildMeetingBean> childMeetingList;
    private TextView tv_participant_childmeeting_defalt;
    private TextView tv_participant_myChildmeeting_defalt;
    boolean childMeetingThreadFlag=false;
    boolean myChildMeetingThreadFlag=false;
    String childMeetingResponseJsonStr="";
    String myChildMeetingResponseJsonStr="";
    MyThread myThread;
    HashMap map;//查询服务器用来传递参数的map;
    String childMeetingMsgTag="childMeeting";
    String myChildMeetingMsgTag="myChildMeeting";
    //private MeetingAdapter meetingAdapter;
    String id,phone;
    ParticipantBean participantBean;
    private Button btnGo,btnComment;
    private static final String HTTPPATH = Url.URLa+"SearchParticipant";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_part__meeting_detail);

        init();//初始化
        Intent i=getIntent();
        meeting=(MeetingBean) i.getSerializableExtra("meeting");
        textViewSetText(tv_participant_meetingName,meeting.getMeetingName());
        textViewSetText(tv_participant_meetingCompere,meeting.getMeetingPeople());
        textViewSetText(tv_participant_meetingContent,meeting.getMeetingContent());
        textViewSetText(tv_participant_meetingDate,meeting.getMeetingDate());
        textViewSetText(tv_participant_meetingPlace,meeting.getMeetingPlace());
       /* textViewSetText(tv_participant_meetingAgenda,meeting.getMeetingAgenda());*/
        textViewSetText(tv_participant_meetingOverDate,meeting.getMeetingOverDate());
        //查看会议子项
        btn_participant_childMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_participant_childMeeting.getText().equals("查看会议子项")) {
                    btn_participant_childMeeting.setText("收起");
                    ll_childMeeting.setVisibility(View.VISIBLE);//点击查看子会议后，listview出现
                    //查询服务器
                    map=new HashMap<String,String>();
                    map.put("meetingId",meeting.getMeetingId()+"");
                    String urlServlet="ParticipantGetChildMeetingServlet";
                    //  (Map map,String urlServlet,boolean threadFlag,String responseJsonStr,String msgTag)
                    startThread(map,urlServlet,childMeetingThreadFlag,childMeetingResponseJsonStr,childMeetingMsgTag);
                }else{
                    btn_participant_childMeeting.setText("查看会议子项");
                    ll_childMeeting.setVisibility(View.GONE);
                    lv_childMeeting.setVisibility(View.GONE);
                    tv_participant_childmeeting_defalt.setVisibility(View.GONE);
                }


            }
        });

        //查看我参加的会议子项
        btn_participant_myChildMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_participant_myChildMeeting.getText().equals("我参加的子会议")) {
                    btn_participant_myChildMeeting.setText("收起");
                    ll_myChildMeeting.setVisibility(View.VISIBLE);//点击"我参加的子会议"后，listview出现
                    //查询服务器
                    map=new HashMap<String,String>();
                    map.put("participantTelNum","18510801374");
                    map.put("mainMeetingId",meeting.getMeetingId()+"");
                    String urlServlet="ParticipantGetMyChildMeetingServlet";
                    startThread(map,urlServlet,myChildMeetingThreadFlag,myChildMeetingResponseJsonStr,myChildMeetingMsgTag);
                }else{
                    btn_participant_myChildMeeting.setText("我参加的子会议");
                    ll_myChildMeeting.setVisibility(View.GONE);
                    lv_myChildMeeting.setVisibility(View.GONE);
                    tv_participant_myChildmeeting_defalt.setVisibility(View.GONE);
                }
            }
        });

        //查看会议资料：
        btn_participant_meetingDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParticipantPart_MeetingDetailActivity.this,FileListActivity.class);
                intent.putExtra("meetingId",id);
                startActivity(intent);
            }
        });

        //进入坐席解答区
        btn_participant_meetingTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParticipantPart_MeetingDetailActivity.this,AllPeopleActivity.class);
                intent.putExtra("meetingId",id);
                intent.putExtra("workName",participantBean.getParticipantName());
                startActivity(intent);
            }
        });

        // 查看评论
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipantPart_MeetingDetailActivity.this,PartCommentActivity.class);
                intent.putExtra("meetingId",id);
                startActivity(intent);
            }
        });
    }

    public void init(){
        tv_participant_meetingName=(TextView) findViewById(R.id.tv_participant_meetingName);
        tv_participant_meetingCompere=(TextView)findViewById(R.id.tv_participant_meetingCompere);
        tv_participant_meetingContent=(TextView)findViewById(R.id.tv_participant_meetingContent);
        tv_participant_meetingDate=(TextView)findViewById(R.id.tv_participant_meetingDate);
        tv_participant_meetingPlace=(TextView)findViewById(R.id.tv_participant_meetingPlace);
        tv_participant_meetingAgenda=(TextView)findViewById(R.id.tv_participant_meetingAgenda);
        tv_participant_meetingGuide=(TextView)findViewById(R.id.tv_participant_meetingGuide);
        tv_participant_meetingOverDate=(TextView)findViewById(R.id.tv_participant_meetingOverDate);
        btn_participant_childMeeting=(Button) findViewById(R.id.btn_participant_childMeeting);
        btn_participant_myChildMeeting=(Button)findViewById(R.id.btn_participant_myChildMeeting);
        btn_participant_meetingDocument=(Button)findViewById(R.id.btn_participant_meetingDocument);
        btn_participant_meetingTalk=(Button)findViewById(R.id.btn_participant_meetingTalk);
        btnGo=findViewById(R.id.btn_gocomment);
        btnComment=findViewById(R.id.btn_comment);
        meeting=new MeetingBean();

        ll_childMeeting=findViewById(R.id.ll_childMeeting);
        ll_myChildMeeting=findViewById(R.id.ll_myChildMeeting);
        ll_childMeeting.setVisibility(View.GONE);//初始化时，默认不存在，在点击查看后才出现
        ll_myChildMeeting.setVisibility(View.GONE);

        lv_childMeeting=findViewById(R.id.lv_childMeeting);
        lv_myChildMeeting=findViewById(R.id.lv_myChildMeeting);



        gson=new Gson();
        childMeetingList=new ArrayList<ChildMeetingBean>();
        tv_participant_childmeeting_defalt=(TextView) findViewById(R.id.tv_participant_childmeeting_defalt);
        tv_participant_myChildmeeting_defalt=findViewById(R.id.tv_participant_myChildmeeting_defalt);

        id = getIntent().getStringExtra("meetingId");
        phone=getIntent().getStringExtra("phone");
        System.out.println("参会方点击的会议Id:"+id);

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = msg.obj.toString();
            Gson gson=new Gson();
            participantBean=gson.fromJson(str,ParticipantBean.class);
            Log.e("**********",str);
            // 进行评论
            btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ParticipantPart_MeetingDetailActivity.this,OrderRatingActivity.class);
                    intent.putExtra("meetingId",id);
                    Log.e("**********",participantBean.getParticipantId()+"数据");
                    intent.putExtra("pid",participantBean.getParticipantId()+"");
                    startActivity(intent);
                }
            });
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                try {
                    message.obj= HttpUtils.httpURLConnectionPostData(HTTPPATH,
                            phone, "utf-8", "text/html");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    public void textViewSetText(TextView view, String text){
        view.setText(text);
    }

    public void startThread(Map map, String urlServlet, boolean threadFlag, String responseJsonStr, String msgTag){
        if(threadFlag){
            //证明线程已经开启，则数据已经取出了
            Message message=new Message();
            message.obj=responseJsonStr;
            if(msgTag=="childMeeting"){
                //点击了查看子会议
                childMeetingHandler.sendMessage(message);
            }else{
                //点击了查看“我的子会议”
                myChildMeetingHandler.sendMessage(message);
            }

        }else{
            threadFlag=true;//线程还未开启，则下面开启线程，并将threadFlag设置为true
            myThread =new MyThread(map,urlServlet,responseJsonStr,msgTag);
            myThread.start();
        }
    }

    class  MyThread extends Thread{
        Map<String,String> map=new HashMap<String,String>();//定义需要传值进来的变量
        String urlServlet;
        String responseJsonStr;
        String msgTag;
        public MyThread(Map map,String urlServlet,String responseJsonStr,String msgTag){
            this.map=map;
            this.urlServlet=urlServlet;
            this.responseJsonStr=responseJsonStr;
            this.msgTag=msgTag;
        }
        public void run() {
            //这里写入子线程需要做的工作
            System.out.println("进了thread 并且传进参数：");
            String url= Url.URLa+urlServlet;
            OkHttpUtils.post(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("错误来了：", "执行错误回调！！！ ", e);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    responseJsonStr= response.body().string();
                    System.out.println("获取到会议子项啊啊啊" + responseJsonStr);
                    System.out.println("获取到会议子项啊啊啊" + responseJsonStr);
                    System.out.println("获取到会议子项啊啊啊" + responseJsonStr);
                    Message message=new Message();
                    message.obj=responseJsonStr;
                    if(msgTag=="childMeeting"){
                        childMeetingHandler.sendMessage(message);
                    }else{
                        myChildMeetingHandler.sendMessage(message);
                    }
                }
            } ,map);
        }

    }
    Handler childMeetingHandler=new Handler(){
        public void handleMessage(Message msg) {
            String responseJsonStr=msg.obj.toString();
            if(responseJsonStr==null||responseJsonStr.equals("")||responseJsonStr.equals("[]")){
                //未查询到数据，则显示“无子会议”
                lv_childMeeting.setVisibility(View.GONE);
                tv_participant_childmeeting_defalt.setVisibility(View.VISIBLE);

            }else{
                lv_childMeeting.setVisibility(View.VISIBLE);
                tv_participant_childmeeting_defalt.setVisibility(View.GONE);
                childMeetingList= gson.fromJson(responseJsonStr, new TypeToken<List<ChildMeetingBean>>(){}.getType());
                System.out.println("shuchu 子会议list:"+childMeetingList.toString());
                // meetingAdapter = new MeetingAdapter(ParticipantPart_MeetingDetailActivity.this, childMeetingList);
                childMeetingAdapter=new ChildMeetingAdapter(ParticipantPart_MeetingDetailActivity.this,childMeetingList);
                lv_childMeeting.setAdapter(childMeetingAdapter);
            }

        }};
    Handler myChildMeetingHandler=new Handler(){
        public void handleMessage(Message msg) {
            String responseJsonStr=msg.obj.toString();
            if(responseJsonStr==null||responseJsonStr.equals("")||responseJsonStr.equals("[]")){
                //未查询到数据，则显示“无子会议”
                lv_myChildMeeting.setVisibility(View.GONE);
                tv_participant_myChildmeeting_defalt.setVisibility(View.VISIBLE);

            }else{
                lv_myChildMeeting.setVisibility(View.VISIBLE);
                tv_participant_myChildmeeting_defalt.setVisibility(View.GONE);
                System.out.println("获取到我参加的子会议啦：："+responseJsonStr);
                System.out.println("获取到我参加的子会议啦：："+responseJsonStr);
                childMeetingList= gson.fromJson(responseJsonStr, new TypeToken<List<ChildMeetingBean>>(){}.getType());
                System.out.println("shuchu 子会议list:"+childMeetingList.toString());
                // meetingAdapter = new MeetingAdapter(ParticipantPart_MeetingDetailActivity.this, childMeetingList);
                childMeetingAdapter=new ChildMeetingAdapter(ParticipantPart_MeetingDetailActivity.this,childMeetingList);
                lv_myChildMeeting.setAdapter(childMeetingAdapter);
            }
        }
    };

    //当界面关闭，则线程关闭
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myThread!=null&&myThread.isAlive()){
            myThread.interrupt();//关闭线程
            childMeetingThreadFlag=false;
            myChildMeetingThreadFlag=false;
        }
    }
}
