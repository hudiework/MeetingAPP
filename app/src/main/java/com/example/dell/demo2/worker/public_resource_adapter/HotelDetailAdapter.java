package com.example.dell.demo2.worker.public_resource_adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loadutils.OkHttpUtil;
import com.example.dell.demo2.web_url_help.Url;
import com.example.dell.demo2.worker.HotelListActivity;
import com.example.dell.demo2.worker.WorkerActivity;
import com.example.dell.demo2.worker.public_resource_bean.HotelBean;

import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/1.
 */

public class HotelDetailAdapter  extends BaseAdapter {
    private List<HotelBean> list;
    private LayoutInflater inflater;
    private Context context;

    public HotelDetailAdapter(Context context, List<HotelBean> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        HotelDetailAdapter.HotelDetailViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.hotel_detail_list_item, null);
            holder = new HotelDetailAdapter.HotelDetailViewHolder();
           holder.btn_hotel_detail_add=view.findViewById(R.id.btn_hotel_detail_add);
           holder.tv_hotel_detail_address=view.findViewById(R.id.tv_hotel_detail_address);
           holder.tv_hotel_detail_describ=view.findViewById(R.id.tv_hotel_detail_describ);
           holder.tv_hotel_detail_name=view.findViewById(R.id.tv_hotel_detail_name);
           holder.tv_hotel_detail_price=view.findViewById(R.id.tv_hotel_detail_price);
           holder.tv_hotel_detail_roomPeopleNumber=view.findViewById(R.id.tv_hotel_detail_roomPeopleNumber);
           holder.tv_hotel_detail_scheduleNum=view.findViewById(R.id.tv_hotel_detail_scheduleNum);
           holder.tv_hotel_detail_tel=view.findViewById(R.id.tv_hotel_detail_tel);
           holder.tv_hotel_detail_type=view.findViewById(R.id.tv_hotel_detail_type);
           view.setTag(holder);
        } else {
            holder = (HotelDetailAdapter.HotelDetailViewHolder) view.getTag();
        }
            HotelBean hotel= list.get(position);
            holder.tv_hotel_detail_type.setText(hotel.getRoomType());
            holder.tv_hotel_detail_tel.setText(hotel.getHotelPhoneNumber());
            holder.tv_hotel_detail_scheduleNum.setText(hotel.getHotelBookRoomNumber()+"");
            holder.tv_hotel_detail_roomPeopleNumber.setText(hotel.getRoomPeopleNumber()+"");
            holder.tv_hotel_detail_price.setText(hotel.getMoney());
            holder.tv_hotel_detail_address.setText(hotel.getHotelAddress());
            holder.tv_hotel_detail_describ.setText(hotel.getHotelDetail());
            holder.tv_hotel_detail_name.setText(hotel.getHotelName());

            holder.btn_hotel_detail_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url= Url.URLa+"GetHotelListServlet";
                    final String[] responseJsonStr = {""};
                    OkHttpUtil.doGet(url, new Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                            Log.e("执行了错误回调", e+"");
                            Log.e("执行了错误回调", e+"");
                            Log.e("执行了错误回调", e+"");
                        }
                        @Override
                        public void onResponse(okhttp3.Call call, Response response) throws IOException {
                            responseJsonStr[0] = response.body().string();
                            System.out.println("成功获取数据！！！！"+ responseJsonStr[0].toString());
                            System.out.println("成功获取数据！！！！"+ responseJsonStr[0].toString());
                            System.out.println("成功获取数据！！！！"+ responseJsonStr[0].toString());
                            Intent i3=new Intent(context,HotelListActivity.class);
                            i3.putExtra("responseJsonStr", responseJsonStr[0]);
                            context.startActivity(i3);
                        }
                    });

                }
            });

        return view;
    }

    public final class HotelDetailViewHolder {
        public TextView tv_hotel_detail_scheduleNum,tv_hotel_detail_price,tv_hotel_detail_address,
                tv_hotel_detail_tel,tv_hotel_detail_name,tv_hotel_detail_type,
                tv_hotel_detail_roomPeopleNumber,tv_hotel_detail_describ;
        public Button btn_hotel_detail_add;
    }
}
