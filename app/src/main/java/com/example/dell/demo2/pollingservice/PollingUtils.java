package com.example.dell.demo2.pollingservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Administrator on 2018/3/15.
 */

public class PollingUtils {
    public static void startPollingService(Context context, int seconds, Class<?> cls, String action,String meetingId){
        //获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        //包装需要执行service的Intent
        Intent intent = new Intent(context, cls);
        intent.putExtra("meetingId","1");
        intent.setAction(action);
        //intent.putExtra("meetingId",meetingId);FLAG_CANCEL_CURRENT
        //getService(Context context, int requestCode, Intent intent, int flags)
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //触发服务的起始时间
        long triggerAtTime = SystemClock.elapsedRealtime();//返回的是系统从启动到现在的时间

        //使用AlarmManager的setRepeating方法设置定期执行的时间间隔和需要执行的service
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
                seconds * 5000, pendingIntent);
        System.out.println("开启服务....");
    }


    //关闭轮询服务
    public static void stopPollingService(Context context, Class<?> cls,String action){
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的轮询服务
        manager.cancel(pendingIntent);

    }


}


