package com.example.dell.demo2.loginsucce_participantpart.PayAndBill;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loginsucce_participantpart.entity.ParticipantBean;
import com.example.dell.demo2.supervisor.OkHttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.example.dell.demo2.worker.ScheduleHotelActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/25.
 */

public class PayDialog extends BaseDialog{
    private PayPwdEditText payPwdEditText;
    private Dialog loadingDialog;
    private Button btnCreateBill,btnSignUp;
    private ParticipantBean participant;
    private Gson gson;
    private String participantStr=null;

    public PayDialog(Context context, Dialog loadingDialog, Button btnCreateBill,Button signUp,ParticipantBean participant) {
        super(context);
        this.loadingDialog=loadingDialog;
        this.btnSignUp=signUp;
        this.btnCreateBill=btnCreateBill;
        this.participant=participant;
        this.gson=new Gson();
        this.participantStr=gson.toJson(participant);//把对象转为json字符串
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    LoadingDialogUtils.closeDialog(loadingDialog);
                    Toast.makeText(context,"支付成功！",Toast.LENGTH_SHORT).show();
                    String url= Url.URLa+"ParticipantSignUpServlet";
                    Map<String,String> map=new HashMap<String,String >();
                    map.put("participant",participantStr);
                    OkHttpUtils.post(url, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("为啥会错呢。。。");
                            Log.e("错误来了：", "执行错误回调！！！ ", e);
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseJsonStr = response.body().string();
                            System.out.println("获取到报名返回信息"+responseJsonStr);
                            System.out.println("获取到报名返回信息"+responseJsonStr);
                            System.out.println("获取到报名返回信息"+responseJsonStr);
                            Message msg = new Message();
                            msg.what = 1;
                            Bundle b=new Bundle();
                            b.putString("responseJsonStr",responseJsonStr);
                            msg.setData(b);
                            signUpMagHandler.sendMessage(msg);
                        }
                    } ,map);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_pay_password_dialog_layout);
        payPwdEditText = (PayPwdEditText) findViewById(R.id.ppet);

        payPwdEditText.initStyle(R.drawable.edit_num_bg_red, 6, 0.33f, R.color.colorAccent, R.color.colorAccent, 30);
        payPwdEditText.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {//密码输入完后的回调
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

                LoadingDialogUtils.startDialog(loadingDialog);
                mHandler.sendEmptyMessageDelayed(1, 2000);//2秒之后消失
                dismiss();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                payPwdEditText.setFocus();
            }
        }, 100);

    }

    private Handler signUpMagHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String jsonStr=msg.getData().getString("responseJsonStr");
            if(msg.what==1&&jsonStr!=null){
                if(jsonStr.equals("0")){
                    Toast.makeText(context,"报名失败，请点击重试",Toast.LENGTH_SHORT).show();
                }else{
                    btnCreateBill.setVisibility(View.VISIBLE);//报名成功，可打印发票
                    btnSignUp.setText("已报名");
                    btnSignUp.setEnabled(false);
                }
            }
        }
    };
}
