package com.example.dell.demo2.loginandregister.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.dell.demo2.R;
import com.example.dell.demo2.loginandregister.web.ResetPostService;
import com.example.dell.demo2.web_url_help.Url;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;


public class ResetActivity extends AppCompatActivity implements View.OnClickListener{

    static int RESET_FAILED = 4;
    static int RESET_SUCCEEDED = 5;

    private Button btnReset1,btnReset2,btnSend2,btnReset;
    private EditText et_resetphone,et_code2,et_newpsw;
    MyCountTimer timer;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        // 设置背景图片透明度：
       /* View tableview = findViewById(R.id.reset_background);
        tableview.getBackground().setAlpha(200);
*/
        //第一：默认初始化
        BmobSMS.initialize(ResetActivity.this, "d7fa04244a2d2b53a6cfc979979ec1f2");

        btnReset1 = findViewById(R.id.btn_reset1);
        btnReset2 = findViewById(R.id.btn_reset2);
        btnSend2 = findViewById(R.id.btn_send2);
        btnReset = findViewById(R.id.btn_reset);

        et_code2 = findViewById(R.id.et_verify_code2);
        et_resetphone = findViewById(R.id.et_resetphone);
        et_newpsw = findViewById(R.id.et_newpwd);

        btnReset1.setOnClickListener(ResetActivity.this);
        btnReset2.setOnClickListener(ResetActivity.this);
        btnReset.setOnClickListener(ResetActivity.this);
        btnSend2.setOnClickListener(ResetActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reset1:
                Intent i1 = new Intent(ResetActivity.this,LoginActivity.class);
                startActivity(i1);
                break;
            case R.id.btn_reset2:
                Intent i2 = new Intent(ResetActivity.this,RegisterActivity.class);
                startActivity(i2);
                break;
            case R.id.btn_send2:
                requestSMSCode();
                break;
            case R.id.btn_reset:
                String code = et_code2.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(ResetActivity.this, "验证码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    resetPwd(code);
                }
                break;

        }
    }


    private void requestSMSCode() {
        String number = et_resetphone.getText().toString();
        if (!TextUtils.isEmpty(number)) {
            timer = new MyCountTimer(60000, 1000);
            timer.start();
            BmobSMS.requestSMSCode(ResetActivity.this,number, "重置密码模板", new RequestSMSCodeListener() {
                @Override
                public void done(Integer smsId, BmobException ex) {
                    // TODO Auto-generated method stub
                    if (ex == null) {// 验证码发送成功
                        // 用于查询本次短信发送详情
                        Toast.makeText(ResetActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    } else {//如果验证码发送错误，可停止计时
                        timer.cancel();
                    }
                }
            });
        } else {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     */
    private void resetPwd(final String security_code) {
        String Reset_phone = et_resetphone.getText().toString();
        String Reset_psw = et_newpsw.getText().toString();

        // 检测网络
        if (!checkNetwork()) {
            Toast toast = Toast.makeText(ResetActivity.this, "网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        if (TextUtils.isEmpty(Reset_psw )) {
            Toast.makeText(ResetActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progress = new ProgressDialog(ResetActivity.this);
        progress.setMessage("正在验证并注册中...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        BmobSMS.verifySmsCode(ResetActivity.this,Reset_phone, security_code, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    new ResetActivity.ResetPostThread(et_resetphone.getText().toString(),
                            et_newpsw.getText().toString()).start();

                    //Handle,Msg返回成功信息，跳转到其他Activity
                    handler = new Handler() {
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            progress.dismiss();
                            if (msg.what == 222) {  // 处理发送线程传回的消息
                                if (msg.obj != null) {
                                    if (msg.obj.toString().equals("SUCCEEDED")) {
                                        //跳转
                                        Toast.makeText(ResetActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(ResetActivity.this, "重置密码失败", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(ResetActivity.this, "重置密码失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    };

                } else {
                    Log.v("重置", "有没有e" + e.toString());
                    Toast.makeText(ResetActivity.this, "验证码错误" + e.toString(), Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }


            }

        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }

    class MyCountTimer extends CountDownTimer {

        public MyCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            btnSend2.setText((millisUntilFinished / 1000) +"秒后重发");
        }
        @Override
        public void onFinish() {
            btnSend2.setText("重新发送验证码");
        }
    }

    // 检测Wifi和网络
    private boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ResetActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    //注册Thread调用ResetPostService，返回Msg
    public class ResetPostThread extends Thread {
        public String mobilephone, password;

        public ResetPostThread(String mobilephone, String password) {
            this.mobilephone = mobilephone;
            this.password = password;
        }

        @Override
        public void run() {
            // Sevice传回int
            int responseInt = 0;
            if (!mobilephone.equals("")) {
                // 要发送的数据
              /*  List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("mobilephone", mobilephone));
                params.add(new BasicNameValuePair("password", password));*/
                // 发送数据，获取对象
                String url = Url.URLa+"ResetServlet";
                responseInt = ResetPostService.send(url,mobilephone,password);
                Log.i("tag", "ResetActivity: responseInt = " + responseInt);
                // 准备发送消息
                Message msg = handler.obtainMessage();
                // 设置消息默认值
                msg.what = 222;
                // 服务器返回信息的判断和处理
                if (responseInt == RESET_FAILED) {
                    msg.obj = "FAILED";
                } else if (responseInt == RESET_SUCCEEDED) {
                    msg.obj = "SUCCEEDED";
                }
                handler.sendMessage(msg);
            }
        }
    }
}
