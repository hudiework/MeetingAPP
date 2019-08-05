package com.example.dell.demo2.loginsucce_meetingpart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.create_meeting.utils.DateUtil;
import com.example.dell.demo2.loginsucce_participantpart.ChildMeetingBean;
import com.example.dell.demo2.selectfileutils.FileUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText updateTitle, updateContent, updatePlace, updatePeople, updateSpecial, updateCost;
    private TextView updateStartTime, updateOverTime;
    private Button btnUpdateStart, btnUpdateOver, btnUpdateNext, btnUpdateChild, btnUpdateCancel, btnUpdateAgenda, btnUpdateGuide;
    private int meetingId;
    private int mYear, mMonth, mDay;
    private DateUtil dateUtil = new DateUtil();
    private static int DOCUMENTSUI_REQUEST_CODE = 456;
    private static int MY_PERMISSIONS_REQUEST_CODE = 123;
    public Handler handler;
    private QuerryChildThread querryChildThread;
    String path, agendaPath, guidePath;
    Gson gson = new Gson();
    List<ChildMeetingBean> childMeetingList = new ArrayList<ChildMeetingBean>();
    Map<String, String> mapValue = new HashMap<>();
    private MeetingBean meeting;
    private HashSet<ChildMeetingBean> newChildMeetingSet = new HashSet<ChildMeetingBean>();
    ChildMeetingBean childMeetingBean;
    private String name, content, startDate, overDate, place, people, special, cost;

    // 记录需要群发的号码列表
    private ArrayList<String> sendList = new ArrayList<String>();
    // 声明短信管理器SmsManager对象
    private SmsManager sManage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
    }

    /**
     * 初始化群发名单的集合
     */
    private void initSendList() {
        sendList.add("18361082640");
        sendList.add("18510801374");

    }

    private void initView() {
        updateTitle = findViewById(R.id.et_updateName);
        updateContent = findViewById(R.id.et_updateContent);
        updateStartTime = findViewById(R.id.tv_updateStartTime);
        updateOverTime = findViewById(R.id.tv_updateoverTime);
        updatePlace = findViewById(R.id.et_updatePlace);
        updatePeople = findViewById(R.id.et_updatePeople);
        updateSpecial = findViewById(R.id.et_updateSpecial);
        updateCost = findViewById(R.id.et_updatecost);

        meeting = (MeetingBean) getIntent().getExtras().getSerializable("meeting");
        meetingId = getIntent().getExtras().getInt("meetingId");
        //newChildMeetingSet = (HashSet<ChildMeetingBean>) getIntent().getExtras().getSerializable("newChildMeetingSet");

        System.out.println("修改页收到的会议id" + meetingId);
        name = meeting.getMeetingName();
        content = meeting.getMeetingContent();
        startDate = meeting.getMeetingDate();
        overDate = meeting.getMeetingOverDate();
        place = meeting.getMeetingPlace();
        people = meeting.getMeetingPeople();
        special = meeting.getMeetingSpecial();
        cost = meeting.getMeetingCost() + "";
        updateTitle.setText(name);
        updateContent.setText(content);
        updateStartTime.setText(startDate);
        updateOverTime.setText(overDate);
        updatePlace.setText(place);
        updatePeople.setText(people);
        updateSpecial.setText(special);
        updateCost.setText(cost);

        btnUpdateChild = findViewById(R.id.btn_updateChild);
        btnUpdateStart = findViewById(R.id.btn_updateChooseStart);
        btnUpdateOver = findViewById(R.id.btn_updateoverTime);
        btnUpdateCancel = findViewById(R.id.btn_updatecancel);
        btnUpdateNext = findViewById(R.id.btn_updatenext);
        btnUpdateAgenda = findViewById(R.id.btn_updateAgenda);
        btnUpdateGuide = findViewById(R.id.btn_updateGuide);

        btnUpdateChild.setOnClickListener(this);
        btnUpdateStart.setOnClickListener(this);
        btnUpdateOver.setOnClickListener(this);
        btnUpdateCancel.setOnClickListener(this);
        btnUpdateNext.setOnClickListener(this);
        btnUpdateAgenda.setOnClickListener(this);
        btnUpdateGuide.setOnClickListener(this);

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        // 获取系统SmsManager服务
        sManage = SmsManager.getDefault();
        initSendList();



        //Android 6.0系统读写文件出现FileNotFoundException:EACCES (permission denied)解决办法:

       /*  动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取*/


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

        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 121) {
                    String data = msg.obj.toString();
                    if (data != "") {
                        childMeetingList = gson.fromJson(data, new TypeToken<List<ChildMeetingBean>>() {
                        }.getType());
                    }
                    Intent intent = new Intent(UpdateActivity.this, UpdateChildActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("meetingId", meetingId);
                    b.putSerializable("childMeetingList", (Serializable) childMeetingList);
                    b.putString("eventStartTime", updateStartTime.getText().toString());
                    b.putString("eventOverTime", updateOverTime.getText().toString());
                    intent.putExtras(b);
                    startActivity(intent);
                } else if (msg.what == 123) {
                    String data = msg.obj.toString();
                    if (data.equals("SUCCESS")) {
                        Toast.makeText(UpdateActivity.this, "更新会议成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateActivity.this, "更新会议失败！", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };







    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_updateChooseStart:
                new DatePickerDialog(UpdateActivity.this, onDateSetListener1, mYear, mMonth, mDay).show();
                break;
            case R.id.btn_updateoverTime:
                new DatePickerDialog(UpdateActivity.this, onDateSetListener2, mYear, mMonth, mDay).show();
                break;
            case R.id.btn_updateAgenda:
                showChooser();
                agendaPath = path;
                break;
            case R.id.btn_updateGuide:
                showChooser();
                guidePath = path;
                break;
            case R.id.btn_updateChild:
                Toast.makeText(this, "点击了修改子会议按钮", Toast.LENGTH_SHORT).show();
                querryChildThread = new QuerryChildThread(meetingId + "", "ShowChildMeetServlet",handler);
                querryChildThread.start();
                break;
            case R.id.btn_updatenext:

                String newTitle = updateTitle.getText().toString();
                String newContent = updateContent.getText().toString();
                String newStartTime = updateStartTime.getText().toString();
                String newOverTime = updateOverTime.getText().toString();
                String newPlace = updatePlace.getText().toString();
                String newPeople = updatePeople.getText().toString();
                String newSpecial = updateSpecial.getText().toString();
                String cost = updateCost.getText().toString();
                Float newCost = Float.parseFloat(cost);
                Boolean isMeetName = newStartTime.equals(meeting.getMeetingName());
                Boolean isStartTime = newStartTime.equals(meeting.getMeetingDate());
                Boolean isPlace = newPlace.equals(meeting.getMeetingPlace());
                Boolean isSpecial = newSpecial.equals(meeting.getMeetingSpecial());
                Boolean isCost = newCost.equals(meeting.getMeetingCost());

                MeetingBean newMeet = new MeetingBean(newTitle, newStartTime, newPlace, newContent, newPeople, newSpecial, newOverTime, newCost);
                mapValue.put("meetingId", meetingId + "");
                String newMeetStr = gson.toJson(newMeet);
                mapValue.put("newMeet", newMeetStr);


                childMeetingBean = (ChildMeetingBean) getIntent().getExtras().getSerializable("childMeetingBean");
                if (childMeetingBean != null) {
                    newChildMeetingSet.add(childMeetingBean);
                }
                String newChildMeetingSetStr = gson.toJson(newChildMeetingSet);
                mapValue.put("newChildMeetingSet", newChildMeetingSetStr);

                UpdateThread updateThread = new UpdateThread(mapValue, "UpdateMeetServlet");
                updateThread.start();
                System.out.println("线程开启后的:"+isMeetName);

                if(!isMeetName){
                    System.out.println("会议名称变了");
                    String oldName = meeting.getMeetingName();
                    String meesage = "您好，您参加的"+oldName+"会议，现更名为"+newTitle;
                    //String contentStr = content.getText().toString();
                    for (int i = 0; i < sendList.size(); i++) {
                        String number = sendList.get(i);
                        System.out.println("获取的号码是："+number);
                        // 创建一个PendingIntent对象
                        PendingIntent pi = PendingIntent.getActivity(
                                UpdateActivity.this, 0, new Intent(), 0);
                        // 发送短信
                        sManage.sendTextMessage(number, "18361082640", meesage, pi, null);
                    }
                    // 提示短息群发完成
                    Toast.makeText(UpdateActivity.this, "短信群发完成！",
                            Toast.LENGTH_LONG).show();
                }

                if (!isStartTime) {
                    System.out.println("会议开始时间变了");
                    String message = "您好，您参加的"+newTitle+"会议，现会议开始时间更改为"+newStartTime;

                }
                if (!isPlace) {
                    System.out.println("会议地点变了");
                    //发短信
                    String message = "您好，您参加的"+newTitle+"会议，现会议地点更改为"+newPlace;
                    //发短信
                    //sendSMS("18361082640",message);

                }
                if (!isSpecial) {
                    System.out.println("会议特邀嘉宾变了");
                    //发短信
                    String message = "您好，您参加的"+newTitle+"会议，现会议特邀嘉宾更改为"+newSpecial;
                    //发短信
                    //sendSMS("18361082640",message);
                }
                if (!isCost) {
                    System.out.println("会议费用变了");
                    //发短信
                    String message = "您好，您参加的"+newTitle+"会议，现会议开始时间更改为"+newCost;
                    //发短信
                    //sendSMS("18361082640",message);
                }

               /* this.finish();
                break;*/
            case R.id.btn_updatecancel:
                this.finish();
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
                updateStartTime.setText(days);
            } else {
                Toast.makeText(UpdateActivity.this, "您当前选择的时间早于系统时间，请重新选择！", Toast.LENGTH_SHORT).show();
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
            String startTime = updateStartTime.getText().toString();
            if (!startTime.equals("")) {
                Boolean iszao = dateUtil.compareTwoTime(startTime, days);
                System.out.println("看选择的时间是否早于会议开始时间：" + iszao);
                if (iszao) {
                    updateOverTime.setText(days);
                } else {
                    Toast.makeText(UpdateActivity.this, "会议截止时间不能早于会议开始时间！请重新选择！", Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean isearly = dateUtil.compareNowTime(days);

                if (isearly) {
                    Toast.makeText(UpdateActivity.this, "您当前选择的时间早于系统时间，请重新选择！", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };


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
                            Toast.makeText(UpdateActivity.this,
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
