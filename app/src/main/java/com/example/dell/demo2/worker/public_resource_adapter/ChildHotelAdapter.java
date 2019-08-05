package com.example.dell.demo2.worker.public_resource_adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.worker.ScheduleHotelActivity;
import com.example.dell.demo2.worker.public_resource_bean.HotelBean;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Administrator on 2018/4/29.
 */

public class ChildHotelAdapter extends BaseAdapter {
    private List<HotelBean> hotelList;
    private LayoutInflater inflater;
    private Context context;

    public ChildHotelAdapter(Context context, List<HotelBean> list) {
        this.hotelList = list;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return hotelList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return hotelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildHotelViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.child_hotel_list_item, null);
            holder = new ChildHotelViewHolder();
            holder.tv_hotel_availableNumber= (TextView) view.findViewById(R.id.tv_hotel_availableNumber);
            holder.tv_hotelCharge = (TextView) view.findViewById(R.id.tv_hotelCharge);
            holder.tv_hotelType = (TextView) view.findViewById(R.id.tv_hotelType);
            holder.btn_schedule=view.findViewById(R.id.btn_schedule);
            view.setTag(holder);
        } else {
            holder = (ChildHotelViewHolder) view.getTag();
        }
        final HotelBean hotel= hotelList.get(position);
        holder.tv_hotelType.setText(hotel.getRoomType());
        holder.tv_hotelCharge.setText(hotel.getMoney());
        holder.tv_hotel_availableNumber.setText(hotel.getRoomAvailableNumber()+"");
        holder.btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ScheduleHotelActivity.class);
                // 通过Bundle
                Bundle bundle = new Bundle();
               // bundle.putString("MyString", "test bundle");
                bundle.putSerializable("hotel",  hotel);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }

    public final class ChildHotelViewHolder {
        public TextView tv_hotelType;//房间类型
        public TextView tv_hotelCharge;//预定价格
        public TextView tv_hotel_availableNumber;//可预订数；
        public Button btn_schedule;//点击预订
    }
}
