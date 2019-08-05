package com.example.dell.demo2.loginsucce_meetingpart;

import android.os.Handler;
import android.os.Message;

import com.example.dell.demo2.loadutils.OkHttpUtil;
import com.example.dell.demo2.web_url_help.Url;

/**
 * Created by dell on 2018-05-02.
 */

public class QuerryChildThread extends Thread {
    private String servlet;
    private String meetingId;
    private Handler handler;

    public QuerryChildThread(String meetingId, String servlet,Handler handler) {
        this.meetingId = meetingId;
        this.servlet = servlet;
        this.handler = handler;
    }

    public void run() {
        String url = Url.URLa + servlet;
        String response = OkHttpUtil.doPost(url, meetingId);
        System.out.println("更新界面返回的子会议：" + response);
        Message message = handler.obtainMessage();
        message.obj = response;
        message.what = 121;
        handler.sendMessage(message);

    }

}
