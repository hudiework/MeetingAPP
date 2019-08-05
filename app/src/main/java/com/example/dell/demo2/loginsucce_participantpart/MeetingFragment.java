package com.example.dell.demo2.loginsucce_participantpart;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.adapter.MeetingAdapter;
import com.example.dell.demo2.loginsucce_meetingpart.MeetingBean;
import com.example.dell.demo2.supervisor.OkHttpUtils;
import com.example.dell.demo2.web_url_help.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dell on 2018-04-15.
 */

public class MeetingFragment extends Fragment {
    private TextView tv_default;
    private ListView lv_ongoing_meeting;
    private ImageView ivEmptyData;
    private Gson gson;
    private MeetingAdapter meetingAdapter;
    private View view;
    private String phone;
    private String responseJsonStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_participant_meeting, container, false);
        init();
        phone = (String)getArguments().get("phone");

        Intent i = getActivity().getIntent();
        String url= Url.URLa+"ParticipantMeetingServlet";
        Map<String,String> map=new HashMap<String,String>();
        map.put("mobilePhone",phone);
        OkHttpUtils.post(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("错误来了：", "执行错误回调！！！ ", e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseJsonStr = response.body().string();
                System.out.println("获取到参加过的会议啊啊啊啊"+responseJsonStr);
                System.out.println("这次难道没有回忆了：" + responseJsonStr);
            }
        } ,map);

        List<List<MeetingBean>> list = new ArrayList<List<MeetingBean>>();
       // List<MeetingBean> meetingOverList = new ArrayList<MeetingBean>();
        List<MeetingBean> ongoingMeetingList = new ArrayList<MeetingBean>();
        System.out.println("这次难道没有回忆了：" + responseJsonStr);
        if (/*responseJsonStr.equals("")||*/responseJsonStr == null) {
            lv_ongoing_meeting.setVisibility(View.GONE);
            //lv_over_meeting.setVisibility(View.GONE);
            ivEmptyData.setVisibility(View.VISIBLE);
            tv_default.setText("暂无会议信息！");
        } else {
            list = gson.fromJson(responseJsonStr, new TypeToken<List<List<MeetingBean>>>() {
            }.getType());
            //meetingOverList = list.get(0);
            ongoingMeetingList = list.get(1);
            Toast.makeText(getActivity(), "lallal::" + ongoingMeetingList.size(), Toast.LENGTH_SHORT).show();
            /*if(meetingOverList==null&&meetingOverList.isEmpty()){
                //仅仅判断了是否有正在进行的会议，而没有判断和展示已经结束了的会议
                lv_ongoing_meeting.setVisibility(View.GONE);
                lv_over_meeting.setVisibility(View.GONE);
                tv_default.setVisibility(View.VISIBLE);
                tv_default.setText("暂无会议信息！");
            }else{*/
            lv_ongoing_meeting.setVisibility(View.VISIBLE);
            // lv_over_meeting.setVisibility(View.GONE);//当两个list同时存在时，添加item点击事件无效
            tv_default.setVisibility(View.GONE);
            ivEmptyData.setVisibility(View.GONE);
            meetingAdapter = new MeetingAdapter(getActivity(), ongoingMeetingList);
            lv_ongoing_meeting.setAdapter(meetingAdapter);
            System.out.println("进了else participant界面呐！！");
            System.out.println("进了else 加载了适配器！！");
            //}

            return view;
        }

        final List<MeetingBean> finalOngoingMeetingList = ongoingMeetingList;

        lv_ongoing_meeting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //添加item点击事件，展示会议详情，有会议资料则可下载..
                System.out.println("点击了会议啦阿拉啦啦啦啦啦");
                //System.out.println(finalOngoingMeetingList.get(i).getMeetingAgenda());

                MeetingBean meeting = finalOngoingMeetingList.get(i);
                int id = meeting.getMeetingId();
                Intent intent = new Intent(getActivity(), ParticipantPart_MeetingDetailActivity.class);
                intent.putExtra("meeting", meeting);
                intent.putExtra("meetingId", id + "");
                startActivity(intent);
            }
        });

       /* lv_over_meeting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("点击了会议啦阿拉啦啦啦啦啦");
                System.out.println("点击了会议啦阿拉啦啦啦啦啦");
                System.out.println("点击了会议啦阿拉啦啦啦啦啦");
            }
        });*/


        return view;
    }

    public void init() {
        tv_default = view.findViewById(R.id.tv_participant_default);
        lv_ongoing_meeting = view.findViewById(R.id.lv_ongoing_meeting);
        //lv_over_meeting = view.findViewById(R.id.lv_over_meeting);
        ivEmptyData = view.findViewById(R.id.iv_empty_data);
        gson = new Gson();
    }
}