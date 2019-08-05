package com.example.dell.demo2.worker;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.QRcode.QRcodeActivity;
import com.example.dell.demo2.R;
import com.example.dell.demo2.billview.NoticeView;
import com.example.dell.demo2.listview.AllPeopleActivity;
import com.example.dell.demo2.listview.WorkerSeeActivity;
import com.example.dell.demo2.listview.WorkerSendActivity;
import com.example.dell.demo2.listview.WorkingActivity;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingPart_MainActivity;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingPart_MyMeetingActivity;
import com.example.dell.demo2.pollingservice.PollingService;
import com.example.dell.demo2.pollingservice.PollingUtils;
import com.example.dell.demo2.loadutils.OkHttpUtil;
import com.example.dell.demo2.supervisor.OkHttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Callback;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class WorkerActivity extends AppCompatActivity implements OnClickListener{
    private NoticeView notice_view;
    private MyReceier myReceier = null;
    private List<String> list;
    private String meetingId="1";
    private MeetingBean meeting;
    private Button btn_work_talking,btn_all_talking,btn_zxing,btn_scheHotel,btn_schePlace,btn_scheCar;
    private String id,workName;
    private Button btn5,btn6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        init();

        //注册广播接收器
        myReceier = new MyReceier();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dell.demo2.pollingservice.PollingService");
        WorkerActivity.this.registerReceiver(myReceier, intentFilter);
        //开启服务 查询服务器数据

         PollingUtils.startPollingService(this, 20, PollingService.class, PollingService.ACTION,"1");


        // 这里可以设置一个动画集合，如果不想要动画可以设置成null
        // 不过这里设置动画我设计的不太友好，需要的直接改源码可能更快捷。
//        notice_view.setEnterAnimation(null);
//        notice_view.setExitAnimation(null);

        // 默认动画效果就是渐变和位移，可以通过这个设置动画的时长，默认是1000
//        notice_view.setAnimationDuration(1000);

        // 公告切换一次是3秒，可以通过这个方法设置,设置的比动画的长就好。默认是3000
//        notice_view.setNoticeDuration(2000);

        // 这里就是监听点击事件，TextView是点中的那个公告，position是位置。
        // 比如点击之后想该条公告边灰色，就可以view.setTextColor();实现了
       // notice_view.setVisibility(View.VISIBLE);
        notice_view.setOnItemClickListener(new NoticeView.OnItemClickListener() {
            @Override
            public void onItemClick(TextView view, int position) {
                String s = list.get(position);
                view.setTextColor(Color.GRAY);//点击公告，字体变成灰色
                myDialog();
                Toast.makeText(WorkerActivity.this, s, LENGTH_SHORT).show();
            }
        });
    }

    public void init() {
        System.out.println("进到工作人员界面！！！！");
        System.out.println("进到工作人员界面！！！！");
        System.out.println("进到工作人员界面！！！！");
        meeting=new MeetingBean();
        //获取传递过来的会议信息。
        Intent i=getIntent();
        meeting= (MeetingBean) i.getSerializableExtra("meeting");
        workName=i.getStringExtra( "workName" );
        notice_view = (NoticeView) findViewById(R.id.notice_view);
        // 首先，模拟一个公告的集合。需要字符串泛型的list
        list = new ArrayList<String>();
        id=getIntent().getStringExtra("meetingId");
        btn_work_talking=findViewById(R.id.button3);//工作人员讨论区
        btn_all_talking=findViewById(R.id.button4);//坐席解答区
        btn_zxing=findViewById(R.id.btn_zxing);//扫码签到
        btn_scheHotel=findViewById(R.id.btn_scheHotel);//预订酒店
        btn_scheCar=findViewById(R.id.btn_scheCar);//预订车辆
        btn_schePlace=findViewById(R.id.btn_schePlace);//预订场地
        btn5=findViewById( R.id.button5 );
        btn6=findViewById( R.id.button6 );
        btn_all_talking.setOnClickListener(this);
        btn_work_talking.setOnClickListener(this);
        btn_zxing.setOnClickListener(this);
        btn_scheHotel.setOnClickListener(this);
        btn_scheCar.setOnClickListener(this);
        btn_schePlace.setOnClickListener(this);
        btn5.setOnClickListener( this );
        btn6.setOnClickListener( this );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button3://工作人员讨论区
                Intent i=new Intent(WorkerActivity.this, WorkingActivity.class);
                i.putExtra("meetingId",id);
                i.putExtra( "workName",workName);
                startActivity(i);
                break;
            case R.id.button4://坐席解答区
                Intent i1=new Intent(WorkerActivity.this, AllPeopleActivity.class);
                i1.putExtra("meetingId",id);
                i1.putExtra( "workName",workName);
                startActivity(i1);
                break;
            case R.id.btn_zxing:
                Toast.makeText(this ,"二维码界面被点击了",Toast.LENGTH_SHORT).show();
                Intent i2 = new Intent(WorkerActivity.this, QRcodeActivity.class);
                startActivity(i2);
                break;
            case R.id.btn_scheHotel://预订酒店
            {
                String url= Url.URLa+"GetHotelListServlet";
                final String[] responseJsonStr = {""};
                OkHttpUtil.doGet(url, new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Log.e("执行了错误回调", e+"");
                        Log.e("执行了错误回调", e+"");
                        Log.e("执行了错误回调", e+"");
                    }
                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        responseJsonStr[0] = response.body().string();
                        System.out.println("成功获取数据！！！！"+ responseJsonStr[0].toString());
                        System.out.println("成功获取数据！！！！"+ responseJsonStr[0].toString());
                        System.out.println("成功获取数据！！！！"+ responseJsonStr[0].toString());
                        Intent i3=new Intent(WorkerActivity.this,HotelListActivity.class);
                        i3.putExtra("responseJsonStr", responseJsonStr[0]);
                        startActivity(i3);
                    }
                });
                Toast.makeText(this,"预订酒店点击了！！",Toast.LENGTH_SHORT).show();
                }
                 break ;
            case R.id.btn_schePlace://预订场地
                break;
            case R.id.btn_scheCar://预订车辆
                break;
            // 审批情况
            case R.id.button5:
                Intent i3=new Intent(WorkerActivity.this, WorkerSeeActivity.class);
                i3.putExtra("meetingId",id);
                startActivity(i3);
                break;
            // 发布消息
            case R.id.button6:
                Intent i4=new Intent(WorkerActivity.this, WorkerSendActivity.class);
                i4.putExtra("meetingId",id);
                startActivity(i4);
                break;

        }

    }

    public class MyReceier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonStr = intent.getStringExtra("msg");
            System.out.println("获取到预警信息想嘻嘻嘻想想信息：：：------>>>"+jsonStr);
            System.out.println("获取到预警信息想嘻嘻嘻想想信息：：：------>>>"+jsonStr);
            System.out.println("获取到预警信息想嘻嘻嘻想想信息：：：------>>>"+jsonStr);
            Gson gson=new Gson();
           list= gson.fromJson(jsonStr, new TypeToken<List<String>>(){}.getType());
            if(list==null){
                notice_view.setVisibility(View.GONE);
            }else {
                notice_view.setVisibility(View.VISIBLE);
                notice_view.setNoticeList(list);
            }
            //将list数据设置进去

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Stop polling service...");
        PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
        unregisterReceiver(myReceier); //注销广播
    }

    //点击公告条，选择处理事件
    public void myDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(WorkerActivity.this);
        Field mAlert = null;



        dialog.setTitle("△警告信息(点击处理)！");
        dialog.setCancelable(false);

        //创建数据源 数据源就是公告栏的list
        //创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        //设置对话框为适配器对话框
        dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {

            /**
             * 第一个参数：此Dialog
             * 第二个参数：条目的位置，从0开始
             * 每个条目被点击都会触发改回调函数
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Pattern p = Pattern.compile(".*(场地).*");//正则表达式，用来判断是哪一类的预警信息
                Matcher m = p.matcher(list.get(which));
                System.out.println(m.matches() + "-");
                Toast.makeText(WorkerActivity.this, m.matches()+"", Toast.LENGTH_LONG).show();
                Map map=new HashMap<String,String>();
                if(m.matches()){
                    map.put("meetingId",meetingId);
                   /* Intent intent=new Intent(WorkerActivity.this,ScheduleHotelDetailActivity.class);
                    intent.putExtra("meetingId",meetingId);*/
                    String url= Url.URLa+"GetScheduleHotelDetailServlet";
                    final String[] responseJsonStr = {""};
                    OkHttpUtils.post(url, new Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                            Log.e("执行了错误回调", e+"");
                            Log.e("执行了错误回调", e+"");
                            Log.e("执行了错误回调", e+"");
                        }
                        @Override
                        public void onResponse(okhttp3.Call call, Response response) throws IOException {
                            responseJsonStr[0] = response.body().string();
                            System.out.println("成功获取数据！！！！"+ responseJsonStr[0].toString());
                            System.out.println("成功获取数据！！！！"+ responseJsonStr[0].toString());
                            System.out.println("成功获取数据！！！！"+ responseJsonStr[0].toString());
                            Intent i3=new Intent(WorkerActivity.this,ScheduleHotelDetailActivity.class);
                            i3.putExtra("responseJsonStr", responseJsonStr[0]);
                            startActivity(i3);
                        }
                    },map);

                }


            }
        });
        dialog.show();
    }


}
