package com.example.dell.demo2.loginsucce_participantpart;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.adapter.MessageAdapter;
import com.example.dell.demo2.entity.ContentEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.listview.MyDetailActivity;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


/**
 * Created by dell on 2018-04-15.
 */

public class MessageFragment extends Fragment {
    private ListView listView;
    private List<ContentEntity> list;
    private MessageAdapter messageAdapter;
    private static final String HTTPPATH = Url.URLa + "MyServlet";
    private View view;
    private String phone;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        view = inflater.inflate(R.layout.fragment_message, container, false);
        phone = (String)getArguments().get("phone");
        listView = view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                try {
                    message.obj = HttpUtils.httpURLConnectionPostData(HTTPPATH,
                            "2", "utf-8", "text/html");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = msg.obj.toString();
            Log.e("**********", str);
            Gson gson = new Gson();
            list = gson.fromJson(str, new TypeToken<List<ContentEntity>>() {
            }.getType());
            messageAdapter = new MessageAdapter(getActivity(), list);
            listView.setAdapter(messageAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ContentEntity contentEnitity = (ContentEntity) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), MyDetailActivity.class);
                    intent.putExtra("content", contentEnitity);
                    startActivity(intent);
                }
            });
        }
    };

}
