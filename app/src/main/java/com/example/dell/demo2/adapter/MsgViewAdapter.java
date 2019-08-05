package com.example.dell.demo2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.example.dell.demo2.R;
import com.example.dell.demo2.entity.MessageEntity;

public class MsgViewAdapter extends BaseAdapter {
	
	private List<MessageEntity> mEntities;
	private LayoutInflater lInflater;

	public MsgViewAdapter(Context mContext, List<MessageEntity> mEntities) {
		this.mEntities = mEntities;
		lInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mEntities.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mEntities.get(position);
	}

	@Override
	public int getItemViewType(int position) {
		MessageEntity entity = mEntities.get(position);
		if (entity.getMsgType()) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MessageEntity mEntity = mEntities.get(position);
		boolean isFrom = mEntity.getMsgType();
		ViewHolder holder = null;
		if (convertView == null) {
			if (isFrom) {
				convertView = lInflater.inflate(R.layout.item_left, null);
			} else {
				convertView = lInflater.inflate(R.layout.item_right, null);
			}

			holder = new ViewHolder();
			holder.content = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			holder.datextView = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			holder.name = (TextView) convertView.findViewById(R.id.tv_username);
			holder.touxiang = (ImageView) convertView
					.findViewById(R.id.iv_userhead);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.content.setText(mEntity.getMessage());
		holder.datextView.setText(mEntity.getDate());
		holder.name.setText(mEntity.getName());
		return convertView;

	}

	public class ViewHolder {
		TextView datextView;
		ImageView touxiang;
		TextView name;
		TextView content;
	}

}
