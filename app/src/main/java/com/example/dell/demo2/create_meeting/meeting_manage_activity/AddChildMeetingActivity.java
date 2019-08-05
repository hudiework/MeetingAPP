package com.example.dell.demo2.create_meeting.meeting_manage_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.create_meeting.utils.DateUtil;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.loginsucce_participantpart.ChildMeetingBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddChildMeetingActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etChildMeetName, etChildMeetContent, etChildMeetPlace, etChildMeetCompere;
    private Button btnChildStartDate, btnChildCancel, btnChildNext, btnChildStartTime;
    private TextView tvChildMeetTime;
    private ChildMeetingBean childMeetingBean;
    private List<ChildMeetingBean> childMeetingList = new ArrayList<ChildMeetingBean>();
    private Bundle bundle = new Bundle();
    private Calendar calendar = Calendar.getInstance();
    private static String eventStartTime,eventOverTime;
    private int workerId;
    private MeetingBean meeting;
    private  String agendaPath,guidePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_meeting);
        initView();
    }
    public void initView() {
        etChildMeetName = findViewById(R.id.et_childMeetName);
        etChildMeetContent = findViewById(R.id.et_ChildMeetContent);
        etChildMeetPlace = findViewById(R.id.et_ChildMeetPlace);
        etChildMeetCompere = findViewById(R.id.et_childMeetGuest);
        tvChildMeetTime = findViewById(R.id.tv_ChildMeetStart);

        btnChildStartDate = findViewById(R.id.btn_ChildStartDate);
        btnChildStartTime = findViewById(R.id.btn_ChildStartTime);
        btnChildCancel = findViewById(R.id.btn_childcancel);
        btnChildNext = findViewById(R.id.btn_childnext);

        btnChildNext.setOnClickListener(this);
        btnChildStartDate.setOnClickListener(this);
        btnChildStartTime.setOnClickListener(this);
        btnChildCancel.setOnClickListener(this);

        bundle = getIntent().getExtras();
        eventStartTime = bundle.getString("startTime");
        eventOverTime = bundle.getString("overTime");
        workerId = bundle.getInt("workerId");
        meeting = (MeetingBean) bundle.getSerializable("meeting");
        System.out.println("AddChildActivity页面："+meeting.toString());
        agendaPath = getIntent().getStringExtra("agendaPath");
        guidePath = getIntent().getStringExtra("guidePath");



    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ChildStartDate:
//                https://blog.csdn.net/qq_33756493/article/details/78120743
                showDatePickerDialog(AddChildMeetingActivity.this, 5, tvChildMeetTime, calendar);
                break;

            case R.id.btn_ChildStartTime:
                showTimePickerDialog(AddChildMeetingActivity.this, 5, tvChildMeetTime, calendar);
                break;
            case R.id.btn_childnext:
                if(CheckData()){
                    showDialog(AddChildMeetingActivity.this);
                }
                break;
            case R.id.btn_childcancel:
                finish();
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


    public void showDialog(Activity activity){
        //AlertDialog的创建用到AlertDialog.Builder，AlertDialog.Builder构造函数中的Context必须传Activity的实例(先记着)
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //设置对话框标题，该标题会显示在标题区域中
        builder.setTitle("是否继续添加子会议")
                //设置对话框图标，该标题会显示在标题区域中
                .setIcon(null)
                //setMessage方法中的内容会显示在内容区域中
                .setMessage("您是否需要继续添加子会议？")
                /*以下三个setXXXButton(CharSequence text, final OnClickListener listener)方法
                   都向对话框的按钮区域添加了一个按钮，方法的第一个参数是按钮文本，第二个是按钮点击监听器。
                   注意按钮的顺序和代码的添加顺序无关，只有调用了对应的setXXXButton()
                   方法该按钮才显示。
                */
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        childMeetingBean = new ChildMeetingBean(etChildMeetName.getText().toString(), tvChildMeetTime.getText().toString(), etChildMeetPlace.getText().toString(), etChildMeetContent.getText().toString(), etChildMeetCompere.getText().toString());
                        childMeetingList.add(childMeetingBean);
                        ClearData();
                    }
                })
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        childMeetingBean = new ChildMeetingBean(etChildMeetName.getText().toString(), tvChildMeetTime.getText().toString(), etChildMeetPlace.getText().toString(), etChildMeetContent.getText().toString(), etChildMeetCompere.getText().toString());
                        childMeetingList.add(childMeetingBean);
                        Intent i = new Intent(AddChildMeetingActivity.this,RolesAssignmentActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("childMeetingList", (Serializable) childMeetingList);
                        b.putInt("workerId",workerId);
                        b.putSerializable("meeting",meeting);
                        b.putString("agendaPath",agendaPath);
                        b.putString("guidePath",guidePath);
                        i.putExtras(b);
                        startActivity(i);

                    }
                })/*.setNeutralButton("button3", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        })*/
                //真正实例化AlertDialog对象
                .create()
                //显示对话框
                .show();
    }

    public void ClearData(){

        etChildMeetName.setText("");
        etChildMeetContent.setText("");
        etChildMeetPlace.setText("");
        etChildMeetCompere.setText("");
        tvChildMeetTime.setText("");

    }

    public Boolean CheckData(){

        String name = etChildMeetName.getText().toString();
        String start = tvChildMeetTime.getText().toString();
        String place = etChildMeetPlace.getText().toString();
        String content = etChildMeetContent.getText().toString();
        String compere = etChildMeetCompere.getText().toString();
        if(name.equals("")){
            Toast.makeText(AddChildMeetingActivity.this, "请填写会议名称！", Toast.LENGTH_SHORT).show();
            return  false;
        }
        if(start.equals("")){
            Toast.makeText(AddChildMeetingActivity.this, "请选择会议开始时间！", Toast.LENGTH_SHORT).show();
            return  false;
        }

        if(place.equals("")){
            Toast.makeText(AddChildMeetingActivity.this, "请填写会议地点！", Toast.LENGTH_SHORT).show();
            return  false;
        }
        if(content.equals("")){
            Toast.makeText(AddChildMeetingActivity.this, "请填写会议内容！", Toast.LENGTH_SHORT).show();
            return  false;
        }

        if(compere.equals("")){
            Toast.makeText(AddChildMeetingActivity.this, "请填写会议主持人！", Toast.LENGTH_SHORT).show();
            return  false;
        }

        return true;
    }


}
