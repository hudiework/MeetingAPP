package com.example.dell.demo2.listview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.adapter.MeetingAdapter;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SearchMeetingActivity extends Activity{

	private ListView listView;
	private MeetingAdapter meetingAdapter;
	private static final String HTTPPATH = Url.URLa+"MyServlet";
	private List<MeetingBean> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_participant_part__my_meeting);
		listView = (ListView) findViewById(R.id.lv_ongoing_meeting);
		String str=getIntent().getStringExtra("meetings");
		Gson gson = new Gson();
		list= gson.fromJson(str, new TypeToken<List<MeetingBean>>() {
		}.getType());
		meetingAdapter = new MeetingAdapter(this,list);
		listView.setAdapter(meetingAdapter);
	}


}