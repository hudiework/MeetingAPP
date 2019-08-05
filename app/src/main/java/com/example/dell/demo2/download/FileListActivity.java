package com.example.dell.demo2.download;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loadutils.OkHttpUtil;
import com.example.dell.demo2.loadutils.ProgressListener;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FileListActivity extends AppCompatActivity {
    public static String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/huiyiguanlidown";

    private ListView lvFilesList;
    private TextView tvEmptyData,tv_showProgress;
    private ProgressBar progressBar;
    private ImageView ivEmptyData;
    private Handler handler;
    private ShowFilesThread showFilesThread;
    private String meetingId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        lvFilesList = findViewById(R.id.lv_files);
        tvEmptyData = findViewById(R.id.tv_empty_data);
        ivEmptyData =findViewById(R.id.iv_empty_data);
//        tv_showProgress = findViewById(R.id.tv_showprogress);
//        progressBar = findViewById(R.id.progressBar);


        meetingId = getIntent().getStringExtra("meetingId");
        showFilesThread = new ShowFilesThread("ShowFilesServlet",meetingId);
        showFilesThread.start();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String data = msg.obj.toString();
                System.out.println("服务器端返回的->" + data);
                if (data.equals("")) {
                    Toast.makeText(FileListActivity.this, "服务器连接失败！请重试！", Toast.LENGTH_SHORT).show();

                }else {
                    Gson gson = new Gson();
                    List<String> fileList = gson.fromJson(data, new TypeToken<List<String>>(){}.getType());

                    if(fileList == null||fileList.size()==0){
                        System.out.println("没有文件时列表:" + fileList.size());
                        tvEmptyData.setText("本会议暂无文件资料，等待上传！");
                        ivEmptyData.setVisibility(View.VISIBLE);
                    }else{
                        ivEmptyData.setVisibility(View.GONE);
                        FileAdapter fileAdapter = new FileAdapter(fileList,FileListActivity.this);
                        lvFilesList.setAdapter(fileAdapter);

                        lvFilesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                // adapterView代表适配器组件（如listview,gridview等）
                                String filename = (String) adapterView.getItemAtPosition(position);
                                download(filename);
                            }
                        });
                    }
                }
            }
        };
    }

    class ShowFilesThread extends Thread{
        private String servlet,meetingId;

        public ShowFilesThread(String servlet, String meetingId) {
            this.servlet = servlet;
            this.meetingId = meetingId;
        }

        @Override
        public void run() {
            super.run();
            String url = Url.URLa + servlet;
            System.out.println("看访问服务器路径："+url);
            String responseJsonStr = OkHttpUtil.doPost(url,meetingId);
            System.out.println("服务器端返回的信息" + responseJsonStr);
            Message message = handler.obtainMessage();
            message.obj = responseJsonStr;
            handler.sendMessage(message);
        }
    }

    public void download(final String fileName){
        Toast.makeText(FileListActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
        //String url = "http://172.17.73.38:8080/myServer/upload/"+fileName;
        String url = Url.URLa+"upload/"+fileName;
        // final String fileName = url.split("/")[url.split("/").length - 1];
        System.out.println("看url:"+url);
        System.out.println("文件名：" + fileName);
        OkHttpUtil.downloadFile(url, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                System.out.println("执行download方法了没？");

               // Log.i(TAG, "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
                int progress = (int) (currentBytes * 100 / contentLength);
                System.out.println("看进度值："+progress);
                /*progressBar.setProgress(progress);
                tv_showProgress.setText(progress + "%");*/
                if(progress==100){
                    // download_progress.setVisibility(View.GONE); //设置进度条不可见
                    Toast.makeText(FileListActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
                   /* progress = 0;
                    progressBar.setProgress(progress);
                    tv_showProgress.setText("");*/
                }

            }
        }, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("失败，原因:" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    InputStream is = response.body().byteStream();
                    // 储存下载文件的目录
                    File dir = new File(basePath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    System.out.println("2看看存储的路径是：" + basePath + File.separator + fileName);
                    int len = 0;
                    byte[] buffer = new byte[2048];
                    while (-1 != (len = is.read(buffer))) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                }
            }
        });
    }
}
