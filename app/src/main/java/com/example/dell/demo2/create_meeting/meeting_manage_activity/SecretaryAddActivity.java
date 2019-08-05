package com.example.dell.demo2.create_meeting.meeting_manage_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.create_meeting.connect.MyThread;
import com.example.dell.demo2.supervisor.beans.WorkerBean;

import java.io.Serializable;
import java.util.HashSet;

public class SecretaryAddActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etPhone, etName, etCompany;
    private Button btnCall;
    private Intent intent;
    private MyThread myThread;
    private MyHandler myHandler;
    private WorkerBean secretary;
    private HashSet<WorkerBean> secretarySet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_add);

        myHandler = new MyHandler();
        intent = getIntent();
        etPhone = findViewById(R.id.et_phone);
        etName = findViewById(R.id.et_name);
        etCompany = findViewById(R.id.et_company);
        btnCall = findViewById(R.id.btn_call);
        btnCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
            String phone = etPhone.getText().toString();
            if(!isPoneAvailable(phone)){
                Toast.makeText(SecretaryAddActivity.this, "手机号码格式错误！请重新输入！", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle bundle = getIntent().getExtras();
            secretarySet = (HashSet<WorkerBean>) bundle.getSerializable("secretarySet");
            System.out.println("看看看有没有传过来："+secretarySet.size());
            for(WorkerBean s: secretarySet ) {
                if(s.getWorkerMobilePhone().equals(phone))
                {
                    Toast.makeText(SecretaryAddActivity.this, "此秘书已添加过！请再选择其他人！", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            myThread = new MyThread(phone, myHandler, "CheckPhone");
            myThread.start();
        } catch (NumberFormatException e) {
            Toast.makeText(SecretaryAddActivity.this, "请输入正确格式",
                    Toast.LENGTH_SHORT).show();
        }
    }

    class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.obj.toString();
            System.out.println("httpResponseString33-->" + "/" + data);
            if ("".equals(data)) {
                Toast.makeText(SecretaryAddActivity.this, "数据错误，请重试",
                        Toast.LENGTH_SHORT).show();

            } else {
                if ("0".equals(data)) Toast.makeText(SecretaryAddActivity.this, "该用户尚未注册",
                        Toast.LENGTH_SHORT).show();
                else {
                    /*if ("1".equals(data))
                        secretary = new WorkerBean(etName.getText().toString(), etCompany.getText().toString(), etPhone.getText().toString());
                    else {
                        secretary = new Gson().fromJson(data, WorkerBean.class);
                        Toast.makeText(SecretaryAddActivity.this, "提交成功",
                                Toast.LENGTH_SHORT).show();
                    }*/
                    Bundle bundle = new Bundle();
                    secretary = new WorkerBean(etName.getText().toString(), etCompany.getText().toString(), etPhone.getText().toString());
                    bundle.putString("secretaryName", etName.getText().toString());
                    System.out.println("SecertaryAdd頁面"+etName.getText().toString());
                    bundle.putSerializable("secretary", (Serializable) secretary);
                    intent.putExtras(bundle);
                    setResult(2, intent);
                    Toast.makeText(SecretaryAddActivity.this, "提交成功",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private boolean isPoneAvailable(String number) {
        String myreg = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (!number.matches(myreg)) {
            return false;
        } else {
            return true;
        }
    }
}
