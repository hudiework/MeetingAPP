package com.example.dell.demo2.loginsucce_participantpart;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.adapter.MeetingAdapter;
import com.example.dell.demo2.httpurlconnection.HttpUtils;
import com.example.dell.demo2.listview.SearchMeetingActivity;
import com.example.dell.demo2.loadutils.OkHttpUtil;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Callback;
import okhttp3.Response;

public class ApplyMeetingFragment extends Fragment implements View.OnClickListener {
    private EditText etSearchMeeting;
    private ListView lvMeeting;
    private TextView tvDefault;
    private List<MeetingBean> ongoingMeetingList;
    private ImageView ivSearch, ivEmptyData;
    private Spinner sp;
    private String[] list;
    private Gson gson;
    private int position;
    private Handler handler;
    private String responseJsonStr;
    private MeetingAdapter meetingAdapter;
    private List<MeetingBean> meetingList;
    private List<MeetingBean> meetings = new ArrayList<>();
    private static final String HTTPPATH = Url.URLa + "SearchServlet";
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_apply_meeting, container, false);
        initView();

        String url = Url.URLa + "GetAllOngingMeeting";
        OkHttpUtil.doGet(url, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("执行了错误回调", e + "");
                Log.e("执行了错误回调", e + "");
                Log.e("执行了错误回调", e + "");
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                responseJsonStr = response.body().string();
                System.out.println("成功获取数据！！！！" + responseJsonStr.toString());
                System.out.println("成功获取数据！！！！" + responseJsonStr.toString());
                System.out.println("成功获取数据！！！！" + responseJsonStr.toString());

            }
        });

        if (responseJsonStr == null || responseJsonStr.equals("") || responseJsonStr.equals("[]")) {
            lvMeeting.setVisibility(View.GONE);
            ivEmptyData.setVisibility(View.VISIBLE);
            tvDefault.setVisibility(View.VISIBLE);
            tvDefault.setText("暂无数据！！");
        } else {
            meetingList = gson.fromJson(responseJsonStr, new TypeToken<List<MeetingBean>>() {
            }.getType());
            lvMeeting.setVisibility(View.VISIBLE);
            // lv_over_meeting.setVisibility(View.GONE);//当两个list同时存在时，添加item点击事件无效
            tvDefault.setVisibility(View.GONE);
            ivEmptyData.setVisibility(View.GONE);
            meetingAdapter = new MeetingAdapter(getActivity(), ongoingMeetingList);
            lvMeeting.setAdapter(meetingAdapter);
        }
        return view;
    }

    private void initView() {
        etSearchMeeting = view.findViewById(R.id.et_searchMeeting);
        sp = view.findViewById(R.id.sp);
        ivEmptyData = view.findViewById(R.id.iv_empty_data);
        tvDefault = view.findViewById(R.id.tv_frag_default);
        ivSearch = view.findViewById(R.id.iv_search);
        lvMeeting = view.findViewById(R.id.lv_all_meeting);
        ivSearch.setOnClickListener(this);
        list = new String[]{"会议号", "会议名"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        gson = new Gson();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search: {
                String sRegex = "^[1-9]\\d*$";
                Pattern p = Pattern.compile(sRegex);
                Matcher m = p.matcher(etSearchMeeting.getText().toString());
                if (position == 0 && !m.matches()) {
                    Toast.makeText(getActivity(), "请输入整数！", Toast.LENGTH_LONG).show();
                } else {
                    final String str = "type=" + position + "&content=" + etSearchMeeting.getText().toString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = handler.obtainMessage();
                            try {
                                Log.e("*****", str);
                                message.obj = HttpUtils.httpURLConnectionGetData(HTTPPATH,
                                        str, "utf-8", "text/html");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            handler.sendMessage(message);
                        }
                    }).start();

                }
            }
            break;
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = msg.obj.toString();
                Log.e("**********", str);
                Gson gson = new Gson();
                meetings = gson.fromJson(str, new TypeToken<List<MeetingBean>>() {
                }.getType());
                if (meetings.size() == 0) {
                    Toast.makeText(getActivity(), "无此会议", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), SearchMeetingActivity.class);
                    intent.putExtra("meetings", str);
                    startActivity(intent);
                }

            }
        };
    }
}
