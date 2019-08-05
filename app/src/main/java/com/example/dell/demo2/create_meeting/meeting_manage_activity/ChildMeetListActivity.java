package com.example.dell.demo2.create_meeting.meeting_manage_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.create_meeting.entity.ChildMeetAdaper;
import com.example.dell.demo2.loginsucce_participantpart.ChildMeetingBean;

import java.util.List;

public class ChildMeetListActivity extends AppCompatActivity {
    private TextView tvShowTitle;
    private ListView childMeetListView;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_meet_list);
        tvShowTitle = findViewById(R.id.tv_showTitle);
        childMeetListView = findViewById(R.id.childMeetList);
        back = findViewById(R.id.childList_back);

        Bundle bundle = getIntent().getExtras();
        String eventName = bundle.getString("eventName");
        List<ChildMeetingBean> childMeetingList = (List<ChildMeetingBean>) bundle.getSerializable("childMeetingList");
        if(childMeetingList==null ||childMeetingList.isEmpty()){
            tvShowTitle.setText("此会议无子会议");
        }else{
            tvShowTitle.setText("子会议有：");
            ChildMeetAdaper adapter = new ChildMeetAdaper(childMeetingList,this);
            childMeetListView.setAdapter(adapter);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
