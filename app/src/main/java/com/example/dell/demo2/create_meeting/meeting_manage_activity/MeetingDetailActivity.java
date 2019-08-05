package com.example.dell.demo2.create_meeting.meeting_manage_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.create_meeting.connect.OkHttpUtil;
import com.example.dell.demo2.loadutils.ProgressListener;
import com.example.dell.demo2.login_main.fakeMainActivity;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.loginsucce_participantpart.ChildMeetingBean;
import com.example.dell.demo2.supervisor.beans.WorkerBean;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MeetingDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvShowTitle, tvShowDate, tvShowDeadline, tvShowAddress, tvShowPeople, tvShowSpecial, tvShowCost, tvShowManagers, tvShowSecretary, tvShowWorkers;
    private Button btnChecked, btnUnChecked, btnCheckChild;
    private MeetingBean meeting;
    private List<ChildMeetingBean> childMeetingList;
    private HashSet<WorkerBean> workerSet, leaderSet, secretarySet;
    private AddEventThread addEventThread;
    public Handler handler;
    private Gson gson = new Gson();
    private String childMeetingListStr;
    private  String agendaPath,guidePath;
    private Boolean creatPost = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail2);
        init();
    }

    private void init() {
        tvShowTitle = findViewById(R.id.et_showMeetingTitle);
        tvShowDate = findViewById(R.id.et_startDate);
        tvShowDeadline = findViewById(R.id.et_showDeadline);
        tvShowAddress = findViewById(R.id.et_showMeetingAddress);
        tvShowPeople = findViewById(R.id.et_showPeople);
        tvShowSpecial = findViewById(R.id.tv_showSpecial);
        tvShowCost = findViewById(R.id.tv_showCost);
        tvShowManagers = findViewById(R.id.et_showManagers);
        tvShowSecretary = findViewById(R.id.et_showSecretary);
        tvShowWorkers = findViewById(R.id.et_showWorkers);

        btnCheckChild = findViewById(R.id.btn_checkChildMeet);
        btnChecked = findViewById(R.id.btn_showChecked);
        btnUnChecked = findViewById(R.id.btn_unChecked);
        btnCheckChild.setOnClickListener(this);
        btnChecked.setOnClickListener(this);
        btnUnChecked.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        meeting = (MeetingBean) bundle.getSerializable("meeting");
        System.out.println("MeetingDetailActivity页面："+meeting.toString());
        childMeetingList = (List<ChildMeetingBean>) bundle.getSerializable("childMeetingList");
        workerSet = (HashSet<WorkerBean>) bundle.getSerializable("workerSet");
        leaderSet = (HashSet<WorkerBean>) bundle.getSerializable("leaderSet");
        secretarySet = (HashSet<WorkerBean>) bundle.getSerializable("secretarySet");
        agendaPath = bundle.getString("agendaPath");
        guidePath = bundle.getString("guidePath");

        tvShowTitle.setText(meeting.getMeetingName());
        tvShowDate.setText(meeting.getMeetingDate().toString());
        tvShowDeadline.setText(meeting.getMeetingOverDate());
        tvShowAddress.setText(meeting.getMeetingPlace().toString());
        tvShowPeople.setText(meeting.getMeetingPeople().toString());
        tvShowSpecial.setText(meeting.getMeetingSpecial().toString());
        tvShowCost.setText(meeting.getMeetingCost() + "");
        for (WorkerBean l : leaderSet) {
            System.out.println("主管详情："+l.getWorkerName().toString());
            tvShowManagers.append(l.getWorkerName().toString() + " ");
        }
        for (WorkerBean s : secretarySet) {
            System.out.println("秘书详情："+s.getWorkerName().toString());
            tvShowSecretary.append(s.getWorkerName().toString() + " ");
        }
        for (WorkerBean w : workerSet) {
            System.out.println("工作人员详情："+w.getWorkerName().toString());
            tvShowWorkers.append(w.getWorkerName().toString() + " ");
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_checkChildMeet:
                Intent i = new Intent(MeetingDetailActivity.this, ChildMeetListActivity.class);
                String eventName = meeting.getMeetingName().toString();
                Bundle b = new Bundle();
                b.putString("eventName", eventName);
                b.putSerializable("childMeetingList", (Serializable) childMeetingList);
                i.putExtras(b);
                startActivity(i);
                break;
            case R.id.btn_showChecked:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否");
                builder.setMessage("确认创办此会议?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Map<String, String> mapValue = new HashMap<>();

                        String meetingStr = gson.toJson(meeting);//把对象转为JSON格式的字符串
                        if (childMeetingList == null || childMeetingList.size() == 0) {
                            childMeetingListStr = "";
                        } else {
                            childMeetingListStr = gson.toJson(childMeetingList);
                        }

                        String workerSetStr = gson.toJson(workerSet);
                        String leaderSetStr = gson.toJson(leaderSet);
                        String secretarySetStr = gson.toJson(secretarySet);

                        // System.out.println("把meeting对象转为JSON格式的字符串///  "+MeetingStr);

                        mapValue.put("meeting", meetingStr);
                        mapValue.put("childMeetingList", childMeetingListStr);
                        mapValue.put("workerSet", workerSetStr);
                        mapValue.put("leaderSet", leaderSetStr);
                        mapValue.put("secretarySet", secretarySetStr);



                        addEventThread = new AddEventThread(mapValue, "AddEvent");
                        addEventThread.start();


                        handler = new Handler() {
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                               String data= msg.obj.toString();
                                System.out.println("服务器端返回的->" + data);
                                ArrayList<String> list= gson.fromJson(
                                        data, new TypeToken<ArrayList<String>>() {
                                        }.getType());
                                String result = list.get(0);
                                String meetingId = list.get(1);
                                System.out.println("创建时传回来的id:"+meetingId);

                                if (result.equals("")) {
                                    Toast.makeText(MeetingDetailActivity.this, "服务器连接失败！请重试！", Toast.LENGTH_SHORT).show();

                                }
                                if (result.equals("failure")) {
                                    Toast.makeText(MeetingDetailActivity.this, "创建会议失败！", Toast.LENGTH_SHORT).show();
                                } else {
                                    System.out.println("详情页面路径："+agendaPath);
                                    postFile(agendaPath,meetingId);
                                    postFile(guidePath,meetingId);
                                    if(creatPost==true){
                                        Toast.makeText(MeetingDetailActivity.this, "创建会议成功！",
                                                Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(MeetingDetailActivity.this, fakeMainActivity.class);
                                        startActivity(i);
                                    }else if(creatPost = false){
                                        Toast.makeText(MeetingDetailActivity.this, "文件上传失败，请重新上传！",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }


                            }
                        };

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.btn_unChecked:
                Intent inte = new Intent(MeetingDetailActivity.this, CreateMeetingMainActivity.class);
                startActivity(inte);
                break;
        }

    }

    public class AddEventThread extends Thread {
        private Map<String, String> map;
        private String servlet;
        OkHttpUtil okHttpUtil = new OkHttpUtil();

        public AddEventThread(Map<String, String> map, String servlet) {
            this.map = map;
            this.servlet = servlet;
        }

        public void run() {
            String url = Url.URLa + servlet;
            okHttpUtil.post(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("错误来了：", "执行错误回调！！！ ", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseJsonStr = response.body().string();
                    System.out.println("服务器端返回的信息" + responseJsonStr);
                    Message message = handler.obtainMessage();
                    message.obj = responseJsonStr;
                    handler.sendMessage(message);
                }
            }, map);

        }


    }



    public void postFile(String path,String meetingId){
        System.out.println("在上传方法里查看path"+path);
        File file = new File(path);

        String postUrl = Url.URLa+"UploadFileServlet";

        com.example.dell.demo2.loadutils.OkHttpUtil.postFile(postUrl,meetingId, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                //Log.i(TAG, "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
                int progress = (int) (currentBytes * 100 / contentLength);
//                    post_progress.setProgress(progress);
//                    post_text.setText(progress + "%");
                if(progress==100){
                    //post_progress.setVisibility(View.GONE); //设置进度条不可见
//                        Toast.makeText(LoadFileMainActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
//                        progress = 0;
//                        post_progress.setProgress(progress);
//                        post_text.setText(progress + "%");
                }
            }
        }, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                creatPost = false;
                System.out.println("响应失败，原因："+e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    String result = response.body().string();
                    System.out.println("result===" + result);
                    // Log.i(TAG, "result===" + result);
                }
            }
        }, file);

    }


}
