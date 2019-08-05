package com.example.dell.demo2.download;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loadutils.OkHttpUtil;
import com.example.dell.demo2.loadutils.ProgressListener;
import com.example.dell.demo2.selectfileutils.FileUtils;
import com.example.dell.demo2.web_url_help.Url;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoadFileMainActivity extends AppCompatActivity implements View.OnClickListener{
    private static int MY_PERMISSIONS_REQUEST_CODE = 123;
    private static int DOCUMENTSUI_REQUEST_CODE = 456;
    public static String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/huiyiguanlidown";
    private ProgressBar post_progress;
    private TextView post_text;
    private String meetingId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_file_main);

        initView();
    }

    public void initView(){
        post_progress = (ProgressBar) findViewById(R.id.post_progress);
        post_text = (TextView) findViewById(R.id.post_text);

        meetingId = getIntent().getStringExtra("meetingId");

        findViewById(R.id.ok_post_file).setOnClickListener(this);

         /* //Android 6.0系统读写文件出现FileNotFoundException:EACCES (permission denied)解决办法:
        *//**
         * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
         */

        if (Build.VERSION.SDK_INT >= 23) {
            //,Manifest.permission.READ_EXTERNAL_STORAGE
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, MY_PERMISSIONS_REQUEST_CODE);
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_post_file:
                showChooser();
                break;

        }
    }


    public void postFile(String path){
        System.out.println("在上传方法里查看path"+path);
        File file = new File(path);

        String postUrl = Url.URLa+"UploadFileServlet";

        OkHttpUtil.postFile(postUrl,meetingId, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                //Log.i(TAG, "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
                int progress = (int) (currentBytes * 100 / contentLength);
                post_progress.setProgress(progress);
                post_text.setText(progress + "%");
                if(progress==100){
                    //post_progress.setVisibility(View.GONE); //设置进度条不可见
                    Toast.makeText(LoadFileMainActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                    progress = 0;
                    post_progress.setProgress(progress);
                    post_text.setText(progress + "%");
                }
            }
        }, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("响应失败，原因："+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    String result = response.body().string();
                    System.out.println("result===" + result);
                   // Log.i(TAG, "result===" + result);
                }
            }
        }, file);

    }


    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, DOCUMENTSUI_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 456:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        //Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            postFile(path);
                            Toast.makeText(LoadFileMainActivity.this,
                                    "File Selected: " + path, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("FileSelectorTestActivity", "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
