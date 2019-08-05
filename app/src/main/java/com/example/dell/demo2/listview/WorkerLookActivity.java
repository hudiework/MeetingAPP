package com.example.dell.demo2.listview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.ContentEntity;

public class WorkerLookActivity extends Activity{

	private TextView textName,textDate,textContent,textStatus,textCheck;
	private Button btnCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_detail);
		textName= (TextView) findViewById(R.id.text_name);
		textDate = (TextView) findViewById(R.id.text_date);
		textContent= (TextView) findViewById(R.id.text_content);
        textStatus=findViewById(R.id.text_status);
        textCheck=findViewById(R.id.check_message);
		Intent i=getIntent();
		ContentEntity content=(ContentEntity) i.getSerializableExtra("content");
		textName.setText(content.getMeeting());
		textDate.setText(content.getDate());
		textContent.setText(content.getContent());
		textCheck.setText("审核信息：" +content.getCheckMessage());
		if(content.getCheckMessage()==null  || content.getCheckMessage().equals( "" )){
			textStatus.setText("未审核");
		}else{
			textStatus.setText("审核驳回");
		}

	}

}