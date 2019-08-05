package com.example.dell.demo2.supervisor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.supervisor.beans.HotelBean;
import com.example.dell.demo2.supervisor.beans.PlaceBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class MeetingDetailActivity extends AppCompatActivity {
private String responseJsonStr;
private Gson gson;
private List<HotelBean> hotelList;
private List<PlaceBean> placeList;
private TextView tv_people,tv_people_num;
private TextView tv_hotel,tv_hotel_room,tv_hotel_room_num,tv_hotel_galleryful,tv_hotel_galleryful_num;
private CheckBox cb_hotel,cb_place;
private TextView tv_place,tv_place_room,tv_place_room_num,tv_place_galleryful,tv_place_galleryful_num,
    tv_place_address,tv_place_address_detail;
private int hotelRoomNum=0,hotelGalleryful=0;
private int placeRoomNum=0,placeGalleryful=0;
private String placeAddress="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);
        init();
        Intent intent=getIntent();
        responseJsonStr= intent.getStringExtra("responseJsonStr");
        if(responseJsonStr.equals("0")){
            System.out.println("暂无信息！");
        }else{
            String str=responseJsonStr.substring(0,2);
            //"10" 代表hotel有信息，place无信息
            //“01”代表place无信息，hotel有信息
            //“11”代表hotel和place都有信息
            if (str.equals("10")){
                cb_hotel.setChecked(true);
                System.out.println("输出hotel的信息："+responseJsonStr.substring(2));
                hotelList= gson.fromJson(responseJsonStr.substring(2), new TypeToken<List<HotelBean>>(){}.getType());
                for(int i=0;i<hotelList.size();i++){
                    hotelRoomNum= hotelList.get(i).getHotelRoomNum()+hotelRoomNum;
                    hotelGalleryful=hotelList.get(i).getHotelRoomNum()* hotelList.get(i).getHotelGalleryful()+hotelGalleryful;
                }
                tv_hotel_room_num.setText(hotelRoomNum+"间");
                tv_hotel_galleryful_num.setText(hotelGalleryful+"人");
            }else if(str.equals("01")){
                cb_place.setChecked(true);
                System.out.println("输出place的信息："+responseJsonStr.substring(2));
                placeList= gson.fromJson(responseJsonStr.substring(2), new TypeToken<List<PlaceBean>>(){}.getType());
                for(int i=0;i<placeList.size();i++){
                    placeRoomNum= placeList.get(i).getPlaceNum()+placeRoomNum;
                    placeGalleryful=placeList.get(i).getPlaceNum()* placeList.get(i).getPlaceGalleryful()+placeGalleryful;
                }
                tv_place_room_num.setText(placeRoomNum+"间");
                tv_place_galleryful_num.setText(placeGalleryful+"人");

            }else{
                cb_hotel.setChecked(true);
                cb_place.setChecked(true);
                String responseStr= responseJsonStr.substring(2);
                int indexStart =responseStr.indexOf("0");
                String hotelStr=responseStr.substring(0,indexStart);
                String placeStr=responseStr.substring(indexStart+1);
                hotelList= gson.fromJson(hotelStr, new TypeToken<List<HotelBean>>(){}.getType());
                placeList= gson.fromJson(placeStr, new TypeToken<List<PlaceBean>>(){}.getType());
                for(int i=0;i<hotelList.size();i++){
                    hotelRoomNum= hotelList.get(i).getHotelRoomNum()+hotelRoomNum;
                    hotelGalleryful=hotelList.get(i).getHotelRoomNum()* hotelList.get(i).getHotelGalleryful()+hotelGalleryful;
                }
                tv_hotel_room_num.setText(hotelRoomNum+"间");
                tv_hotel_galleryful_num.setText(hotelGalleryful+"人");

                for(int i=0;i<placeList.size();i++){
                    placeRoomNum= placeList.get(i).getPlaceNum()+placeRoomNum;
                    placeGalleryful=placeList.get(i).getPlaceNum()* placeList.get(i).getPlaceGalleryful()+placeGalleryful;
                }
                tv_place_room_num.setText(placeRoomNum+"间");
                tv_place_galleryful_num.setText(placeGalleryful+"人");


                System.out.println("输出两个信息：hotelStr:"+hotelStr);
                System.out.println("输出两个信息：placeStr:"+placeStr);
            }
        }


    }
    public void init(){
        gson=new Gson();
        hotelList=new ArrayList<HotelBean>();
        placeList=new ArrayList<PlaceBean>();
        tv_people=(TextView) findViewById(R.id.tv_people);
        tv_people_num=(TextView) findViewById(R.id.tv_people_num);
        tv_hotel=(TextView) findViewById(R.id.tv_hotel);
        tv_hotel_room=(TextView) findViewById(R.id.tv_hotel_room);
        tv_hotel_room_num=(TextView) findViewById(R.id.tv_hotel_room_num);
        tv_hotel_galleryful=(TextView) findViewById(R.id.tv_hotel_galleryful);
        tv_hotel_galleryful_num=(TextView) findViewById(R.id.tv_hotel_galleryful_num);
        cb_hotel=(CheckBox) findViewById(R.id.cb_hotel);
        cb_place=(CheckBox) findViewById(R.id.cb_place);
        tv_place=(TextView) findViewById(R.id.tv_place);
        tv_place_room=(TextView) findViewById(R.id.tv_place_room);
        tv_place_room_num=(TextView) findViewById(R.id.tv_place_room_num);
        tv_place_galleryful=(TextView) findViewById(R.id.tv_place_galleryful);
        tv_place_galleryful_num=(TextView) findViewById(R.id.tv_place_galleryful_num);
        tv_place_address=(TextView) findViewById(R.id.tv_place_address);
        tv_place_address_detail=(TextView) findViewById(R.id.tv_place_address_detail);
    }
}
