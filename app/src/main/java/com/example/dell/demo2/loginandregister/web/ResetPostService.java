package com.example.dell.demo2.loginandregister.web;

import android.util.Log;


/**
 * Created by dell on 2018-03-16.
 */

public class ResetPostService {
    static int RESET_FAILED = 4;
    static int RESET_SUCCEEDED = 5;

    public static int send(String url,String mobilephone,String password){
        int responseInt = RESET_FAILED;
        // 定位服务器的Servlet
        //String servlet = "ResetServlet";
        // 通过 POST 方式获取 HTTP 服务器数据
        String responseMsg;
       // responseMsg = MyHttpPost.executeHttpPost(servlet, params);
        responseMsg = OkHttpUtil.doPost(url,mobilephone,password);
        Log.i("tag", "RegsetService: responseMsg = " + responseMsg);
        // 解析服务器数据，返回相应 Long 值
        if(responseMsg.equals("SUCCEEDED")) {
            responseInt = RESET_SUCCEEDED;
        }
        return responseInt;
    }
}
