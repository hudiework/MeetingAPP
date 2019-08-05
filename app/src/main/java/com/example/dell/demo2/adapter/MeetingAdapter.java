package com.example.dell.demo2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;

import java.util.List;

/*
 * Created by Administrator on 2018/3/30.
 * */


public class MeetingAdapter extends BaseAdapter {
    private Context mContext;
    private List<MeetingBean> meetingList;

    public MeetingAdapter(Context context,List<MeetingBean> meetingBeans){
        this.mContext=context;
        this.meetingList=meetingBeans;
    }
    @Override
    public int getCount() {
        return meetingList.size();
    }

    @Override
    public Object getItem(int position) {
        return meetingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
    ViewHolder viewHolder=null;

        if(view==null){
            view=View.inflate(mContext, R.layout.meeting_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_meeting_content=(TextView) view.findViewById(R.id.tv_meeting_content);
            viewHolder.tv_meeting_name=(TextView) view.findViewById(R.id.tv_meeting_name);
            viewHolder.tv_my_role=(TextView) view.findViewById(R.id.tv_myRole);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.tv_meeting_name.setText(meetingList.get(position).getMeetingName());
        viewHolder.tv_meeting_content.setText(meetingList.get(position).getMeetingContent());
        viewHolder.tv_my_role.setText(meetingList.get(position).getMyRole());


        return view;
    }

    public  class ViewHolder{
        TextView tv_meeting_name;
        TextView tv_meeting_content;
        TextView tv_my_role;
    }
}
