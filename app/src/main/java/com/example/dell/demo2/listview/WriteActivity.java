package com.example.dell.demo2.listview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class WriteActivity extends Activity implements OnClickListener {
    
	private EditText edtWrite;
	private Button btnSave, btnAbandon;
	private String id,date,myRole;
	private ContentEntity contentEnitity;
	private static final String HTTPPATH = Url.URLa+"ContentServlet";

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
		myRole=intent.getStringExtra( "role" );
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			String text=edtWrite.getText().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date=format.format(new Date());
			contentEnitity = new ContentEntity(id,text,date,"0");
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
            if(myRole.equals( "主管" )){
				Intent intent=new Intent(WriteActivity.this,SendActivity.class);
				intent.putExtra("content",contentEnitity);
				intent.putExtra( "role",myRole );
				startActivity(intent);

			}else{
				AlertDialog.Builder builder=new AlertDialog.Builder(WriteActivity.this);
				builder.setMessage("是否交给主管审批？");
				builder.setPositiveButton("是",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finish();
					}
				});
				builder.setNegativeButton("否",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent=new Intent(WriteActivity.this,SendActivity.class);
						intent.putExtra("content",contentEnitity);
						intent.putExtra( "role",myRole );
						startActivity(intent);
					}
				});
				builder.create().show();

			}
			break;
		case R.id.btn_abandon:
			edtWrite.setText("");
			break;
		}
	}



}
