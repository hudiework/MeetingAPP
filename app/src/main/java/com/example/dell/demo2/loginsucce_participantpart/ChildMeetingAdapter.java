package com.example.dell.demo2.loginsucce_participantpart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.demo2.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class ChildMeetingAdapter extends BaseAdapter {
    private Context mContext;
    private List<ChildMeetingBean> childMeetingList;

    public ChildMeetingAdapter(Context context, List<ChildMeetingBean> childMeetingList){
        this.mContext=context;
        this.childMeetingList=childMeetingList;
    }

    @Override
    public int getCount() {
        return childMeetingList.size();
    }

    @Override
    public Object getItem(int position) {
        return childMeetingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ChildMeetingAdapter.ChildMeetingViewHolder childMeetingViewHolder=null;
        if(view==null){
            view=View.inflate(mContext, R.layout.child_detail_layout,null);
            childMeetingViewHolder=new ChildMeetingAdapter.ChildMeetingViewHolder();
            childMeetingViewHolder.tv_childMeetingCompere=(TextView) view.findViewById(R.id.tv_childMeetingCompere);
            childMeetingViewHolder.tv_childMeetingContent=(TextView) view.findViewById(R.id.tv_childMeetingContent);
            childMeetingViewHolder.tv_childMeetingName=(TextView) view.findViewById(R.id.tv_childMeetingName);
            childMeetingViewHolder.tv_childMeetingPlace=view.findViewById(R.id.tv_childMeetingPlace);
            childMeetingViewHolder.tv_childMeetingTime=view.findViewById(R.id.tv_childMeetingTime);
            view.setTag(childMeetingViewHolder);
        }else{
            childMeetingViewHolder=(ChildMeetingAdapter.ChildMeetingViewHolder) view.getTag();
        }
        childMeetingViewHolder.tv_childMeetingName.setText(childMeetingList.get(position).getChildMeetingName());
        childMeetingViewHolder.tv_childMeetingTime.setText(childMeetingList.get(position).getChildMeetingTime());
        childMeetingViewHolder.tv_childMeetingContent.setText(childMeetingList.get(position).getChildMeetingContent());
        childMeetingViewHolder.tv_childMeetingPlace.setText(childMeetingList.get(position).getChildMeetingPlace());
        childMeetingViewHolder.tv_childMeetingCompere.setText(childMeetingList.get(position).getChildMeetingCompere());
        return view;

    }
    public  class ChildMeetingViewHolder{
        TextView tv_childMeetingName;//会议主题
        TextView tv_childMeetingCompere;//主持人
        TextView tv_childMeetingContent;//会议内容
        TextView tv_childMeetingTime;//开始时间
        TextView tv_childMeetingPlace;//会议地点
    }
}
