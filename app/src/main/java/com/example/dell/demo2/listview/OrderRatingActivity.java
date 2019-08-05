package com.example.dell.demo2.listview;

import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.CommentEntity;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class OrderRatingActivity extends Activity implements OnClickListener,
		OnRatingBarChangeListener {

	private RatingBar rb;
	private EditText edtWrite;
	private Button btn,btnNo;
	private float rating = 0;
	private static final String HTTPPATH = Url.URLa+"CommentServlet";
	private String mid,pid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating);

		rb = (RatingBar) findViewById(R.id.order_rb);
		edtWrite = (EditText) findViewById(R.id.edt_order_write);
		btn = (Button) findViewById(R.id.btn_fabiao);
		btnNo=(Button) findViewById(R.id.btn_no);
		btnNo.setText("退出");
		btn.setOnClickListener(this);
		btnNo.setOnClickListener( this );
		rb.setOnRatingBarChangeListener(this);
		mid=getIntent().getStringExtra( "meetingId");
		pid=getIntent().getStringExtra("pid");
		Log.e("**********",pid);

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String str = msg.obj.toString();
			AlertDialog.Builder builder=new AlertDialog.Builder(OrderRatingActivity.this);
			Log.e("******",str);
			if(str.equals("true")){
				builder.setMessage("发布成功,需要进行秘书审核才能查看!");
			}else{
				builder.setMessage("您已经发布了！");
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
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_fabiao:
				if (rating == 0) {
					Toast.makeText(this, "您还没有对会议进行评分！", Toast.LENGTH_SHORT).show();
				} else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							CommentEntity comment=new CommentEntity(pid,mid,rating,edtWrite.getText().toString(),"0");
							Gson gson = new Gson();
							Message message = handler.obtainMessage();
							String jsonString=gson.toJson( comment );
							try {
								message.obj = HttpUtils.httpURLConnectionPostData(
										HTTPPATH, jsonString, "UTF-8", "text/html");
								Log.e("********", message.obj.toString());
							} catch (Exception e) {
								e.printStackTrace();
							}
							handler.sendMessage(message);
						}
					}).start();
				}
				break;
			case R.id.btn_no:
				finish();
				break;
		}


	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		this.rating = rating;
	}

}
