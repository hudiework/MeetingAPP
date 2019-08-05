package com.example.dell.demo2.listview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.ContentEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.web_url_help.Url;

public class MyDetailActivity extends Activity{
	
	private TextView textName,textDate,textContent,textPeople;
	private Button btnCheck;
	private static final String HTTPPATH = Url.URLa+"CountServlet";
	private int cid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		textName= (TextView) findViewById(R.id.text_name);
		textDate = (TextView) findViewById(R.id.text_date);
		textContent= (TextView) findViewById(R.id.text_content);
		textPeople=findViewById( R.id.text_people );
		btnCheck=findViewById( R.id.btn_check );
		Intent i=getIntent();
		ContentEntity content=(ContentEntity) i.getSerializableExtra("content");
		textName.setText(content.getMeeting());
		textDate.setText(content.getDate());
		textContent.setText(content.getContent());
		if(content.getTag().equals("1")){
			textPeople.setText( "通知对象:全部的参会人员");
		}else if(content.getTag().equals("2")){
			textPeople.setText( "通知对象:全部的工作人员");
		}else{
			textPeople.setText( "通知对象:全部的工作人员和参会人员");
		}
		cid=content.getId();
		btnCheck.setText( "退出" );
		btnCheck.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
             finish();
			}
		} );
	}

	@Override
	protected void onStart() {
		super.onStart();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String str="cid="+cid+"&rid="+"4";
				try {
					 HttpUtils.httpURLConnectionGetData(HTTPPATH, str, "utf-8", "text/html");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
