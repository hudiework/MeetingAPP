package com.example.dell.demo2.supervisor;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.example.dell.demo2.pollingservice.HttpUtils;
import com.example.dell.demo2.supervisor.beans.WorkerBean;
import com.example.dell.demo2.web_url_help.Url;

import java.util.ArrayList;
import java.util.List;



public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private List<WorkerBean> list;
    private List<String> functionList=new ArrayList<String>();
    public MyAdapter(Context context,List<WorkerBean> list){
        this.mContext=context;
        this.list=list;

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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        functionList=list.get(position).getFunctionList();

       if(convertView==null){
           convertView=View.inflate(mContext, R.layout.item_listview,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_worker_Name=(TextView) convertView.findViewById(R.id.tv_worker_name);
            viewHolder.btn_car_rent=(Button) convertView.findViewById(R.id.btn_car_rent);
           viewHolder.btn_hotel_reserve=(Button) convertView.findViewById(R.id.btn_hotel_reserve);
           viewHolder.btn_place_reserve=(Button) convertView.findViewById(R.id.btn_place_reserve);
           viewHolder.btn_sign=(Button) convertView.findViewById(R.id.btn_sign);
           for (int i=0;i<functionList.size();i++) {
               System.out.println("打印输出functionlist 我就不信了！！！:"+functionList.toString());
               if (functionList.get(i) .equals("1")) {
                   viewHolder.btn_hotel_reserve.setBackgroundColor(Color.parseColor("#FF6A6A"));
               } else if (functionList.get(i).equals("2")) {
                viewHolder.btn_place_reserve.setBackgroundColor(Color.parseColor("#FF6A6A"));
            } else if (functionList.get(i) .equals("3")) {
               viewHolder.btn_car_rent.setBackgroundColor(Color.parseColor("#FF6A6A"));
            } else if(functionList.get(i) .equals("4")) {
                   viewHolder.btn_sign.setBackgroundColor(Color.parseColor("#FF6A6A"));
               }//根据是否拥有该权限设置不同的背景颜色
           }
       convertView.setTag(viewHolder);
       }else{
           viewHolder=(ViewHolder) convertView.getTag();
       }
        System.out.println("打印输出现在的位置position 我就不信了！！！:"+position);


       viewHolder.tv_worker_Name.setText(list.get(position).getWorkerName());
       viewHolder.btn_car_rent.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              String workerId= list.get(position).getWorkerId();
              String functionId= (String) view.getTag();
               ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();;
               int valid = colorDrawable.getColor();
              Toast.makeText(mContext,valid+"",Toast.LENGTH_SHORT).show();
               myDialog(functionId,workerId,valid,view);
           }
       });
       viewHolder.btn_hotel_reserve.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String workerId= list.get(position).getWorkerId();
               String functionId= (String) view.getTag();
               ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();;
               int valid = colorDrawable.getColor();
               Toast.makeText(mContext,valid+"",Toast.LENGTH_SHORT).show();
               myDialog(functionId,workerId,valid,view);
           }
       });
       viewHolder.btn_place_reserve.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String workerId= list.get(position).getWorkerId();
               String functionId= (String) view.getTag();
               ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();;
               int valid = colorDrawable.getColor();
               Toast.makeText(mContext,valid+"",Toast.LENGTH_SHORT).show();
               myDialog(functionId,workerId,valid,view);
           }
       });
       viewHolder.btn_sign.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String workerId= list.get(position).getWorkerId();
               String functionId= (String) view.getTag();
               ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();;
               int valid = colorDrawable.getColor();
               Toast.makeText(mContext,valid+"",Toast.LENGTH_SHORT).show();
               myDialog(functionId,workerId,valid,view);
           }
       });
/**
 * “1”代表酒店预订
 * “2”代表场地预订
 * “3”代表车辆租赁
 * “4”代表扫码签到
 */
        return convertView;
    }

    public  class ViewHolder{
        TextView tv_worker_Name;
        Button btn_hotel_reserve;
        Button btn_place_reserve;
        Button btn_car_rent;
        Button btn_sign;

    }



    public void myDialog(final String functionId, final String workerId, final int valid, final View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("更改权限：");
        dialog.setCancelable(false);
    if(valid==-38294) {//当该拥有该权限时，点击是剥夺该权限!
        dialog.setMessage("确定要取消该权限吗？");
    }else{
        dialog.setMessage("确定要增加该权限吗？");
    }
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //访问服务器：
                final String HTTPPATH = Url.URLa+"ChangeLimitServlet?workerId="+workerId+"&functionId="+functionId;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String strhttp= HttpUtils.httpURLConnectionGetData(HTTPPATH, "utf-8", "text/html");
                        Message message=new Message();
                        message.obj=strhttp;
                        handler.sendMessage(message);
                    }
                }).start();
            }
            @SuppressLint("HandlerLeak")
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    System.out.println("更改权限后的返回值：msg.obj.toString()"+msg.obj.toString());
                    System.out.println("更改权限后的返回值：msg.obj.toString()"+msg.obj.toString());
                    System.out.println("更改权限后的返回值：msg.obj.toString()"+msg.obj.toString());
                    System.out.println("更改权限后的返回值：msg.obj.toString()"+msg.obj.toString());
                    if(msg.obj.toString().equals("true")){
                        Toast.makeText(mContext,"更改成功！",Toast.LENGTH_SHORT).show();
                        if(valid==-38294){
                            view.setBackgroundColor(Color.parseColor("#8E8E8E"));
                        }else{
                            view.setBackgroundColor(Color.parseColor("#FF6A6A"));
                        }

                    }else{
                        Toast.makeText(mContext,"更改失败！！",Toast.LENGTH_SHORT).show();
                    }
                    super.handleMessage(msg);
                }
            };

        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    }









