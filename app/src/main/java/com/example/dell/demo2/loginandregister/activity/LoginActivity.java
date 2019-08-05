package com.example.dell.demo2.loginandregister.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loginandregister.web.LoginPostService;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingPart_MainActivity;
import com.example.dell.demo2.loginsucce_participantpart.ParticipantMainActivity;
import com.example.dell.demo2.supervisor.OkHttpUtils;
import com.example.dell.demo2.web_url_help.Url;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    static int LOGIN_FAILED = 0;
    static int LOGIN_SUCCEEDED = 1;
    static int CONNECT_FAILED = 444;

    private Button btnForget, btnRegister, btnLogin;
    private EditText etMobile, etPsw;
    private RadioGroup rgIdentity;
    private String identity = "";

    private ProgressDialog progress;
    // 返回的数据
    private String info;
    // 返回主线程更新数据
    public Handler handler;
    private String responseJsonStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


    }

    public void init() {


        btnForget = findViewById(R.id.btn_forget);
        btnRegister = findViewById(R.id.btn_regis);
        btnLogin = findViewById(R.id.btn_login);
        etMobile = findViewById(R.id.et_loginphone);
        etPsw = findViewById(R.id.et_loginpassword);
       rgIdentity = findViewById(R.id.rg_identity);


        btnLogin.setOnClickListener(LoginActivity.this);
        btnForget.setOnClickListener(LoginActivity.this);
        btnRegister.setOnClickListener(LoginActivity.this);

        rgIdentity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rg_work) {
                    identity = "工作人员";
                } else if (i == R.id.rg_attend) {
                    identity = "参会人员";
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forget:
                Intent i1 = new Intent(LoginActivity.this, ResetActivity.class);
                startActivity(i1);
                break;
            case R.id.btn_regis:
                Intent i2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i2);
                break;
            case R.id.btn_login:
            {

                //String responseJsonStr;

                Toast.makeText(LoginActivity.this,responseJsonStr,Toast.LENGTH_SHORT);
                login();
            }

                break;
        }
    }


    private void login() {
        String mobilePhoneNumber = etMobile.getText().toString();
        String password = etPsw.getText().toString();
        // 检测网络
        if (!checkNetwork()) {
            Toast toast = Toast.makeText(LoginActivity.this, "网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if (TextUtils.isEmpty(mobilePhoneNumber)) {
            Toast.makeText(LoginActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isPoneAvailable(mobilePhoneNumber.trim())) {
            Toast.makeText(LoginActivity.this, "手机号码格式错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("正在登录中...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        // 创建子线程，分别进行Get和Post传输
        new LoginPostThread(mobilePhoneNumber,
                password).start();
        //Handle,Msg返回成功信息，跳转到其他Activity
        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progress.dismiss();
                if (msg.what == 111) {  // 处理发送线程传回的消息
                    if (msg.obj.toString().equals("SUCCEEDED")) {
                        if (identity.equals("工作人员")) {
                            //跳转


                            Toast.makeText(LoginActivity.this, "欢迎您：" + identity, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MeetingPart_MainActivity.class);
                            intent.putExtra( "phone",etMobile.getText().toString());
                            //intent.putExtra("str",str);
                            startActivity(intent);
                        } else if(identity.equals("参会人员")){
                            Toast.makeText(LoginActivity.this, "欢迎您：" + identity, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, ParticipantMainActivity.class);
                            intent.putExtra( "phone",etMobile.getText().toString());
                           // intent.putExtra("str",str);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "请选择您的身份！", Toast.LENGTH_SHORT).show();
                        }


                    } else if (msg.obj.toString().equals("CONNECT_FAILED")) {
                        Toast.makeText(LoginActivity.this, "连接服务器失败！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "账号和密码不匹配", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

    }


    //登录Thread调用LoginPostService，返回Msg
    public class LoginPostThread extends Thread {
        public String mobilephone, password;

        public LoginPostThread(String mobilephone, String password) {
            this.mobilephone = mobilephone;
            this.password = password;
        }

        @Override
        public void run() {
            // Sevice传回int
            int responseInt = 0;
            if (!mobilephone.equals("")) {
                // 要发送的数据
                // 发送数据，获取对象
                responseInt = LoginPostService.send(mobilephone, password);
                Log.i("tag", "LoginActivity: responseInt = " + responseInt);
                // 准备发送消息
                Message msg = handler.obtainMessage();
                // 设置消息默认值
                msg.what = 111;
                // 服务器返回信息的判断和处理
                if (responseInt == LOGIN_FAILED) {
                    msg.obj = "FAILED";
                } else if (responseInt == LOGIN_SUCCEEDED) {
                    msg.obj = "SUCCEEDED";
                } else if (responseInt == CONNECT_FAILED) {
                    msg.obj = "CONNECT_FAILED";
                }
                handler.sendMessage(msg);
            }
        }

    }


    // 检测Wifi和网络
    private boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) LoginActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    private boolean isPoneAvailable(String number) {
        String myreg = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (!number.matches(myreg)) {
            System.out.println("看是不是我校验：" + myreg.matches("18811738367"));
            return false;
        } else {
            return true;
        }
    }

}