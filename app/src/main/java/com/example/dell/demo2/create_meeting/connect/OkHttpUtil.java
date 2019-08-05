package com.example.dell.demo2.create_meeting.connect;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttpUtil {


    public static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS)
                                                .readTimeout(10000,TimeUnit.MILLISECONDS)
                                                .writeTimeout(10000,TimeUnit.MILLISECONDS).build();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");


    public static String doPost(String url,String phone){

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("phone",phone);

        Request request  = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response!=null){
                //System.out.println("OKHttpUtil里的doPost()方法值："+response.body().string());
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }



    public static void post(String address, okhttp3.Callback callback, Map<String,String> map)
    {
        FormBody.Builder builder = new FormBody.Builder();
        if (map!=null)
        {
            for (Map.Entry<String,String> entry:map.entrySet())
            {
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }


}
