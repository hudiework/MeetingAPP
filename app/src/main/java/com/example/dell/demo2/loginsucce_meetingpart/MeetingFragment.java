package com.example.dell.demo2.loginsucce_meetingpart;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.adapter.MeetingAdapter;
import com.example.dell.demo2.create_meeting.utils.DateUtil;
import com.example.dell.demo2.loadutils.OkHttpUtil;
import com.example.dell.demo2.secretary.SecretaryMainActivity;
import com.example.dell.demo2.supervisor.OkHttpUtils;
import com.example.dell.demo2.supervisor.SupervisorActivity;
import com.example.dell.demo2.web_url_help.Url;
import com.example.dell.demo2.worker.WorkerActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
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
    private ListView lv_meeting;
    private List<List<MeetingBean>> meetingList, MeetingListNew;
    private List<MeetingBean> ongoingMeetingList;
    private List<MeetingBean> overMeetingList;
    private Gson gson;
    private static MeetingAdapter meetingAdapter;
    private int meetingId, positionTag;
    private String myRole;
    private MeetingBean myMeeting = new MeetingBean();
    private DateUtil dateUtil = new DateUtil();
    private Handler handler;
    private View view;
    private ImageView ivEmptyData;
    private String phone,responseJsonStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        view = inflater.inflate(R.layout.fragment_meetingpart_meeting, container, false);
        init();
        phone = (String)getArguments().get("phone");
        responseJsonStr=(String) getArguments().get("str");
        if (responseJsonStr == null)
            /*if (responseJsonStr.equals("") ||  responseJsonStr.equals("[]"))*/ {
            lv_meeting.setVisibility(View.GONE);
            ivEmptyData.setVisibility(View.VISIBLE);
            tv_default.setVisibility(View.VISIBLE);
            tv_default.setText("暂无数据！！");
            System.out.println("跳转并输出数据：：：暂无数据！！" + responseJsonStr);
            System.out.println("跳转并输出数据：：：暂无数据！！" + responseJsonStr);
            System.out.println("跳转并输出数据：：：暂无数据！！" + responseJsonStr);
            System.out.println("跳转并输出数据：：：暂无数据！！" + responseJsonStr);System.out.println("跳转并输出数据：：：" + responseJsonStr);
            System.out.println("跳转并输出数据：：：暂无数据！！" + responseJsonStr);
            System.out.println("跳转并输出数据：：：暂无数据！！" + responseJsonStr);
        } else {
            tv_default.setVisibility(View.GONE);
            lv_meeting.setVisibility(View.VISIBLE);
            ivEmptyData.setVisibility(View.GONE);
            System.out.println("跳转并输出数据：：：" + responseJsonStr);
            System.out.println("跳转并输出数据：：：" + responseJsonStr);
            System.out.println("跳转并输出数据：：：" + responseJsonStr);
            System.out.println("跳转并输出数据：：：" + responseJsonStr);System.out.println("跳转并输出数据：：：" + responseJsonStr);
            System.out.println("跳转并输出数据：：：" + responseJsonStr);
            System.out.println("跳转并输出数据：：：" + responseJsonStr);
            // Toast.makeText(MeetingPart_MyMeetingActivity.this,responseJsonStr,Toast.LENGTH_SHORT).show();
            meetingList = gson.fromJson(responseJsonStr, new TypeToken<List<List<MeetingBean>>>() {}.getType());
            ongoingMeetingList = meetingList.get(1); //获取到还未结束的会议
            overMeetingList = meetingList.get(0);//获取到历史会议
            //现在展示的是未结束的会议
            //Toast.makeText(MeetingPart_MyMeetingActivity.this,ongoingMeetingList.size(),Toast.LENGTH_SHORT).show();
            meetingAdapter = new MeetingAdapter(getActivity(), ongoingMeetingList);
            lv_meeting.setAdapter(meetingAdapter);
            //设置点击事件
            lv_meeting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    myRole = ongoingMeetingList.get(position).getMyRole();
                    myMeeting = ongoingMeetingList.get(position);
                    Toast.makeText(getActivity().getApplicationContext(), myRole + "被单击", Toast.LENGTH_SHORT).show();
                    //myRole.equals("普通工作人员");
                    meetingId = myMeeting.getMeetingId();
                    System.out.println("输出点击的回应ID：" + meetingId);
                    if (myRole.equals("普通工作人员")) {
                        Toast.makeText(getActivity().getApplicationContext(), "角色是普通工作人员！！", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), WorkerActivity.class);
                        i.putExtra("meeting", (Serializable) myMeeting);
                        i.putExtra("meetingId", meetingId + "");
                        startActivity(i);
                        //myRole.equals("主管")
                    } else if (myRole.equals("主管")) {
                        Intent i = new Intent(getActivity(), SupervisorActivity.class);
                        System.out.println("跳转到主管界面！！");
                        System.out.println("跳转到主管界面！！");
                        System.out.println("跳转到主管界面！！");
                        i.putExtra("meeting", myMeeting);
                        i.putExtra("meetingId", meetingId + "");
                        startActivity(i);
                    } else if (myRole.equals("秘书")) {
                        Intent i = new Intent(getActivity(), SecretaryMainActivity.class);
                        System.out.println("跳转到秘书界面！！");
                        System.out.println("跳转到秘书界面！！");
                        System.out.println("跳转到秘书界面！！");

                        i.putExtra("meeting", myMeeting);
                        i.putExtra("meetingId", meetingId + "");
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), "该会议还未分配角色", Toast.LENGTH_SHORT).show();
                        //只是转至会议详情界面？？
                    }
                }
            });
        }
        lv_meeting.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("选择操作");
                menu.add(0, 0, 0, "修改此会议");
                menu.add(0, 1, 0, "删除此会议");
            }
        });
        return view;
    }

    //给菜单项添加事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //info.id得到listview中选择的条目绑定的id
        //String id = String.valueOf(info.id);
        int position = (int) info.id;
        switch (item.getItemId()) {
            case 0:  //修改
                //Toast.makeText(this, "点击了修改", Toast.LENGTH_SHORT).show();
                myRole = ongoingMeetingList.get(position).getMyRole();
                myMeeting = ongoingMeetingList.get(position);
                meetingId = myMeeting.getMeetingId();
                if (myRole.equals("主管") || myRole.equals("秘书")) {
                    String meetingStartTime = myMeeting.getMeetingDate();
                    Boolean iszao = dateUtil.compareNowTime2(meetingStartTime);  //true说明此会议开始时间晚于系统时间即未开始
                    if (iszao) {
                        System.out.println("修改时传的meetingId" + meetingId);
                        Intent i = new Intent(getActivity(), UpdateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("meeting", myMeeting);
                        bundle.putInt("meetingId", meetingId);
                        i.putExtras(bundle);
                        //startActivityForResult(i, 1);
                        startActivity(i);
                    } else {
                        System.out.println("会议已开始不能修改！");
                        Toast.makeText(getActivity(), "此会议已开始不能进行修改！", Toast.LENGTH_SHORT).show();
                        return true;
                    }


                } else {
                    System.out.println("普通工作人员不能修改！");
                    Toast.makeText(getActivity(), "抱歉，您不能修改此会议！", Toast.LENGTH_SHORT).show();
                    return true;
                }


                return true;
            case 1:  //删除
                myRole = ongoingMeetingList.get(position).getMyRole();
                myMeeting = ongoingMeetingList.get(position);
                meetingId = myMeeting.getMeetingId();
                if (myRole.equals("主管") || myRole.equals("秘书")) {
                    String meetingStartTime = myMeeting.getMeetingDate();
                    Boolean iszao = dateUtil.compareNowTime2(meetingStartTime);  //true说明此会议开始时间晚于系统时间即未开始
                    if (iszao) {
                        System.out.println("删除时传的meetingId" + meetingId);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("是否");
                        builder.setMessage("确认删除此会议?");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteMeetThread deleteMeetThread = new DeleteMeetThread(meetingId + "", "DeleteMeetingServlet");
                                deleteMeetThread.start();
                                handler = new Handler() {
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        String data = msg.obj.toString();
                                        System.out.println("删除时的data" + data);
                                        if (data == "SUCCESS") {
                                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                };
                                //this.finish();

                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();


                    } else {
                        System.out.println("会议已开始不能删除！");
                        Toast.makeText(getActivity(), "此会议已开始不能进行修改！", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                } else {
                    System.out.println("普通工作人员不能删除！");
                    Toast.makeText(getActivity(), "抱歉，您不能删除此会议！", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void init() {
        tv_default = view.findViewById(R.id.tv_frag_default);
        ivEmptyData = view.findViewById(R.id.iv_empty_data);
        lv_meeting = view.findViewById(R.id.lv_meeting);


        meetingList = new ArrayList<List<MeetingBean>>();
        ongoingMeetingList = new ArrayList<MeetingBean>();
        overMeetingList = new ArrayList<MeetingBean>();
        gson = new Gson();
    }

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("执行onActivityResult1，主会议里的");
        if (requestCode == 1) {
            System.out.println("执行onActivityResult2，主会议里的");
            if (resultCode == 11) {
                System.out.println("执行onActivityResult3，主会议里的");
                Toast.makeText(MeetingPart_MyMeetingActivity.this, "更新界面去了", Toast.LENGTH_SHORT).show();
                QuerryMeetingThread querryMeetingThread = new QuerryMeetingThread("18510801374", "MeetingPart_MyMeetingServlet", handler);
                querryMeetingThread.start();
                handler = new Handler() {
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String data = msg.obj.toString();
                        if (data != "") {
                            MeetingListNew = gson.fromJson(data, new TypeToken<List<ChildMeetingBean>>() {
                            }.getType());
                        }
                    }

                };
                ongoingMeetingList.clear();
                ongoingMeetingList.addAll(MeetingListNew.get(1));
                meetingAdapter.notifyDataSetChanged();
            }
        }


    }*/

    public class DeleteMeetThread extends Thread {
        private String servlet;
        private String meetingId;

        public DeleteMeetThread(String meetingId, String servlet) {
            this.meetingId = meetingId;
            this.servlet = servlet;
        }

        public void run() {
            String url = Url.URLa + servlet;
            String response = OkHttpUtil.doPost(url, meetingId);
            System.out.println("删除界面返回的：" + response);
            Message message = handler.obtainMessage();
            message.obj = response;
            handler.sendMessage(message);

        }

    }
}

