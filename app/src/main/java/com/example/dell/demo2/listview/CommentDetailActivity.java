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
import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.CommentEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.web_url_help.Url;


public class CommentDetailActivity extends Activity{

	private RatingBar rb;
	private EditText edtRead;
	private CommentEntity comment;
	private Button btn,btnNo;
	private String mid;
	private static final String HTTPPATH = Url.URLa+"AgreeCommentServlet";
	private static final String httppath = Url.URLa+"DisAgreeCommentServlet";

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String str = msg.obj.toString();
			AlertDialog.Builder builder=new AlertDialog.Builder(CommentDetailActivity.this);
			Log.e("******",str);
			if(str.equals("true")){
				builder.setMessage("成功提交");
			}else{
				builder.setMessage("提交失败");
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
		setContentView(R.layout.activity_rating);

		rb = (RatingBar) findViewById(R.id.order_rb);
		edtRead = (EditText) findViewById(R.id.edt_order_write);
		btn=findViewById( R.id.btn_fabiao );
		btnNo=findViewById( R.id.btn_no );

		Intent intent = getIntent();
		comment = (CommentEntity) intent.getSerializableExtra("comment");
		mid=intent.getStringExtra("meetingId");

		rb.setRating(comment.getRating());
        edtRead.setText( comment.getComment() );
		edtRead.setEnabled(false);
		rb.setIsIndicator(true);
		btn.setText( "同意发布" );

		// 同意发布
		btn.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder=new AlertDialog.Builder(CommentDetailActivity.this);
				builder.setMessage("您确定要发布吗？");
				builder.setPositiveButton("是",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								Message message = handler.obtainMessage();
								try {
									String str="pid="+comment.getPid()+"&mid="+mid;
									Log.e("******",str);
									message.obj = HttpUtils.httpURLConnectionGetData(HTTPPATH,str, "utf-8", "text/html");
								} catch (Exception e) {
									e.printStackTrace();
								}
								handler.sendMessage(message);
							}
						}).start();
					}
				});
				builder.setNegativeButton("否",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.create().show();

			}
		} );

		// 驳回
		btnNo.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder=new AlertDialog.Builder(CommentDetailActivity.this);
				builder.setMessage("您确定要驳回吗？");
				builder.setPositiveButton("是",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								Message message = handler.obtainMessage();
								try {
									String str="pid="+comment.getPid()+"&mid="+mid;
									message.obj = HttpUtils.httpURLConnectionGetData(httppath,str, "utf-8", "text/html");
								} catch (Exception e) {
									e.printStackTrace();
								}
								handler.sendMessage(message);
							}
						}).start();
					}
				});
				builder.setNegativeButton("否",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.create().show();

			}
		} );


	}

}
