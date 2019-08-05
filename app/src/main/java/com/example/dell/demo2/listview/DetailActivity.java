package com.example.dell.demo2.listview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.ContentEntity;

public class DetailActivity extends Activity{
	
	private TextView textName,textDate,textContent;
	private Button btnCheck;
	private String id,myRole;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		textName= (TextView) findViewById(R.id.text_name);
		textDate = (TextView) findViewById(R.id.text_date);
		textContent= (TextView) findViewById(R.id.text_content);
		btnCheck=findViewById( R.id.btn_check );
		Intent i=getIntent();
		final ContentEntity content=(ContentEntity) i.getSerializableExtra("content");
		myRole=getIntent().getStringExtra( "role" );
		id=getIntent().getStringExtra( "meetingId" );
		textName.setText(content.getMeeting());
		textDate.setText(content.getDate());
		textContent.setText(content.getContent());
		btnCheck.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(myRole.equals( "主管" )){
					Intent i=new Intent(DetailActivity.this,CheckActivity.class);
					i.putExtra("content",content);
					i.putExtra( "role",myRole );
					startActivity(i);

				}else{
					AlertDialog.Builder builder=new AlertDialog.Builder(DetailActivity.this);
					builder.setMessage("是否交给主管审批？");
					builder.setPositiveButton("是",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});

					builder.setNegativeButton("否",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i=new Intent(DetailActivity.this,CheckActivity.class);
							i.putExtra( "meetingId",id );
							i.putExtra("content",content);
							i.putExtra( "role",myRole );
							startActivity(i);
						}
					});
					builder.create().show();

				}

			}
		} );
	}

}
