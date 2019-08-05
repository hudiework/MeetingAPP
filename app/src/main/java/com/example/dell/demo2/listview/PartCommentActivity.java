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
import com.example.dell.demo2.adapter.CommentAdapter;
import com.example.dell.demo2.entity.CommentEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class PartCommentActivity extends Activity{

	private ListView listView;
	private List<CommentEntity> list;
	private CommentAdapter myAdapter;
	private static final String HTTPPATH = Url.URLa+"PartCommentServlet";
	private String mid;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String str = msg.obj.toString();
			Log.e("**********", str);
			Gson gson = new Gson();
			list = gson.fromJson(str, new TypeToken<List<CommentEntity>>() {
			}.getType());
			myAdapter = new CommentAdapter(PartCommentActivity.this,list);
			listView.setAdapter(myAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
					CommentEntity comment=list.get(position);
                    Intent i=new Intent(PartCommentActivity.this,PartDetailActivity.class);
					i.putExtra( "comment",comment);
					i.putExtra( "meetingId",mid);
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
		mid = getIntent().getStringExtra("meetingId");
	}

	@Override
	protected void onStart() {
		super.onStart();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = handler.obtainMessage();
				try {
					Log.e( "********",mid );
					message.obj = HttpUtils.httpURLConnectionPostData(HTTPPATH,
							mid, "utf-8", "text/html");
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendMessage(message);
			}
		}).start();
	}

}