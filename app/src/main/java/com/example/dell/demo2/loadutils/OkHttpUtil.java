package com.example.dell.demo2.loadutils;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpUtil {


    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS)
                                                .readTimeout(10000,TimeUnit.MILLISECONDS)
                                                .writeTimeout(10000,TimeUnit.MILLISECONDS).build();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    //下载文件方法
    public static void downloadFile(String url, final ProgressListener listener, Callback callback){
        System.out.println("执行OKHttpUtil的down");
        //增加拦截器
        OkHttpClient client = okHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response.newBuilder().body(new ProgressResponseBody(response.body(),listener)).build();
            }
        }).build();

        Request request  = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }


    public static void postFile(String url, String meetingId,final ProgressListener listener, Callback callback, File...files){

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Log.i("huang","files[0].getName()=="+files[0].getName());


        builder.addFormDataPart("filename",files[0].getName());
        builder.addFormDataPart("position",meetingId);

        //第一个参数与服务器端一致
        builder.addFormDataPart("file",files[0].getName(), RequestBody.create(MediaType.parse("application/octet-stream"),files[0]));

        MultipartBody multipartBody = builder.build();

        Request request  = new Request.Builder().url(url).post(new ProgressRequestBody(multipartBody,listener)).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    //get请求
    public static String doGet(String url) {
        System.out.println("执行了doGet()方法");
        Request request  = new Request.Builder().url(url).build();
        try {
            System.out.println("到这儿了，2");
            Response response = okHttpClient.newCall(request).execute();
            if (response!=null){
                return response.body().string();
            }
        } catch (IOException e) {
            System.out.println("doGet()方法失败，原因："+e.toString());
            e.printStackTrace();
        }
        return "";

    }

    public static void doGet(String url, Callback callback){
        Request request  = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static String doPost(String url,String meetingId){


        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("meetingId",meetingId);

        Request request  = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();

            if (response!=null){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String doPost(String url,String mobilephone,String password){

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("mobilephone",mobilephone);
        formBodyBuilder.add("password",password);

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

    public static void doPost(String url, String userName, String password, Callback callback){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("username",userName);
        builder.addFormDataPart("password",password);

        Request request  = new Request.Builder().url(url).post(builder.build()).build();
        okHttpClient.newCall(request).enqueue(callback);
    }


    public static void crach(){
        CacheControl.Builder builder = new CacheControl.Builder();
        builder.noCache();//不使用缓存，全部走网络
        builder.noStore();//不使用缓存，也不存储缓存
        builder.onlyIfCached();//只使用缓存
        builder.noTransform();//禁止转码
        builder.maxAge(10, TimeUnit.MILLISECONDS);//指示客户机可以接收生存期不大于指定时间的响应。
        builder.maxStale(10, TimeUnit.SECONDS);//指示客户机可以接收超出超时期间的响应消息
        builder.minFresh(10, TimeUnit.SECONDS);//指示客户机可以接收响应时间小于当前时间加上指定时间的响应。
        CacheControl cache = builder.build();//cacheControl


        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.addFormDataPart("username","password");
        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder().post(requestBody).cacheControl(cache).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){

                }
            }
        });
    }


}
