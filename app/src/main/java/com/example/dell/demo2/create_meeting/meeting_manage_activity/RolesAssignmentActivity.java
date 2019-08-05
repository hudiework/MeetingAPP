package com.example.dell.demo2.create_meeting.meeting_manage_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.create_meeting.entity.MapWorkerMeeting;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.loginsucce_participantpart.ChildMeetingBean;
import com.example.dell.demo2.supervisor.beans.WorkerBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RolesAssignmentActivity extends AppCompatActivity implements View.OnClickListener , RadioGroup.OnCheckedChangeListener {
    private Integer workId;
    private Button btnPrevious, btnNext, btnSecretary, btnLeader, btnWorker;
    private TextView tvLeader,tvSecretary,tvWorker;
    private RadioGroup rgOwn;
    private Intent intent;
    private MeetingBean meeting;
    private List<ChildMeetingBean> childMeetingList = new ArrayList<ChildMeetingBean>();
    private MapWorkerMeeting map;
    private Bundle bundle;
    private HashSet<WorkerBean> workerSet,leaderSet,secretarySet;
    private WorkerBean leader,secretary,worker;
    private  String agendaPath,guidePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles_assignment);
        meeting= (MeetingBean) getIntent().getExtras().getSerializable("meeting");
        System.out.println("RoleAssignActivity页面："+meeting.toString());
        workId = getIntent().getExtras().getInt("workId", 0);
        agendaPath = getIntent().getStringExtra("agendaPath");
        System.out.println("会议议程文件："+agendaPath);
        guidePath = getIntent().getStringExtra("guidePath");
        System.out.println("会议指南文件："+guidePath);
        childMeetingList = (List<ChildMeetingBean>) getIntent().getExtras().getSerializable("childMeetingList");
        initView();
        map = new MapWorkerMeeting(workId+"", "工作人员");
        workerSet=new HashSet<WorkerBean>();
        leaderSet=new HashSet<WorkerBean>();
        secretarySet=new HashSet<WorkerBean>();
        bundle=new Bundle();
    }
    private void initView() {
        btnLeader = findViewById(R.id.btn_leader);
        btnPrevious = findViewById(R.id.btn_previous);
        btnWorker = findViewById(R.id.btn_worker);
        btnSecretary = findViewById(R.id.btn_secretary);
        btnNext = findViewById(R.id.btn_childnext);
        rgOwn = findViewById(R.id.rg_own);
        tvLeader=findViewById(R.id.tv_leader);
        tvSecretary=findViewById(R.id.tv_secretary);
        tvWorker=findViewById(R.id.tv_worker);

        btnLeader.setOnClickListener(this);
        btnWorker.setOnClickListener(this);
        btnSecretary.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        rgOwn.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_leader:
                intent = new Intent(RolesAssignmentActivity.this,
                        LeaderAddActivity.class);
                bundle.putSerializable("leaderSet",leaderSet);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
                break;
            case R.id.btn_secretary:
                intent = new Intent(RolesAssignmentActivity.this,
                        SecretaryAddActivity.class);
                bundle.putSerializable("secretarySet",secretarySet);
                intent.putExtras(bundle);
                startActivityForResult(intent,2);
                break;
            case R.id.btn_worker:
                intent = new Intent(RolesAssignmentActivity.this,
                        WorkerAddActivity.class);
                bundle.putSerializable("workerSet",workerSet);
                intent.putExtras(bundle);
                startActivityForResult(intent,3);
                break;
            case R.id.btn_childnext:
                if(CheckData()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("是否");
                    builder.setMessage("确认分配这些角色?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(RolesAssignmentActivity.this, "提交成功",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RolesAssignmentActivity.this,
                                    MeetingDetailActivity.class);
                            bundle = new Bundle();
                            bundle.putSerializable("meeting",meeting);
                            bundle.putSerializable("childMeetingList", (Serializable) childMeetingList);
                            bundle.putSerializable("map",map);
                            bundle.putString("agendaPath",agendaPath);
                            bundle.putString("guidePath",guidePath);
                            bundle.putSerializable("leaderSet",leaderSet);
                            bundle.putSerializable("secretarySet",secretarySet);
                            bundle.putSerializable("workerSet",workerSet);
                            intent.putExtras(bundle);
                            startActivity(intent);

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
                }

                break;
            case R.id.btn_previous:
                this.finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        if (group.getId() == R.id.rg_own && checkedId > -1) {
            switch (checkedId) {
                case R.id.rb_leader:
                    map.setRole("主管");
                    break;
                case R.id.rb_secretary:
                    map.setRole("秘书");
                    break;
                case R.id.rb_worker:
                    map.setRole("工作人员");
                    break;
                default:
                    break;
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            String leaderName=data.getStringExtra("leaderName");
            System.out.println("leaderName"+leaderName);
            leader=(WorkerBean)data.getSerializableExtra("leader");
            tvLeader.append(leaderName+"  ");
            leaderSet.add(leader);
        }
        if (requestCode == 2 && resultCode == 2) {
            String secretaryName=data.getStringExtra("secretaryName");
            System.out.println("secretaryName"+secretaryName);
            secretary=(WorkerBean)data.getSerializableExtra("secretary");
            tvSecretary.append(secretaryName+"  ");
            secretarySet.add(secretary);
        }
        if (requestCode == 3 && resultCode == 3) {
            String workerName=data.getStringExtra("workerName");
            worker=(WorkerBean)data.getSerializableExtra("worker");
            System.out.println("workerName"+workerName);
            tvWorker.append(workerName+"  ");
            workerSet.add(worker);
        }
    }

    public Boolean CheckData(){
        if(leaderSet.size()==0){
            Toast.makeText(RolesAssignmentActivity.this,"请添加至少一位主管!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(workerSet.size()==0){
            Toast.makeText(RolesAssignmentActivity.this,"请添加至少一位工作人员!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(secretarySet.size()==0){
            Toast.makeText(RolesAssignmentActivity.this,"请添加至少一位秘书!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
