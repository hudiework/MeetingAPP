package com.example.dell.demo2.pollingservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dell.demo2.R;


public class MessageActivity extends AppCompatActivity {
private PollingService pollingService;
private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        /*NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();*/
        tv=(TextView) findViewById(R.id.tv);
        pollingService=new PollingService();
      // String str= PollingService.getInstance().getStr();


    }
}
