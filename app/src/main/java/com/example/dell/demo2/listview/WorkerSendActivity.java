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
import android.widget.EditText;
import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.ContentEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkerSendActivity extends Activity implements OnClickListener {

	private EditText edtWrite;
	private Button btnSave, btnAbandon;
	private String id,date;
	private ContentEntity contentEnitity;
	private static final String HTTPPATH = Url.URLa+"ContentServlet";

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String str = msg.obj.toString();
			Builder builder=new Builder(WorkerSendActivity.this);
			Log.e("******",str);
			if(str.equals("true")){
				builder.setMessage("提交成功！");
			}else{
				builder.setMessage("失败");
			}
			builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					finish();
				}
			});
			builder.create().show();


		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);
		btnSave = (Button) findViewById(R.id.btn_save);
		btnAbandon = (Button) findViewById(R.id.btn_abandon);
		edtWrite=(EditText) findViewById(R.id.edt_write);
		btnSave.setOnClickListener(this);
		btnAbandon.setOnClickListener(this);
		Intent intent=getIntent();
		id=intent.getStringExtra("meetingId");
		btnSave.setText("提交给秘书");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_save:
				String text=edtWrite.getText().toString();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date=format.format(new Date());
				// 标记0为待审批事项
				contentEnitity = new ContentEntity(id,text,date,"0");
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
				break;
			case R.id.btn_abandon:
				edtWrite.setText("");
				break;
		}
	}

}
