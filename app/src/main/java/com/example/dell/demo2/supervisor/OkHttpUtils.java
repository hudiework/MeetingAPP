package com.example.dell.demo2.supervisor;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Created by Administrator on 2018/3/18.
 */

public class OkHttpUtils {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

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
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void postByInt(String address, okhttp3.Callback callback, Map<String,Integer> map)
    {
        FormBody.Builder builder = new FormBody.Builder();
        if (map!=null)
        {
            for (Map.Entry<String,Integer> entry:map.entrySet())
            {
                builder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }
}
