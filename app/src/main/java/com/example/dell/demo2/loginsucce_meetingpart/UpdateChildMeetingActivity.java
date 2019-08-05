package com.example.dell.demo2.loginsucce_meetingpart;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.create_meeting.utils.DateUtil;
import com.example.dell.demo2.loginsucce_participantpart.ChildMeetingBean;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdateChildMeetingActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etUpateChildName,etUpdateChildContent,etUpdateChildPlace,etUpdateChildCompere;
    private TextView tvUpdateChildTime;
    private Button btnUpdateChildCancel,btnUpdateSure,btnUpdateDate,btnUpdateTime;
    private ChildMeetingBean childMeetingBean;
    private int childMeetingBeanId;
    private static String eventStartTime,eventOverTime;
    private Calendar calendar = Calendar.getInstance();
    private Handler handler;
    private Boolean tag = false;
    Gson gson = new Gson();
    Map<String, String> mapValue = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_child_meeting);
        initView();
    }

    private void initView() {
        etUpateChildName = findViewById(R.id.et_updatechildMeetName);
        etUpdateChildContent = findViewById(R.id.et_updateChildMeetContent);
        etUpdateChildPlace = findViewById(R.id.et_updateChildMeetPlace);
        etUpdateChildCompere = findViewById(R.id.et_updatechildMeetGuest);

        tvUpdateChildTime = findViewById(R.id.tv_updateChildMeetStart);
        btnUpdateChildCancel = findViewById(R.id.btn_updatechildcancel);
        btnUpdateSure = findViewById(R.id.btn_updatechildnext);
        btnUpdateDate = findViewById(R.id.btn_updateChildStartDate);
        btnUpdateTime = findViewById(R.id.btn_updateChildStartTime);


        btnUpdateChildCancel.setOnClickListener(this);
        btnUpdateSure.setOnClickListener(this);
        btnUpdateDate.setOnClickListener(this);
        btnUpdateTime.setOnClickListener(this);





        childMeetingBean = (ChildMeetingBean) getIntent().getSerializableExtra("childMeetingBean");
        System.out.println("看字会议对象有没有传过来："+childMeetingBean.toString());
        childMeetingBeanId = getIntent().getIntExtra("childMeetingBeanId",childMeetingBeanId);
        //mapValue = (Map<String, String>) getIntent().getExtras().getSerializable("mapValue");
        //newChildMeetingSet = (HashSet<ChildMeetingBean>) getIntent().getExtras().getSerializable("newChildMeetingSet");

        eventStartTime = getIntent().getStringExtra("eventStartTime");
        eventOverTime = getIntent().getStringExtra("eventOverTime");

        etUpateChildName.setText(childMeetingBean.getChildMeetingName());
        etUpdateChildContent.setText(childMeetingBean.getChildMeetingContent());
        etUpdateChildPlace.setText(childMeetingBean.getChildMeetingPlace());
        etUpdateChildCompere.setText(childMeetingBean.getChildMeetingCompere());
        tvUpdateChildTime.setText(childMeetingBean.getChildMeetingTime());


        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String data = msg.obj.toString();
                if (data.equals("SUCCESS")) {
                   Intent i = new Intent(UpdateChildMeetingActivity.this,MeetingPart_MyMeetingActivity.class);
                   startActivity(i);

                    Toast.makeText(UpdateChildMeetingActivity.this, "更新子会议成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateChildMeetingActivity.this, "更新子会议失败！", Toast.LENGTH_SHORT).show();
                }

            }
        };


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_updatechildcancel:
                this.finish();
                break;
            case R.id.btn_updatechildnext:
                String newChildName = etUpateChildName.getText().toString();
                String newChildContent = etUpdateChildContent.getText().toString();
                String newChildPlace = etUpdateChildPlace.getText().toString();
                String newChildCompere = etUpdateChildCompere.getText().toString();
                String newChidTime = tvUpdateChildTime.getText().toString();
                Boolean isChildPlace = newChildPlace.equals(childMeetingBean.getChildMeetingPlace());
                Boolean isChidTime = newChidTime.equals(childMeetingBean.getChildMeetingTime());
                if(!isChildPlace){
                    System.out.println("子会议地点变了");
                    //发短信
                }
                if(!isChidTime){
                    System.out.println("子会议时间变了");
                    //发短信
                }

                ChildMeetingBean childMeetingBean = new ChildMeetingBean(childMeetingBeanId,newChildName,newChidTime,newChildPlace,newChildContent,newChildCompere);
                String childMeetingBeanStr = gson.toJson(childMeetingBean);
                mapValue.put("childMeetingBeanStr", childMeetingBeanStr);

                UpdateThread updateThread = new UpdateThread(mapValue, "UpdateMeetServlet");
                updateThread.start();



                break;

            case R.id.btn_updateChildStartDate:
                showDatePickerDialog(UpdateChildMeetingActivity.this, 5, tvUpdateChildTime, calendar);
                break;
            case R.id.btn_updateChildStartTime:
                showTimePickerDialog(UpdateChildMeetingActivity.this, 5, tvUpdateChildTime, calendar);
                break;

        }


    }


    //Android 原生态日期选择
    public static void showDatePickerDialog(final Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                String day = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                DateUtil dateUtil = new DateUtil();
                Boolean a = dateUtil.compareTwoTime(eventStartTime,day);
                Boolean b = dateUtil.compareTwoTime(day,eventOverTime);
                if(a&&b){
                    tv.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                }else{
                    Toast.makeText(activity, "子会议时间必须晚于主会议开始时间且早于主会议结束时间！", Toast.LENGTH_SHORT).show();
                }

            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Android原生态 时间选择
     *
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showTimePickerDialog(final Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // Activity是context的子类
        new mtimePicket(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String day = tv.getText().toString();
                        if (day.equals("")) {
                            Toast.makeText(activity, "请先选择日期", Toast.LENGTH_SHORT).show();
                        } else {
                            //tv.append("-" + hourOfDay + "时" + minute + "分");
                            System.out.println("-" + hourOfDay + "时" + minute + "分");
                            String time = "-" + hourOfDay + "时" + minute + "分";
                            tv.setText(day + time);
                        }
                    }

                }

                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();

    }


    //为了解决TimePickerDialog回调两次，重写源码
    static class mtimePicket extends TimePickerDialog {
        @Override
        protected void onStop() {
//          注释这里，onstop就不会调用回调方法
//          super.onStop();
        }

        /* public mtimePicket(Context context, OnTimeSetListener callBack,
                            int hourOfDay, int minute, boolean is24HourView) {
             super(context, callBack, hourOfDay, minute, is24HourView);
             // TODO Auto-generated constructor stub
         }*/
        public mtimePicket(Context context, int theme,
                           OnTimeSetListener callBack, int hourOfDay, int minute,
                           boolean is24HourView) {
            super(context, theme, callBack, hourOfDay, minute, is24HourView);
            // TODO Auto-generated constructor stub
        }
    }

    public class UpdateThread extends Thread {
        private String servlet;
        private Map<String, String> map;

        public UpdateThread(Map<String, String> map, String servlet) {
            this.map = map;
            this.servlet = servlet;
        }

        public void run() {
            String url = Url.URLa + servlet;
            com.example.dell.demo2.create_meeting.connect.OkHttpUtil.post(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("错误来了：", "执行错误回调！！！ ", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseJsonStr = response.body().string();
                    System.out.println("服务器端返回的信息" + responseJsonStr);
                    Message message = handler.obtainMessage();
                    // 设置消息默认值
                    message.what = 123;
                    message.obj = responseJsonStr;
                    handler.sendMessage(message);
                }
            }, map);

        }

    }


    }

