package com.example.dell.demo2.worker.public_resource_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.worker.public_resource_bean.HotelBean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/29.
 */

public class HotelAdapter extends BaseAdapter {
    private List<List<HotelBean>> lists;
    private LayoutInflater inflater;
    private Context context;


    public HotelAdapter(Context context, List<List<HotelBean>> list) {
        this.lists = list;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        HotelViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.hotel_list_item, null);
            holder = new HotelViewHolder();
            holder.tv_hotelName= (TextView) view.findViewById(R.id.tv_hotelName);
            holder.lv_hotels = (ListView) view.findViewById(R.id.lv_hotels);
            holder.tv_hotelEval=view.findViewById(R.id.tv_hotelEval);
            holder.tv_hotelAddress=view.findViewById(R.id.tv_hotelAddress);
            view.setTag(holder);
        } else {
            holder = (HotelViewHolder) view.getTag();
        }
        List<HotelBean> hotel= lists.get(position);
        if(hotel.size()==0||hotel==null){
            holder.tv_hotelName.setText("暂无数据！！");
        }else {
            holder.tv_hotelName.setText(hotel.get(0).getHotelName());//填写酒店名
            holder.tv_hotelAddress.setText(hotel.get(0).getHotelAddress());//酒店地址
            holder.tv_hotelEval.setText(hotel.get(0).getHotelEval() + "");//酒店评价
            System.out.println("子adapter中的数据："+hotel.get(0).getRoomType());
            System.out.println("子adapter中的数据："+hotel.get(1).getRoomType());
            ChildHotelAdapter childHotelAdapter = new ChildHotelAdapter(context, hotel);
            holder.lv_hotels.setAdapter(childHotelAdapter);
            setListViewHeightBasedOnChildren( holder.lv_hotels);
        }
        return view;
    }

    public final class HotelViewHolder {
        public TextView tv_hotelName;
        public ListView lv_hotels;
        public TextView tv_hotelEval;//酒店评分
        public TextView tv_hotelAddress;//酒店地址
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ChildHotelAdapter childHotelAdapter = (ChildHotelAdapter) listView.getAdapter();
        if (childHotelAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < childHotelAdapter.getCount(); i++) {
            View listItem = childHotelAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (childHotelAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
