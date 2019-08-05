
package com.example.dell.demo2.supervisor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.supervisor.beans.WorkerBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class ChangeLimitActivity extends AppCompatActivity {
private WorkerBean workerBean;
private List<WorkerBean> list;
private Gson gson;
String responseJsonStr="";
private ListView listView;
private MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_limit);
        init();
        Intent intent=getIntent();
        responseJsonStr= intent.getStringExtra("responseJsonStr");
        try{
            System.out.println("进了try，加载适配器！！");
            System.out.println("进了try，加载适配器！！");
            System.out.println("进了try，加载适配器！！");
            list= gson.fromJson(responseJsonStr, new TypeToken<List<WorkerBean>>(){}.getType());
          myAdapter = new MyAdapter(ChangeLimitActivity.this, list);
            listView.setAdapter(myAdapter);


        }catch (Exception e){
            Log.e("json->list failed:", String.valueOf(e));
        }

        Log.e("new message!!!",responseJsonStr);
        Log.e("new message!!!",responseJsonStr);
        Log.e("new message!!!",responseJsonStr);
        Log.e("new message!!!",responseJsonStr);
        Log.e("new message!!!",responseJsonStr);

       // tv_sponseMsg.setText(responseMSG);
    }
    @SuppressLint("WrongViewCast")
    public void init(){
        workerBean=new WorkerBean();
        gson=new Gson();
        list=new ArrayList<WorkerBean>();
        //map=new HashMap<String,String>();
        listView=(ListView) findViewById(R.id.lv);
    }
}
