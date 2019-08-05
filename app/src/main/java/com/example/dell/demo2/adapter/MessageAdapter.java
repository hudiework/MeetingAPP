package com.example.dell.demo2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.ContentEntity;

public class MessageAdapter extends BaseAdapter {

	private List<ContentEntity> list;
	private LayoutInflater inflater;

	public MessageAdapter(Context context, List<ContentEntity> list) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.activity_list, null);
			holder = new ViewHolder();
			holder.meeting= (TextView) view.findViewById(R.id.list_meeting);
			holder.date = (TextView) view.findViewById(R.id.list_date);
			//holder.count=view.findViewById( R.id.list_count );
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		ContentEntity content= list.get(position);
		holder.meeting.setText(content.getMeeting());
		holder.date.setText(content.getDate());
		//holder.count.setText("点击量:"+content.getCount());
		return view;
	}

	public final class ViewHolder {
		public TextView meeting;
		public TextView date;
		//public TextView count;
	}

}
