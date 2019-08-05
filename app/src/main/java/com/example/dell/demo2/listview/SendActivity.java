package com.example.dell.demo2.listview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.example.dell.demo2.R;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingPart_MyMeetingActivity;
import com.example.dell.demo2.secretary.SecretaryMainActivity;
import com.example.dell.demo2.supervisor.SupervisorActivity;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.example.dell.demo2.entity.ContentEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;

public class SendActivity extends Activity implements OnClickListener {

	private Button btnJoin, btnAll, btnWork;
	private static final String HTTPPATH = Url.URLa+"SendServlet";
	private ContentEntity contentEnitity;
	private String myRole,id;
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String str = msg.obj.toString();
			Builder builder=new Builder(SendActivity.this);
			Log.e("******",str);
	        if(str.equals("true")){
	        	builder.setMessage("发布成功");
			}else{
				builder.setMessage("失败");
			}
        	builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					if(myRole.equals( "主管" )){
						Intent intent=new Intent(SendActivity.this,SupervisorActivity.class);
						intent.putExtra( "meetingId",id );
						startActivity(intent);
					}else{
						Intent intent=new Intent(SendActivity.this,SecretaryMainActivity.class);
						intent.putExtra( "meetingId",id );
						startActivity(intent);
					}


				}
			});
        	builder.create().show();
				
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send);
		btnJoin = (Button) findViewById(R.id.btn_join);
		btnAll = (Button) findViewById(R.id.btn_all);
		btnWork = (Button) findViewById(R.id.btn_work);
		btnJoin.setOnClickListener(this);
		btnWork.setOnClickListener(this);
		btnAll.setOnClickListener(this);
        contentEnitity= (ContentEntity) getIntent().getSerializableExtra( "content" );
	    myRole=getIntent().getStringExtra( "role" );
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_join:
			contentEnitity.setTag("1");
			break;
		case R.id.btn_all:
			contentEnitity.setTag("2");
			break;
		case R.id.btn_work:
			contentEnitity.setTag("3");
			break;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = handler.obtainMessage();
				Gson gson = new Gson();
				final String jsonstr = gson.toJson(contentEnitity);
				try {
					Log.e("********",jsonstr);
					message.obj = HttpUtils.httpURLConnectionPostData(HTTPPATH,
							jsonstr, "utf-8", "text/html");
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendMessage(message);
			}
		}).start();
	}

}
