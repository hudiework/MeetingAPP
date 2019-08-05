package com.example.dell.demo2.secretary;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.download.LoadFileMainActivity;
import com.example.dell.demo2.listview.AllPeopleActivity;
import com.example.dell.demo2.listview.CallSuperActivity;
import com.example.dell.demo2.listview.MainActivity;
import com.example.dell.demo2.listview.SecretaryCheckActivity;
import com.example.dell.demo2.listview.SecretaryCommentActivity;
import com.example.dell.demo2.listview.SecretaryWorkActivity;
import com.example.dell.demo2.listview.WriteActivity;
import com.example.dell.demo2.selectfileutils.FileUtils;

public class SecretaryMainActivity extends AppCompatActivity implements View.OnClickListener{
private Button btn_secretary_postFile;//上传文件
    private Button btn_secretary_postMsg;//发布消息
    private Button btn_secretary_undone;//查看待审批事件
    private Button btn_secretary_work_place; //工作讨论区
    private Button btn_secretary_hlep_place;//坐席解答区
    private Button btn_secretary_myMsg;//联系主管
    private Button btn_secretary_comment;

    private String id,myRole,workName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_main);
        init();
    }

    public void init(){
        btn_secretary_postFile=findViewById(R.id.btn_secretary_postFile);
        btn_secretary_postMsg=findViewById(R.id.btn_secretary_postMsg);
        btn_secretary_undone=findViewById(R.id.btn_secretary_undone);
        btn_secretary_work_place=findViewById(R.id.btn_secretary_work_place);
        btn_secretary_hlep_place=findViewById(R.id.btn_secretary_help_place);
        btn_secretary_myMsg=findViewById(R.id.btn_secretary_myMsg);
        btn_secretary_comment=findViewById( R.id.btn_secretary_comment );

        btn_secretary_postFile.setOnClickListener(this);
        btn_secretary_postMsg.setOnClickListener(this);
        btn_secretary_myMsg.setOnClickListener(this);
        btn_secretary_work_place.setOnClickListener(this);
        btn_secretary_hlep_place.setOnClickListener(this);
        btn_secretary_undone.setOnClickListener( this );
        btn_secretary_comment.setOnClickListener( this );

        id = getIntent().getStringExtra("meetingId");
        myRole=getIntent().getStringExtra("role");
        workName=getIntent().getStringExtra( "workName" );


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_secretary_postFile:
                Toast.makeText(SecretaryMainActivity.this, "点击了上传按钮", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SecretaryMainActivity.this, LoadFileMainActivity.class);
                i.putExtra("meetingId",id);
                startActivity(i);
                break;
            // 发布消息
            case R.id.btn_secretary_postMsg:
                Toast.makeText(SecretaryMainActivity.this, "点击了发布消息", Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(SecretaryMainActivity.this, WriteActivity.class);
                i1.putExtra("meetingId",id);
                i1.putExtra( "role",myRole );
                startActivity(i1);
                break;
            // 工作讨论区
            case R.id.btn_secretary_work_place:
                Intent i2 = new Intent(SecretaryMainActivity.this,SecretaryWorkActivity.class);
                i2.putExtra("meetingId",id);
                i2.putExtra( "workName",workName );
                startActivity(i2);
                break;
            // 联系主管
            case R.id.btn_secretary_myMsg:
                Intent i3 = new Intent(SecretaryMainActivity.this,CallSuperActivity.class);
                i3.putExtra("meetingId",id);
                i3.putExtra( "workName",workName );
                startActivity(i3);
                break;
            // 坐席解答区
            case R.id.btn_secretary_help_place:
                Intent i4 = new Intent(SecretaryMainActivity.this,AllPeopleActivity.class);
                i4.putExtra("meetingId",id);
                i4.putExtra( "workName",workName );
                startActivity(i4);
                break;

            // 待审批事项
            case R.id.btn_secretary_undone:
                Intent i5= new Intent(SecretaryMainActivity.this,SecretaryCheckActivity.class);
                i5.putExtra("meetingId",id);
                i5.putExtra( "role",myRole );
                startActivity(i5);
                break;
            // 待审批评论
            case R.id.btn_secretary_comment:
                Intent i6= new Intent(SecretaryMainActivity.this, SecretaryCommentActivity.class);
                i6.putExtra("meetingId",id);
                startActivity(i6);
                break;
        }
    }
}
