package com.example.dell.demo2.listview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.adapter.MessageAdapter;
import com.example.dell.demo2.entity.ContentEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MyMessageActivity extends Activity{

	private ListView listView;
	private List<ContentEntity> list;
	private MessageAdapter messageAdapter;
	private static final String HTTPPATH = Url.URLa+"MyServlet";

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
			messageAdapter = new MessageAdapter(MyMessageActivity.this,list);
			listView.setAdapter(messageAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					ContentEntity contentEnitity= (ContentEntity) parent.getItemAtPosition(position);
					Intent intent=new Intent(MyMessageActivity.this,MyDetailActivity.class);
					intent.putExtra("content",contentEnitity);
					startActivity(intent);
				}
			});
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		listView = (ListView) findViewById(R.id.listView);
	
	}

	@Override
	protected void onStart() {
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

}