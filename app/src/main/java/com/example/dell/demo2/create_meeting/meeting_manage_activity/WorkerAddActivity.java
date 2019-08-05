package com.example.dell.demo2.create_meeting.meeting_manage_activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WorkerAddActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etPhone, etName, etCompany;
    private Button btnCall, btnHotel, btnPlace, btnCar, btnSign;
    private Intent intent;
    private MyThread myThread;
    private MyHandler myHandler;
    private WorkerBean worker;
    private HashSet<WorkerBean> workerSet;
    private List<String> functionList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_add);
        init();
    }

    public void init() {
        myHandler = new MyHandler();
        intent = getIntent();
        etPhone = findViewById(R.id.et_phone);
        etName = findViewById(R.id.et_name);
        etCompany = findViewById(R.id.et_company);
        btnCall = findViewById(R.id.btn_call);
        btnHotel = findViewById(R.id.btn_choose_hotel_reserve);
        btnPlace = findViewById(R.id.btn_choose_place_reserve);
        btnCar = findViewById(R.id.btn_choose_car_rent);
        btnSign = findViewById(R.id.btn_choose_sign);

        btnCall.setOnClickListener(this);
        btnHotel.setOnClickListener(this);
        btnPlace.setOnClickListener(this);
        btnCar.setOnClickListener(this);
        btnSign.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_choose_hotel_reserve:
                ColorDrawable buttonColor1 = (ColorDrawable) btnHotel.getBackground();
                int colorId1 = buttonColor1.getColor();
                System.out.println("颜色id"+colorId1);
                if(colorId1 ==-7434610){
                    btnHotel.setBackgroundColor(Color.parseColor("#FF6A6A"));
                }else{
                    btnHotel.setBackgroundColor(Color.parseColor("#8E8E8E"));
                }
                break;
            case R.id.btn_choose_place_reserve:
                ColorDrawable buttonColor2 = (ColorDrawable) btnPlace.getBackground();
                int colorId2 = buttonColor2.getColor();
                if(colorId2 ==-7434610){
                    btnPlace.setBackgroundColor(Color.parseColor("#FF6A6A"));
                }else{
                    btnPlace.setBackgroundColor(Color.parseColor("#8E8E8E"));
                }
                break;
            case R.id.btn_choose_car_rent:
                ColorDrawable buttonColor3 = (ColorDrawable) btnCar.getBackground();
                int colorId3 = buttonColor3.getColor();
                System.out.println("颜色id"+colorId3);
                if(colorId3 ==-7434610){
                    btnCar.setBackgroundColor(Color.parseColor("#FF6A6A"));
                }else{
                    btnCar.setBackgroundColor(Color.parseColor("#8E8E8E"));
                }
                break;
            case R.id.btn_choose_sign:
                ColorDrawable buttonColor4 = (ColorDrawable) btnSign.getBackground();
                int colorId4 = buttonColor4.getColor();
                System.out.println("颜色id"+colorId4);
                if(colorId4 ==-7434610){
                    btnSign.setBackgroundColor(Color.parseColor("#FF6A6A"));
                }else{
                    btnSign.setBackgroundColor(Color.parseColor("#8E8E8E"));
                }
                break;


            case R.id.btn_call:
                try {
                    String phone = etPhone.getText().toString();
                    if (!isPoneAvailable(phone)) {
                        Toast.makeText(WorkerAddActivity.this, "手机号码格式错误！请重新输入！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Bundle bundle = getIntent().getExtras();
                    workerSet = (HashSet<WorkerBean>) bundle.getSerializable("workerSet");
                    System.out.println("看看看有没有传过来：" + workerSet.size());
                    for (WorkerBean w : workerSet) {
                        if (w.getWorkerMobilePhone().equals(phone)) {
                            Toast.makeText(WorkerAddActivity.this, "此工作人员已添加过！请再选择其他人！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    myThread = new MyThread(phone, myHandler, "CheckPhone");
                    myThread.start();
                } catch (NumberFormatException e) {
                    Toast.makeText(WorkerAddActivity.this, "请输入正确的手机号",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.obj.toString();
            System.out.println("httpResponseString33-->" + "/" + data);
            if ("".equals(data)) {
                Toast.makeText(WorkerAddActivity.this, "连接失败，请重试",
                        Toast.LENGTH_SHORT).show();
            } else {
                if ("0".equals(data))
                    Toast.makeText(WorkerAddActivity.this, "该用户尚未注册",
                            Toast.LENGTH_SHORT).show();
                else {
                    /*if ("1".equals(data))
                        worker = new WorkerBean(etName.getText().toString(), etCompany.getText().toString(), etPhone.getText().toString());
                    else {
                        worker = new Gson().fromJson(data, WorkerBean.class);
                        Toast.makeText(WorkerAddActivity.this, "提交成功",
                                Toast.LENGTH_SHORT).show();
                    }*/
                    worker = new WorkerBean(etName.getText().toString(), etCompany.getText().toString(), etPhone.getText().toString());
                    addPermit(btnHotel,worker);
                    addPermit(btnCar,worker);
                    addPermit(btnPlace,worker);
                    addPermit(btnSign,worker);
                    worker.setFunctionList(functionList);

                    Bundle bundle = new Bundle();
                    bundle.putString("workerName", etName.getText().toString());
                    System.out.println("workerAdd頁面"+etName.getText().toString());
                    bundle.putSerializable("worker", (Serializable) worker);
                    intent.putExtras(bundle);
                    setResult(3, intent);
                    Toast.makeText(WorkerAddActivity.this, "提交成功",
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


    //添加权限抽取方法
    public void addPermit(View view,WorkerBean worker){
        ColorDrawable buttonColor = (ColorDrawable) view.getBackground();
        int colorId = buttonColor.getColor();
        if(colorId == -38294){
            System.out.println("button的Tag:"+view.getTag());
            functionList.add((String) view.getTag());
        }
    }


}
