package com.example.dell.demo2.worker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.worker.public_resource_adapter.HotelDetailAdapter;
import com.example.dell.demo2.worker.public_resource_bean.HotelBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ScheduleHotelDetailActivity extends AppCompatActivity {
private TextView tv_default_hotel_details;
    private ListView lv_hotel_details;
    private Gson gson;
    private List<HotelBean> hotelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_hotel_detail);
        initCtrl();
        initData();
    }
    /**
     * 初始化数据：
     */
    public void initData(){
        Intent i=getIntent();
       String responseJsonStr= i.getStringExtra("responseJsonStr");
       gson=new Gson();
        hotelList=new ArrayList<HotelBean>();
       if(responseJsonStr==null||responseJsonStr.equals("")||responseJsonStr.equals("[]")){
            System.out.println("获取数据为null*********");
           System.out.println("获取数据为null*********");
           System.out.println("获取数据为null*********");
           Toast.makeText(ScheduleHotelDetailActivity.this,"没有获取到数据",Toast.LENGTH_SHORT).show();
           tv_default_hotel_details.setVisibility(View.VISIBLE);
           lv_hotel_details.setVisibility(View.GONE);
       }else{
           tv_default_hotel_details.setVisibility(View.GONE);
           lv_hotel_details.setVisibility(View.VISIBLE);
           hotelList=gson.fromJson(responseJsonStr, new TypeToken<List<HotelBean>>(){}.getType());
           HotelDetailAdapter adapter=new HotelDetailAdapter(ScheduleHotelDetailActivity.this,hotelList);
           lv_hotel_details.setAdapter(adapter);
       }
    }

    /**
     * 初始化控件
     */
    public void initCtrl(){
        tv_default_hotel_details=findViewById(R.id.tv_default_hotel_details);
        lv_hotel_details=findViewById(R.id.lv_hotel_details);
        tv_default_hotel_details.setVisibility(View.GONE);//默认显示listView,无数据时才显示TextView
    }
}
