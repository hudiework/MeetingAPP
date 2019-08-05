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

import java.util.ArrayList;
import java.util.List;

public class SecretaryCheckActivity extends Activity{

	private ListView listView;
	private List<ContentEntity> list;
	private List<ContentEntity> contentList;
	private MessageAdapter messageAdapter;
	private static final String HTTPPATH = Url.URLa+"SecretaryCheckServlet";
	private static String id;
	private String myRole;

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
			contentList=new ArrayList<>();
			for(int i=0;i<list.size();i++){
				if(list.get(i).getCheckMessage()==null || list.get( i ).getCheckMessage().equals( "" )){
					contentList.add(list.get(i));
				}
			}
			messageAdapter = new MessageAdapter(SecretaryCheckActivity.this,contentList);
			listView.setAdapter(messageAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
					ContentEntity content=list.get(position);
                    Intent i=new Intent(SecretaryCheckActivity.this,DetailActivity.class);
					i.putExtra( "content",content);
					i.putExtra( "role",myRole);
                    startActivity(i);
				}
			});
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		listView = (ListView) findViewById(R.id.listView);
		id = getIntent().getStringExtra("meetingId");
		myRole=getIntent().getStringExtra( "role" );
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
							id, "utf-8", "text/html");
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendMessage(message);
			}
		}).start();
	}

}