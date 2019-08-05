package com.example.dell.demo2.supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.download.LoadFileMainActivity;
import com.example.dell.demo2.listview.AllPeopleActivity;
import com.example.dell.demo2.listview.CallSecretaryActivity;
import com.example.dell.demo2.listview.MainActivity;
import com.example.dell.demo2.listview.SecretaryCheckActivity;
import com.example.dell.demo2.listview.SendActivity;
import com.example.dell.demo2.listview.WriteActivity;
import com.example.dell.demo2.secretary.SecretaryMainActivity;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SupervisorActivity extends AppCompatActivity{
    private Button btn_getLimit;
    private Button btn_getMeetingDetail;
    private Button btn_putMsg;
    private Button btn_pendingApplication;
    private Button btn_uploadFiles;
    private Button btn_callSecretary;
    private Button btn_goWorkerTalk;
    private Button btn_goConsultTalk;
    private OkHttpUtils okHttpUtils;
    private String responseJsonStr;
    private Gson gson;
    private List<Map<String, String>> list;
    private Map<String, String> map;
    private final static String URL = Url.URLa;
    private String id,workName,myRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_limit);
        init();
       //为工作人员动态分配权限
        btn_getLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = URL + "SetLimitServlet";
                map = new HashMap<String, String>();
                map.put("meetingId", "1");
                OkHttpUtils.post(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("错误来了：", "执行错误回调！！！ ", e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        responseJsonStr = response.body().string();
                        System.out.println(responseJsonStr + "湖市场上能否");
                        System.out.println(responseJsonStr + "湖市场上能否");
                        System.out.println(responseJsonStr + "湖市场上能否");
                        Intent intent = new Intent(SupervisorActivity.this, ChangeLimitActivity.class);
                        intent.putExtra("responseJsonStr", responseJsonStr);
                        startActivity(intent);
                    }
                }, map);
            }
        });

        //查看会议的准备情况
        btn_getMeetingDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = URL + "PutMeetingDetailServlet";
                map = new HashMap<String, String>();
                map.put("meetingId", "1");
                OkHttpUtils.post(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("错误来了：", "执行错误回调！！！ ", e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        responseJsonStr = response.body().string();
                        System.out.println(responseJsonStr + "查看会议详情");
                        System.out.println(responseJsonStr + "查看会议详情");
                        System.out.println(responseJsonStr + "查看会议详情");
                        Intent intent = new Intent(SupervisorActivity.this, MeetingDetailActivity.class);
                        intent.putExtra("responseJsonStr", responseJsonStr);
                        startActivity(intent);
                    }
                }, map);

            }
        });
        //发布消息
        btn_putMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupervisorActivity.this,WriteActivity.class);
                intent.putExtra("meetingId",id);
                intent.putExtra( "role",myRole);
                startActivity(intent);

            }
        });
        //上傳文件
        btn_uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SupervisorActivity.this, "点击了上传按钮！", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SupervisorActivity.this, LoadFileMainActivity.class);
                i.putExtra("meetingId",id);
                startActivity(i);
            }
        });

        // 工作人员讨论区
        btn_goWorkerTalk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupervisorActivity.this,MainActivity.class);
                intent.putExtra("meetingId",id);
                intent.putExtra( "workName",workName);
                startActivity(intent);
            }
        });

        // 联系秘书
        btn_callSecretary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupervisorActivity.this,CallSecretaryActivity.class);
                intent.putExtra("meetingId",id);
                intent.putExtra( "workName",workName);
                startActivity(intent);
            }
        });

        // 坐席解答区
        btn_goConsultTalk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupervisorActivity.this,AllPeopleActivity.class);
                intent.putExtra("meetingId",id);
                intent.putExtra( "workName",workName);
                startActivity(intent);
            }
        });

        // 审批消息
        btn_pendingApplication.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupervisorActivity.this,SecretaryCheckActivity.class);
                intent.putExtra("meetingId",id);
                intent.putExtra( "role",myRole);
                startActivity(intent);
            }
        } );

    }


    public void init() {
        System.out.println("进了主管的activiy的init方法中！！！");
        System.out.println("进了主管的activiy的init方法中！！！");
        System.out.println("进了主管的activiy的init方法中！！！");
        list = new ArrayList<Map<String, String>>();
        btn_getLimit = (Button) findViewById(R.id.btn_getLimit);
        btn_getMeetingDetail = (Button) findViewById(R.id.btn_getMeetingDetail);
        btn_putMsg = (Button) findViewById(R.id.btn_putMsg);
        btn_pendingApplication = findViewById(R.id.btn_pendingApplication);

        btn_callSecretary = (Button) findViewById(R.id.btn_callSecretary);
        btn_goWorkerTalk = (Button) findViewById(R.id.btn_goWorkerTalk);
        btn_goConsultTalk = (Button) findViewById(R.id.btn_goConsultTalk);
        btn_uploadFiles = findViewById(R.id.btn_uploadFiles);
        gson = new Gson();

        id = getIntent().getStringExtra("meetingId");
        workName=getIntent().getStringExtra( "workName" );
        myRole=getIntent().getStringExtra( "role" );
        System.out.println("执行完了主管的activiy的init方法中！！！");
        System.out.println("执行完了主管的activiy的init方法中！！！");
        System.out.println("执行完了主管的activiy的init方法中！！！");


    }

}
