package com.example.dell.demo2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.CommentEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class CommentAdapter extends BaseAdapter{
    private Context context;
    private List<CommentEntity> list;

    public CommentAdapter(Context context, List<CommentEntity> list) {
        this.context = context;
        this.list = list;
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
        return position + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (convertView == null) {
            view = View.inflate(context,R.layout.list_item, null);
            holder = new ViewHolder();
            holder.meeting = (TextView) view.findViewById( R.id.text_meeting);
            holder.rb = (RatingBar) view
                    .findViewById(R.id.rb_meeting);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        CommentEntity comment= list.get(position);
        holder.meeting.setText( comment.getMid() );
        holder.rb.setRating(comment.getRating());
        return view;
    }

    public final class ViewHolder {
        public TextView meeting;
        public RatingBar rb;
    }
}
