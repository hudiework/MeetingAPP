package com.example.dell.demo2.worker;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.supervisor.OkHttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.example.dell.demo2.worker.public_resource_bean.HotelBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ScheduleHotelActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_schedule_hotel;
   private  HotelBean hotel;
   private AmountView mAmountView;
   private int scheduleNum=1;

   //以下全是添加文本需要用的控件：
    private TextView tv_hotel_name,tv_hotel_eval,tv_hotel_type,tv_hotel_describ,
           tv_hotel_available_num,tv_hotel_price,tv_hotel_tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_hotel);
        initData();
        initCtrl();
        mAmountView.setGoods_storage(hotel.getRoomAvailableNumber());//设置选择的最大数量
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                Toast.makeText(getApplicationContext(), "Amount=>  " + amount, Toast.LENGTH_SHORT).show();
                scheduleNum=amount;
            }
        });
    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.btn_schedule_hotel:{
            String url= Url.URLa+"ScheduleHotelServlet";
            Map<String,Integer> map=new HashMap<String,Integer>();
            map.put("hotelId",hotel.getHotelId());
            map.put("scheduleNum",scheduleNum);
            map.put("AvailableNumber",hotel.getRoomAvailableNumber());
            map.put("meetingId",1);
            OkHttpUtils.postByInt(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("错误来了：", "执行错误回调！！！ ", e);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseJsonStr = response.body().string();
                    System.out.println("获取到预订酒店返回信息"+responseJsonStr);
                    System.out.println("获取到预订酒店返回信息"+responseJsonStr);
                    System.out.println("获取到预订酒店返回信息"+responseJsonStr);
                    Message msg = new Message();
                    msg.what = 1;
                    Bundle b=new Bundle();
                    b.putString("responseJsonStr",responseJsonStr);
                    msg.setData(b);
                    handler.sendMessage(msg);
                       // Toast.makeText(ScheduleHotelActivity.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                   //Toast.makeText(ScheduleHotelActivity.this,responseJsonStr,Toast.LENGTH_SHORT).show();
                }
            } ,map);
        }break;
    }
    }
    /**
     * 控件初始化
     */
    public void initCtrl(){

        btn_schedule_hotel=findViewById(R.id.btn_schedule_hotel);
        mAmountView = (AmountView) findViewById(R.id.amount_view);
        btn_schedule_hotel.setOnClickListener(this);

        tv_hotel_name=findViewById(R.id.tv_hotel_name);
        tv_hotel_eval=findViewById(R.id.tv_hotel_eval);
        tv_hotel_type=findViewById(R.id.tv_hotel_type);
        tv_hotel_describ=findViewById(R.id.tv_hotel_describ);
        tv_hotel_available_num=findViewById(R.id.tv_remain_num);
        tv_hotel_price=findViewById(R.id.tv_hotel_price);
        tv_hotel_tel=findViewById(R.id.tv_hotel_tel);

        tv_hotel_name.setText(hotel.getHotelName());
        tv_hotel_eval.setText(hotel.getHotelEval()+"");
        tv_hotel_type.setText(hotel.getRoomType());
        tv_hotel_describ.setText(hotel.getHotelDetail());
        System.out.println("可用数量有问题：：：："+hotel.getRoomAvailableNumber());
        tv_hotel_available_num.setText(hotel.getRoomAvailableNumber()+"");
        tv_hotel_price.setText(hotel.getMoney()+"/天");
        tv_hotel_tel.setText(hotel.getHotelPhoneNumber());
    }


    /**
     *初始化数据
     */
    public void initData(){
        Bundle bundle = getIntent().getExtras();
        hotel= (HotelBean) bundle.getSerializable("hotel");
    }

    /**
     * 处理服务器端传递的数据
     */

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String jsonStr=msg.getData().getString("responseJsonStr");
           if(msg.what==1&&jsonStr!=null){
                Toast.makeText(ScheduleHotelActivity.this,jsonStr,Toast.LENGTH_SHORT).show();
                if(jsonStr.equals("预订成功！！")){
                    //开启服务 查询服务器数据
                   // PollingUtils.startPollingService(ScheduleHotelActivity.this, 20, PollingService.class, PollingService.ACTION,"1");
                }
            }
        }
    };

}
