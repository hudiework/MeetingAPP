package com.example.dell.demo2.loginsucce_participantpart.PayAndBill;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loginsucce_participantpart.entity.ParticipantBean;

import java.util.ArrayList;
import java.util.List;

public class ParticipantDetialActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_signUp, btn_signCancel;
    private EditText et_participantName, et_participantTel, et_participantCompany, et_participantPosition;
    private Spinner sp_demaind, sp_childMeeting;
    private TextView tv_childMeeting, tv_demaind;
    private Dialog loadingDialog;//支付中...动画效果对话框
    private Button btn_create_bill;//生成发票按钮

    private ParticipantBean participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_detial);
        init();
        btn_signUp.setOnClickListener(this);
        btn_signCancel.setOnClickListener(this);
        btn_create_bill.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signCancel: {
                //返回上一个界面
            }
            break;
            case R.id.btn_signUp: {
                String name = et_participantName.getText().toString();
                String tel = et_participantTel.getText().toString();
                String company = et_participantCompany.getText().toString();
                String position = et_participantPosition.getText().toString();
                String demaind = tv_demaind.getText().toString();
                String childMeeting = tv_childMeeting.getText().toString();

                if (name.equals("") || name == null || tel.equals("") || tel == null || company.equals("") || company == null) {
                    Toast.makeText(ParticipantDetialActivity.this, "信息填写不完整！", Toast.LENGTH_SHORT).show();
                } else if (demaind == null || demaind.equals("")) {
                    Toast.makeText(ParticipantDetialActivity.this, "请选择您的住宿要求！", Toast.LENGTH_SHORT).show();
                } else {
                    participant = new ParticipantBean(tel, 1, name, position, company, 1, demaind, 0);
                    showDialog("会议需要支付：10元", "去支付", "取消", toPay, notPay);
                }
            }
            break;
            case R.id.btn_create_bill: {//点击生成发票
                Intent i = new Intent(ParticipantDetialActivity.this, CreateBillMainActivity.class);
                startActivity(i);

            }
            break;
        }
    }

    public void init() {
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_signCancel = findViewById(R.id.btn_signCancel);
        et_participantName = findViewById(R.id.et_participantName);
        et_participantTel = findViewById(R.id.et_participantTel);
        et_participantCompany = findViewById(R.id.et_participantCompany);
        et_participantPosition = findViewById(R.id.et_participantPosition);

        sp_demaind = findViewById(R.id.sp_demaind);
        sp_childMeeting = findViewById(R.id.sp_childMeeting);
        tv_childMeeting = findViewById(R.id.tv_childMeeting);
        tv_demaind = findViewById(R.id.tv_demaind);
        tv_childMeeting.setVisibility(View.GONE);//默认还未显示，用户选择spanner才会显示
        tv_demaind.setVisibility(View.GONE);
        /**
         * 为spanner设置数据源
         */
        List<String> demaindList = new ArrayList<String>();
        demaindList.add("不住宿");
        demaindList.add("标间合住");
        demaindList.add("单住标准间");
        demaindList.add("单住大床房");
        //新建arrayAdapter,android.R.layout.simple_spinner_item是调用android studio中默认的样式
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, demaindList);
        //adapter设置一个下拉列表
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner加载适配器
        sp_demaind.setAdapter(adapter);
        //实现监听事件
        sp_demaind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String demaind = (String) adapter.getItem(position);
                tv_demaind.setText(demaind);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tv_demaind.setText("不住宿");
            }
        });

        btn_create_bill = findViewById(R.id.btn_create_bill);
        btn_create_bill.setVisibility(View.GONE);
    }

    public void showDialog(String msg, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener yes, DialogInterface.OnClickListener no) {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage(msg);
        alertdialogbuilder.setPositiveButton(positiveButtonText, yes);
        alertdialogbuilder.setNegativeButton(negativeButtonText, no);
        AlertDialog alertdialog = alertdialogbuilder.create();
        alertdialog.show();
    }

    private DialogInterface.OnClickListener toPay = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            loadingDialog = LoadingDialogUtils.initWaitDialog(ParticipantDetialActivity.this, "支付中...", false, true);
            //LoadingDialogUtils.startDialog(loadingDialog);
            PayDialog payDialog = new PayDialog(ParticipantDetialActivity.this, loadingDialog, btn_create_bill, btn_signUp, participant);
            payDialog.show();

        }
    };
    private DialogInterface.OnClickListener notPay = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    };
}
