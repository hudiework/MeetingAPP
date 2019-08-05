package com.example.dell.demo2.create_meeting.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loginsucce_participantpart.ChildMeetingBean;

import java.util.List;

/**
 * Created by dell on 2018-04-20.
 */

public class ChildMeetAdaper extends BaseAdapter {
    private List<ChildMeetingBean> childlist;
    private LayoutInflater inflater;

    public ChildMeetAdaper(List<ChildMeetingBean> childlist, Context context) {
        this.childlist = childlist;
        inflater = LayoutInflater.from(context);   //传入数据源
    }

    @Override
    public int getCount() {
        return childlist.size();
    }

    @Override
    public Object getItem(int i) {
        return childlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) { // 缓冲池中没有可复用的view时，新建view
            //1.将列表项布局文件解析为一个view对象
            view = inflater.inflate(R.layout.child_meet_item, null);
            holder = new ViewHolder();
            //2.使用ViewHolder类封装view中的UI组件
            holder.name = (TextView) view.findViewById(R.id.tv_showItemName);
            holder.content = (TextView) view.findViewById(R.id.tv_showItemContent);
            holder.time = (TextView) view.findViewById(R.id.tv_showItmeTime);
            holder.place = (TextView) view.findViewById(R.id.tv_showItemPlace);
            holder.compere = (TextView) view.findViewById(R.id.tv_showItemCompere);
            //3.为对象设置holder标记（将holder放入view中，将holder和view视图关联）
            view.setTag(holder);
        } else {
            //否则，复用缓冲池里的view对象
            holder = (ViewHolder) view.getTag();
        }
        //从数据源中获取指定位置处的数据项，并填充到子视图的显示组件中
        ChildMeetingBean childMeet = childlist.get(position);
        holder.name.setText(childMeet.getChildMeetingName());
        holder.content.setText(childMeet.getChildMeetingContent());
        holder.time.setText(childMeet.getChildMeetingTime());
        holder.place.setText(childMeet.getChildMeetingPlace());
        holder.compere.setText(childMeet.getChildMeetingCompere());
        return view;
    }

    //内部类：封装子视图中的UI组件对象，优化应用
    public class ViewHolder {
        private TextView name, content, time, place, compere;
    }
}
