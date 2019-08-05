package com.example.dell.demo2.loginsucce_meetingpart;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import okhttp3.Callback;
import okhttp3.Response;

public class MeetingPart_MyMeetingActivity extends AppCompatActivity {
    private TextView tv_default;
    private ListView lv_meeting;
    private List<List<MeetingBean>> meetingList, MeetingListNew;
    private List<MeetingBean> ongoingMeetingList;
    private List<MeetingBean> overMeetingList;
    private Gson gson;
    private static MeetingAdapter meetingAdapter;
    private int meetingId, positionTag;
    private String myRole,myWorkName;
    private MeetingBean myMeeting = new MeetingBean();
    private DateUtil dateUtil = new DateUtil();
    private Handler handler,updateHandler,deleteHandler;
    private Map<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_part__my_meeting);
        init();
        String url= Url.URLa+"MeetingPart_MyMeetingServlet";
        map=new HashMap<String,String>();
        map.put("registerMobilePhone","18510801374");
        OkHttpUtils.post(url, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("执行了错误回调", e+"");
                Log.e("执行了错误回调", e+"");
                Log.e("执行了错误回调", e+"");
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String data = response.body().string();
                Message message = handler.obtainMessage();
                message.obj = data;
                handler.sendMessage(message);

            }
        } ,map);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String data = msg.obj.toString();
                meetingList = gson.fromJson(data, new TypeToken<List<List<MeetingBean>>>() {
                }.getType());
                System.out.println("meetingList："+meetingList);
                ongoingMeetingList = meetingList.get(1); //获取到还未结束的会议
                overMeetingList = meetingList.get(0);//获取到历史会议

                if (ongoingMeetingList.isEmpty()||ongoingMeetingList.size()==0) {
                    lv_meeting.setVisibility(View.GONE);
                    tv_default.setVisibility(View.VISIBLE);
                    tv_default.setText("暂无数据！！");
                } else {
                    tv_default.setVisibility(View.GONE);
                    lv_meeting.setVisibility(View.VISIBLE);
                    System.out.println("跳转并输出数据：：：" + ongoingMeetingList);
                    meetingAdapter = new MeetingAdapter(MeetingPart_MyMeetingActivity.this, ongoingMeetingList);
                    lv_meeting.setAdapter(meetingAdapter);
                    //设置点击事件
                    lv_meeting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            myWorkName=ongoingMeetingList.get( position ).getWorkName();
                            myRole = ongoingMeetingList.get(position).getMyRole();
                            myMeeting = ongoingMeetingList.get(position);
                            Toast.makeText(getApplicationContext(), myRole + "被单击", Toast.LENGTH_SHORT).show();
                            //myRole.equals("普通工作人员");
                            meetingId = myMeeting.getMeetingId();
                            System.out.println("输出点击的回应ID：" + meetingId);
                            if (myRole.equals("普通工作人员")) {
                                Toast.makeText(getApplicationContext(), "角色是普通工作人员！！", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MeetingPart_MyMeetingActivity.this, WorkerActivity.class);
                                i.putExtra("meeting", (Serializable) myMeeting);
                                i.putExtra("meetingId", meetingId + "");
                                i.putExtra( "workName",myWorkName);
                                startActivity(i);
                                //myRole.equals("主管")
                            } else if (myRole.equals("主管")) {
                                Intent i = new Intent(MeetingPart_MyMeetingActivity.this, SupervisorActivity.class);
                                System.out.println("跳转到主管界面！！");
                                System.out.println("跳转到主管界面！！");
                                System.out.println("跳转到主管界面！！");
                                i.putExtra("meeting", myMeeting);
                                i.putExtra("meetingId", meetingId + "");
                                i.putExtra( "workName",myWorkName);
                                i.putExtra( "role",myRole );

                                startActivity(i);
                            } else if (myRole.equals("秘书")) {
                                Intent i = new Intent(MeetingPart_MyMeetingActivity.this, SecretaryMainActivity.class);
                                System.out.println("跳转到秘书界面！！");
                                System.out.println("跳转到秘书界面！！");
                                System.out.println("跳转到秘书界面！！");
                                i.putExtra("meeting", myMeeting);
                                i.putExtra("meetingId", meetingId + "");
                                i.putExtra( "workName",myWorkName);
                                i.putExtra( "role",myRole );
                                startActivity(i);
                            } else {
                                Toast.makeText(MeetingPart_MyMeetingActivity.this, "该会议还未分配角色", Toast.LENGTH_SHORT).show();
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



            }

        };


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
                        Intent i = new Intent(MeetingPart_MyMeetingActivity.this, UpdateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("meeting", myMeeting);
                        bundle.putInt("meetingId", meetingId);
                        i.putExtras(bundle);
                        startActivityForResult(i, 1);
                    } else {
                        System.out.println("会议已开始不能修改！");
                        Toast.makeText(MeetingPart_MyMeetingActivity.this, "此会议已开始不能进行修改！", Toast.LENGTH_SHORT).show();
                        return true;
                    }


                } else {
                    System.out.println("普通工作人员不能修改！");
                    Toast.makeText(MeetingPart_MyMeetingActivity.this, "抱歉，您不能修改此会议！", Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("是否");
                        builder.setMessage("确认删除此会议?");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteMeetThread deleteMeetThread = new DeleteMeetThread(meetingId+"","DeleteMeetingServlet");
                                deleteMeetThread.start();
                                deleteHandler = new Handler() {
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        String data = msg.obj.toString();
                                        System.out.println("删除再次查询到的："+data);
                                        if(data.equals("SUCCESS")){
                                            updateView();
                                            Toast.makeText(MeetingPart_MyMeetingActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(MeetingPart_MyMeetingActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                };


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
                        Toast.makeText(MeetingPart_MyMeetingActivity.this, "此会议已开始不能进行修改！", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                } else {
                    System.out.println("普通工作人员不能删除！");
                    Toast.makeText(MeetingPart_MyMeetingActivity.this, "抱歉，您不能删除此会议！", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void init() {
        tv_default = findViewById(R.id.tv_default);
        lv_meeting = findViewById(R.id.lv_meeting);
        MeetingListNew = new ArrayList<List<MeetingBean>>();
        meetingList = new ArrayList<List<MeetingBean>>();
        ongoingMeetingList = new ArrayList<MeetingBean>();
        overMeetingList = new ArrayList<MeetingBean>();
        gson = new Gson();


        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String data = msg.obj.toString();
                System.out.println("更新再次查询到的："+data);
                MeetingListNew = gson.fromJson(data, new TypeToken<List<List<MeetingBean>>>() {
                }.getType());
                System.out.println("MeetingListNew："+MeetingListNew);
                ongoingMeetingList.clear();
                ongoingMeetingList.addAll(MeetingListNew.get(1));
                System.out.println("ongoingMeetingList："+MeetingListNew.get(1));
                meetingAdapter.notifyDataSetChanged();
            }

        };

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("执行onActivityResult1，主会议里的返回码："+resultCode);

        if (requestCode == 1) {

            updateView();

        }

    }

    public void updateView(){
        String url= Url.URLa+"MeetingPart_MyMeetingServlet";
        map=new HashMap<String,String>();
        map.put("registerMobilePhone","18510801374");
        OkHttpUtils.post(url, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("执行了错误回调", e+"");
                Log.e("执行了错误回调", e+"");
                Log.e("执行了错误回调", e+"");
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String newMeetingList = response.body().string();
                System.out.println("成功获取数据！！！！"+newMeetingList.toString());
                Message message = updateHandler.obtainMessage();
                message.obj = newMeetingList;
                updateHandler.sendMessage(message);

            }
        } ,map);
    }

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
            Message message = deleteHandler.obtainMessage();
            message.obj = response;
            deleteHandler.sendMessage(message);

        }

    }

}

