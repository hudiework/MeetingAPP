package com.example.dell.demo2.loginandregister.web;

import android.util.Log;

import com.example.dell.demo2.web_url_help.Url;

/**
 * Created by dell on 2018-03-16.
 */

public class RegistPostService {
    static int CONNECT_FAILED = 1;
    static int REGIST_FAILED = 2;
    static int REGIST_SUCCEEDED = 3;

    public static int send(String url,String mobilephone,String password,String imagePath) {
        // 返回值
        int responseInt = REGIST_FAILED;
        // 定位服务器的Servlet
       // String servlet = "RegistServlet";
        // 通过 POST 方式获取 HTTP 服务器数据
        String responseMsg;
        //String url = Url.URLa+servlet;
        responseMsg = OkHttpUtil.doPost(url,mobilephone,password,imagePath);
        //responseMsg = MyHttpPost.executeHttpPost(servlet, params);
        Log.i("tag", "RegistService: responseMsg = " + responseMsg);
        // 解析服务器数据，返回相应 Long 值
        if(responseMsg.equals("")){
            responseInt = CONNECT_FAILED;
        }else if(responseMsg.equals("SUCCEEDED")) {
            responseInt = REGIST_SUCCEEDED;
        }else{
            responseInt = REGIST_FAILED;
        }
        return responseInt;
    }
}
