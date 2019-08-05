package com.example.dell.demo2.loginsucce_meetingpart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.create_meeting.entity.ChildMeetAdaper;
import com.example.dell.demo2.loginsucce_participantpart.ChildMeetingBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

public class UpdateChildActivity extends AppCompatActivity {

    private List<ChildMeetingBean> childMeetingList,childMeetingListNew;
    private TextView tvUpdateTitle;
    private ListView lvUpdateChildMeet;
    private int meetingId;
    private static String eventStartTime, eventOverTime;
    private Handler handler;
    ChildMeetAdaper adapter;
    private HashSet<ChildMeetingBean> newChildMeetingSet = new HashSet<ChildMeetingBean>();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_child);
        childMeetingList = (List<ChildMeetingBean>) getIntent().getExtras().getSerializable("childMeetingList");
        meetingId = getIntent().getExtras().getInt("meetingId");
        newChildMeetingSet = (HashSet<ChildMeetingBean>) getIntent().getExtras().getSerializable("newChildMeetingSet");

        tvUpdateTitle = findViewById(R.id.tv_updateTitle);
        lvUpdateChildMeet = findViewById(R.id.lv_updateChildMeet);

        if (childMeetingList == null || childMeetingList.isEmpty()) {
            tvUpdateTitle.setText("此会议无子会议");
        } else {
            tvUpdateTitle.setText("子会议有：");
            adapter = new ChildMeetAdaper(childMeetingList, this);
            lvUpdateChildMeet.setAdapter(adapter);

            lvUpdateChildMeet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // adapterView代表适配器组件（如listview,gridview等）
                    ChildMeetingBean childMeetingBean = new ChildMeetingBean();
                    childMeetingBean = (ChildMeetingBean) adapterView.getItemAtPosition(position);
                    eventStartTime = getIntent().getStringExtra("eventStartTime");
                    eventOverTime = getIntent().getStringExtra("eventOverTime");
                    Intent intent = new Intent(UpdateChildActivity.this, UpdateChildMeetingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("childMeetingBean", childMeetingBean);
                    int id = childMeetingBean.getChildMeetingId();
                    bundle.putInt("childMeetingBeanId", id);
                    bundle.putSerializable("newChildMeetingSet", (Serializable) newChildMeetingSet);
                    bundle.putString("eventStartTime", eventStartTime);
                    bundle.putString("eventOverTime", eventOverTime);
                    intent.putExtras(bundle);
                    //startActivityForResult(intent,2);
                    startActivity(intent);
                }
            });


        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("执行onActivityResult1，子会议里的");
        if (requestCode == 2) {
            System.out.println("执行onActivityResult2，子会议里的");
            Toast.makeText(this, "子会议页面开始更新！", Toast.LENGTH_SHORT).show();
            QuerryChildThread querryChildThread = new QuerryChildThread(meetingId + "", "ShowChildMeetServlet", handler);
            querryChildThread.start();
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 121) {
                        String data = msg.obj.toString();
                        if (data != "") {
                            childMeetingListNew = gson.fromJson(data, new TypeToken<List<ChildMeetingBean>>() {
                            }.getType());
                            System.out.println("更新界面的childMeetingListNew："+childMeetingListNew);
                        }
                    }
                }

            };
            childMeetingList.clear();
            childMeetingList.add((ChildMeetingBean) childMeetingListNew);
            adapter.notifyDataSetChanged();
        }

    }


}