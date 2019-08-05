package com.example.dell.demo2.create_meeting.meeting_manage_activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.create_meeting.utils.DateUtil;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.selectfileutils.FileUtils;

import java.util.Calendar;

public class MeetingAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEventName, etEventPlace, etMeetingCost, etMeetingSpecial, etEventContent, etEventPeople;
    private Button btnEventTime, btnOvertime, btnCancel, btnNext, btnEventAgenda, btnEventGuide;
    private TextView tvEventTime, tvOverTime;
    private int mYear, mMonth, mDay;
    private MeetingBean meeting;
    private Integer workerId;
    private DateUtil dateUtil = new DateUtil();
    private static int DOCUMENTSUI_REQUEST_CODE = 456;
    private static int MY_PERMISSIONS_REQUEST_CODE = 123;
    String path,agendaPath,guidePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_add);
        workerId = getIntent().getIntExtra("workId", 0);
        initView();
    }

    private void initView() {
        //this.setTitle("创建会议");
        path = agendaPath = guidePath = "";
        etEventName = findViewById(R.id.et_eventName);
        etEventContent = findViewById(R.id.etMeetingCost);
        etEventPlace = findViewById(R.id.et_eventPlace);
        etEventPeople = findViewById(R.id.et_eventPeople);
        etMeetingCost = findViewById(R.id.et_meetingcost);
        etMeetingSpecial = findViewById(R.id.et_meetingSpecial);
        tvEventTime = findViewById(R.id.tv_StartTime);
        tvOverTime = findViewById(R.id.tv_overTime);
        btnEventGuide = findViewById(R.id.btn_eventGuide);
        btnEventAgenda = findViewById(R.id.btn_eventAgenda);
        btnEventTime = findViewById(R.id.btn_ChooseStart);
        btnOvertime = findViewById(R.id.btn_overTime);
        btnCancel = findViewById(R.id.btn_cancel);
        btnNext = findViewById(R.id.btn_next);

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        btnNext.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnEventTime.setOnClickListener(this);
        btnOvertime.setOnClickListener(this);
        btnEventGuide.setOnClickListener(this);
        btnEventAgenda.setOnClickListener(this);

         /* //Android 6.0系统读写文件出现FileNotFoundException:EACCES (permission denied)解决办法:
        *//**
         * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
         */

        if (Build.VERSION.SDK_INT >= 23) {
            //,Manifest.permission.READ_EXTERNAL_STORAGE
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, MY_PERMISSIONS_REQUEST_CODE);
                    return;
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                this.finish();
                break;
            case R.id.btn_next:
                if (CheckData()) {
                    String meetingname = etEventName.getText().toString();
                    String meetingdate = tvEventTime.getText().toString();
                    String meetingplace = etEventPlace.getText().toString();
                    String meetingcontent = etEventContent.getText().toString();
                    String meetingpeople = etEventPeople.getText().toString();
                    String meetingspecial = etMeetingSpecial.getText().toString();
                    String meetingover = tvOverTime.getText().toString();
                    String cost = etMeetingCost.getText().toString();
                    Float meetingcost = Float.parseFloat(cost);
                    meeting = new MeetingBean(meetingname, meetingdate, meetingplace, meetingcontent, meetingpeople, meetingspecial, meetingover, meetingcost);
                    System.out.println("MeetingAddActivity页面："+meeting.toString());
                    showDialog(MeetingAddActivity.this);
                }
                break;
            case R.id.btn_ChooseStart:
                new DatePickerDialog(MeetingAddActivity.this, onDateSetListener1, mYear, mMonth, mDay).show();
                break;
            case R.id.btn_overTime:
                new DatePickerDialog(MeetingAddActivity.this, onDateSetListener2, mYear, mMonth, mDay).show();
                break;
            //会议议程：
            case R.id.btn_eventAgenda:
                showChooser();
                agendaPath = path;
                break;
            //参会指南
            case R.id.btn_eventGuide:
                showChooser();
                guidePath = path;
                break;


        }
    }


    private DatePickerDialog.OnDateSetListener onDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            }
            boolean isearly = dateUtil.compareNowTime(days);
            System.out.println("看选择的时间是否早于系统时间：" + isearly);
            if (isearly) {
                tvEventTime.setText(days);
            } else {
                Toast.makeText(MeetingAddActivity.this, "您当前选择的时间早于系统时间，请重新选择！", Toast.LENGTH_SHORT).show();
            }
        }
    };
    public DatePickerDialog.OnDateSetListener onDateSetListener2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            }
            String startTime = tvEventTime.getText().toString();
            if (!startTime.equals("")) {
                Boolean iszao = dateUtil.compareTwoTime(startTime, days);
                System.out.println("看选择的时间是否早于会议开始时间：" + iszao);
                if (iszao) {
                    tvOverTime.setText(days);
                } else {
                    Toast.makeText(MeetingAddActivity.this, "会议截止时间不能早于会议开始时间！请重新选择！", Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean isearly = dateUtil.compareNowTime(days);

                if (isearly) {
                    Toast.makeText(MeetingAddActivity.this, "您当前选择的时间早于系统时间，请重新选择！", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };


    public void showDialog(Activity activity) {
        //AlertDialog的创建用到AlertDialog.Builder，AlertDialog.Builder构造函数中的Context必须传Activity的实例(先记着)
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //设置对话框标题，该标题会显示在标题区域中
        builder.setTitle("创建子会议")
                //设置对话框图标，该标题会显示在标题区域中
                .setIcon(null)
                //setMessage方法中的内容会显示在内容区域中
                .setMessage("您是否需要添加子会议？")
                /*以下三个setXXXButton(CharSequence text, final OnClickListener listener)方法
                   都向对话框的按钮区域添加了一个按钮，方法的第一个参数是按钮文本，第二个是按钮点击监听器。
                   注意按钮的顺序和代码的添加顺序无关，只有调用了对应的setXXXButton()
                   方法该按钮才显示。
                */
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MeetingAddActivity.this,
                                AddChildMeetingActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("workerId", workerId);
                        bundle.putSerializable("meeting", meeting);
                        bundle.putString("agendaPath",agendaPath);
                        bundle.putString("guidePath",guidePath);
                        bundle.putString("startTime", tvEventTime.getText().toString());
                        bundle.putString("overTime", tvOverTime.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MeetingAddActivity.this,
                                RolesAssignmentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("workId", workerId);
                        bundle.putSerializable("meeting", meeting);
                        bundle.putString("agendaPath",agendaPath);
                        bundle.putString("guidePath",guidePath);
                        intent.putExtras(bundle);
                        startActivity(intent);
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


    //上传文件
    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, DOCUMENTSUI_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 456:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        //Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                           path = FileUtils.getPath(this, uri);
                            //postFile(path);
                            Toast.makeText(MeetingAddActivity.this,
                                    "File Selected: " + path, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("FileSelectorTestActivity", "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Boolean CheckData() {

        String name = etEventName.getText().toString();
        String start = tvEventTime.getText().toString();
        String over = tvOverTime.getText().toString();
        String place = etEventPlace.getText().toString();
        String content = etEventContent.getText().toString();
        String people = etEventPeople.getText().toString();
        if (name.equals("")) {
            Toast.makeText(MeetingAddActivity.this, "请填写会议名称！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (start.equals("")) {
            Toast.makeText(MeetingAddActivity.this, "请选择会议开始时间！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (over.equals("")) {
            Toast.makeText(MeetingAddActivity.this, "请选择会议结束时间！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (place.equals("")) {
            Toast.makeText(MeetingAddActivity.this, "请填写会议地点！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (content.equals("")) {
            Toast.makeText(MeetingAddActivity.this, "请填写会议内容！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(people.equals("")){
            Toast.makeText(MeetingAddActivity.this, "请填写邀请对象！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(agendaPath.equals("")){
            Toast.makeText(MeetingAddActivity.this, "您未上传会议议程！", Toast.LENGTH_SHORT).show();
            //return false;
        }
        if(guidePath.equals("")){
            Toast.makeText(this, "您未上传会议指南！", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
