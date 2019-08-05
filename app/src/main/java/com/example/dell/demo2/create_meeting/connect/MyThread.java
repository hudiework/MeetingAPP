package com.example.dell.demo2.create_meeting.connect;

import android.os.Handler;
import android.os.Message;

import com.example.dell.demo2.web_url_help.Url;

/**
 * Created by dell on 2018-04-21.
 */

public class MyThread extends Thread {
    private String phone;
    private Handler handler;
    private String servlet;

    public MyThread(String phone, Handler handler, String servlet) {
        this.phone = phone;
        this.handler = handler;
        this.servlet = servlet;
    }

    @Override
    public void run() {
        String url = Url.URLa + servlet;
        // TODO Auto-generated method stub
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        String strhttp = okHttpUtil.doPost(url,phone);
        Message message = handler.obtainMessage();
        message.obj = strhttp;
        handler.sendMessage(message);
    }

}
