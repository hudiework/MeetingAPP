package com.example.dell.demo2.login_main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingPart_MainActivity;
import com.example.dell.demo2.loginsucce_participantpart.ParticipantMainActivity;
import com.example.dell.demo2.worker.WorkerActivity;

public class fakeMainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_participant;
private Button btn_worker;
private Button btn_fake_workerTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_main);
        btn_participant=findViewById(R.id.btn_fake_participant);
        btn_worker=findViewById(R.id.btn_fake_worker);
        btn_fake_workerTest=findViewById(R.id.btn_fake_workerTest);
        btn_participant.setOnClickListener(this);
        btn_worker.setOnClickListener(this);
        btn_fake_workerTest.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_fake_participant:{
                System.out.println("点击了");
                Toast.makeText(this,"点击了",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(this,ParticipantMainActivity.class);
                startActivity(in);
            }break;
            case R.id.btn_fake_worker:{
                Toast.makeText(this,"点击了",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(this, MeetingPart_MainActivity.class);
                startActivity(in);
            };break;
            case R.id.btn_fake_workerTest:{
                Intent i3=new Intent(this, WorkerActivity.class);
                startActivity(i3);
            };break;
        }
    }
}
