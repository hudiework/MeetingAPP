package com.example.dell.demo2.loginandregister.web;

import android.util.Log;

import com.example.dell.demo2.web_url_help.Url;


/**
 * Created by dell on 2018-03-16.
 */

public class LoginPostService {
    static int LOGIN_FAILED = 0;
    static int LOGIN_SUCCEEDED = 1;
    static int CONNECT_FAILED = 444;

    public static int send(String mobilephone,String password) {
        // 返回值
        int responseInt = CONNECT_FAILED;
        // 定位服务器的Servlet
        String servlet = "LoginServlet";
        // 通过 POST 方式获取 HTTP 服务器数据
        String responseMsg;
        //responseMsg = MyHttpPost.executeHttpPost(servlet, params);
        String url = Url.URLa+servlet;
        responseMsg = OkHttpUtil.doPost(url,mobilephone,password);
        System.out.println("看看有没有执行post方法"+responseMsg);
        Log.i("tag", "LoginService: responseMsg = " + responseMsg);
        // 解析服务器数据，返回相应 Long 值
        if(responseMsg.equals("SUCCEEDED")) {
            responseInt = LOGIN_SUCCEEDED;
        }else if(responseMsg.equals("FAILED")){
            responseInt = LOGIN_FAILED;
        }
        return responseInt;
    }
}
