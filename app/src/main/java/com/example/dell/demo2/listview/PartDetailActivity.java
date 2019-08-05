package com.example.dell.demo2.listview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.CommentEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.web_url_help.Url;


public class PartDetailActivity extends Activity{

	private RatingBar rb;
	private EditText edtRead;
	private CommentEntity comment;
	private Button btn,btnNo;
	private String mid;
	private static int count;
	private TextView txtCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating);

		rb = (RatingBar) findViewById(R.id.order_rb);
		edtRead = (EditText) findViewById(R.id.edt_order_write);
		btn=findViewById( R.id.btn_fabiao );
		btnNo=findViewById( R.id.btn_no );
		txtCount=findViewById( R.id.tv_count);

		Intent intent = getIntent();
		comment = (CommentEntity) intent.getSerializableExtra("comment");
		mid=intent.getStringExtra("meetingId");

		rb.setRating(comment.getRating());
        edtRead.setText( comment.getComment() );
		edtRead.setEnabled(false);
		rb.setIsIndicator(true);
		btn.setText("点赞");
		btnNo.setText("退出");
		txtCount.setText(count+"");

		btn.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				count++;
				txtCount.setText(count+"");
			}
		} );

		btnNo.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		} );


	}

}
