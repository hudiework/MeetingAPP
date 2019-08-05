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
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.ContentEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;

public class CheckActivity extends Activity implements OnClickListener {

	private EditText edtWrite;
	private Button btnSave, btnAbandon;
	private ContentEntity contentEnitity;
	private String myRole,id;
	private static final String HTTPPATH = Url.URLa+"CheckServlet";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);
		btnSave = (Button) findViewById(R.id.btn_save);
		btnAbandon = (Button) findViewById(R.id.btn_abandon);
		edtWrite=(EditText) findViewById(R.id.edt_write);
		btnSave.setOnClickListener(this);
		btnAbandon.setOnClickListener(this);
		btnSave.setText("提交审批信息");
		btnAbandon.setText("驳回");
		contentEnitity= (ContentEntity) getIntent().getSerializableExtra( "content" );
        myRole=getIntent().getStringExtra( "role" );
        id=getIntent().getStringExtra( "meetingId" );
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_save:
				String text=edtWrite.getText().toString();
				contentEnitity.setCheckMessage(text);
				new Thread(new Runnable() {
					@Override
					public void run() {
						Gson gson = new Gson();
						final String jsonstr = gson.toJson(contentEnitity);
						try {
							Log.e("********",jsonstr);
							HttpUtils.httpURLConnectionPostData(HTTPPATH,
									jsonstr, "utf-8", "text/html");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
				Intent intent=new Intent(CheckActivity.this,SendActivity.class);
				intent.putExtra("content",contentEnitity);
				intent.putExtra("role",myRole);
				intent.putExtra( "meetingId",id);
				startActivity(intent);
				break;
			case R.id.btn_abandon:
				Builder builder=new Builder(CheckActivity.this);
				builder.setMessage("您确定要驳回吗？");
				builder.setPositiveButton("是",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						String text=edtWrite.getText().toString();
						contentEnitity.setCheckMessage(text);
						new Thread(new Runnable() {
							@Override
							public void run() {
								Gson gson = new Gson();
								final String jsonstr = gson.toJson(contentEnitity);
								try {
									Log.e("********",jsonstr);
									HttpUtils.httpURLConnectionPostData(HTTPPATH,
											jsonstr, "utf-8", "text/html");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).start();
						finish();
					}
				});
				builder.setNegativeButton("否",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.create().show();
				break;
		}
	}

}
