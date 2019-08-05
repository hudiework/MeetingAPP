package com.example.dell.demo2.listview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.adapter.MsgViewAdapter;
import com.example.dell.demo2.entity.MessageEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CallSecretaryActivity extends Activity implements OnClickListener {
	private Button back, send;
	private ListView chatlist;
	private EditText input;
	private TextView txt;
	private MsgViewAdapter adapter;
	private List<MessageEntity> messages = new ArrayList<MessageEntity>();
	private static final String HTTPPATH = Url.URLa+"SecretaryServlet";
	private static final String INSERT= Url.URLa+"InsertSecretaryServlet";
	private String meeting,workName;
	private Timer timer=new Timer();

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			    String str = msg.obj.toString();
			    Log.e("**********",str);
				Gson gson = new Gson();
				messages= gson.fromJson(str, new TypeToken<List<MessageEntity>>() {
				}.getType());
				for(MessageEntity message:messages){
					txt.setText(message.getMeeting());
					if(message.getRole().equals("主管") ){
						message.setMsgType(false);
					}else{
						message.setMsgType(true);
					}
				}
				adapter = new MsgViewAdapter(CallSecretaryActivity.this, messages);
				chatlist.setAdapter(adapter);
				chatlist.setSelection(adapter.getCount() - 1);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_communicate);
		initView();
	}

	private void initView() {
		back = (Button) findViewById(R.id.btn_back);
		send = (Button) findViewById(R.id.btn_send);
		chatlist = (ListView) findViewById(R.id.listview);
		input = (EditText) findViewById(R.id.et_sendmessage);
		txt=(TextView) findViewById(R.id.txt_meeting);
		back.setOnClickListener(this);
		send.setOnClickListener(this);
		Intent intent=getIntent();
		meeting =intent.getStringExtra("meetingId");
		workName=intent.getStringExtra( "workName" );
	}

	@Override
	protected void onStart(){
		super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                try {
                    message.obj = HttpUtils.httpURLConnectionPostData(HTTPPATH,
                            meeting, "utf-8", "text/html");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendMessage(message);
            }
        }).start();
        circle();
	}

	public void circle(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                try {
                    message.obj = HttpUtils.httpURLConnectionPostData(HTTPPATH,
                            meeting, "utf-8", "text/html");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendMessage(message);
            }
        },5000,5000);
    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			Toast.makeText(this, "返回按钮", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case R.id.btn_send:
			sendMsg();
			break;

		}

	}

	private void sendMsg() {
		String content = input.getText().toString();
		if (content.length() > 0) {
			final MessageEntity messageEntity= new MessageEntity(meeting,workName,gainTime(),content);
			messageEntity.setMsgType(false);
			messages.add(messageEntity);
			adapter.notifyDataSetChanged();
			input.setText("");
			chatlist.setSelection(chatlist.getCount() - 1);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Gson gson=new Gson();
					String jsonstr=gson.toJson(messageEntity);
					Log.e("**********",jsonstr);
					try {
						HttpUtils.httpURLConnectionPostData(INSERT,
								jsonstr, "utf-8", "text/html");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

	}

	@SuppressLint("SimpleDateFormat") 
	private String gainTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	@Override
	protected void onStop() {
		super.onStop();
		timer.cancel();
	}
}