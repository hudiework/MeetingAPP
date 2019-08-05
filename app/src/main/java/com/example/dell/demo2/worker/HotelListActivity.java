package com.example.dell.demo2.worker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.worker.public_resource_adapter.HotelAdapter;
import com.example.dell.demo2.worker.public_resource_bean.HotelBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class HotelListActivity extends AppCompatActivity {
    private TextView tv_hotelList_default;//暂无数据
    private ListView lv_hotel;//酒店信息主列表
  private  String responseJsonStr;
  private List<HotelBean> allHotelList;
  private List<List<HotelBean>> lists;
  private Gson gson;
  private HotelBean hotelBean;
  private List<HotelBean> childHotelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);
        initCtrl();
        initDate();

    }

    /**
     * 初始化数据
     */
    public void initDate(){
        String hotelName="";
        Intent i=getIntent();
        hotelBean=new HotelBean();
        childHotelList= new ArrayList<HotelBean>();
        allHotelList=new ArrayList<HotelBean>();
        lists=new ArrayList<List<HotelBean>>();
        responseJsonStr= i.getStringExtra("responseJsonStr");
        gson=new Gson();
        if(responseJsonStr=="0"||responseJsonStr==null||responseJsonStr.equals("")||responseJsonStr.equals("[]")){
            //如果数据为空，暂无酒店信息
            Toast.makeText(this,"没有get到数据",Toast.LENGTH_SHORT).show();
            System.out.println("meiyou成功获取数据！！！！"+ responseJsonStr);
            System.out.println("meiyou a 成功获取数据！！！！"+ responseJsonStr);
            System.out.println("meiyoua a a 成功获取数据！！！！");
            lv_hotel.setVisibility(View.GONE);
            tv_hotelList_default.setVisibility(View.VISIBLE);
        }else {
            System.out.println("tiaao转至展示界面成功获取数据！！！！"+ responseJsonStr);
            System.out.println("tiaao转至展示界面成功获取数据！！！！"+ responseJsonStr);
            System.out.println("tiaao转至展示界面成功获取数据！！！！"+ responseJsonStr);
            tv_hotelList_default.setVisibility(View.GONE);
            allHotelList= gson.fromJson(responseJsonStr, new TypeToken<List<HotelBean>>(){}.getType());
            hotelName=allHotelList.get(0).getHotelName();
            for(int j=0;j<allHotelList.size();j++){
               if(!allHotelList.get(j).getHotelName().equals(hotelName)){
                   //另一个酒店的房间
                   lists.add(childHotelList);
                   System.out.println("输出lists。size:::::"+childHotelList.size());
                   childHotelList=new ArrayList<HotelBean>();
                   childHotelList.add(allHotelList.get(j));
                   hotelName=allHotelList.get(j).getHotelName();
                   System.out.println("输出lists。hotelName:::::"+hotelName);
               }else{
                   //依然是同一个酒店的房间
                   System.out.println(allHotelList.get(j).getHotelName());
                   childHotelList.add(allHotelList.get(j));
               }
            }
            lists.add(childHotelList);//加上最后一个list；
            //循环一遍就得到了不同酒店的list
            System.out.println("tiaao转至chule循环了！！！！"+ childHotelList.size());
            System.out.println("tiaao转至chule循环了！！！"+ childHotelList.size());
            HotelAdapter hotelAdapter=new HotelAdapter(HotelListActivity.this,lists);
            lv_hotel.setVisibility(View.VISIBLE);
            lv_hotel.setAdapter(hotelAdapter);
        }

    }

    /**
     * 初始化控件
     */
    public void initCtrl(){
        tv_hotelList_default=findViewById(R.id.tv_hotelList_default);
        lv_hotel=findViewById(R.id.lv_hotel);
        tv_hotelList_default.setVisibility(View.GONE);
       // lv_hotel.setVisibility(View.GONE);
    }
}
