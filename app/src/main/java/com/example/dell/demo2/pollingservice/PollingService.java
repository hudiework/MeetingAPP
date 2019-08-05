package com.example.dell.demo2.pollingservice;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.dell.demo2.web_url_help.Url;


public class PollingService extends Service {
    public static final String ACTION = "com.example.administrator.a2222.com.example.dell.demo2.pollingservice.PollingService";
    //private Notification mNotification;
    //private NotificationManager mManager;
    //private Notification.Builder mBuilder;
    private PendingIntent mPendingIntent;
    private int messageNotificationID=0;
    private String meetingId="";
    //private List<String> strList=new ArrayList<String>();
    public String str;
    @Override
    public IBinder onBind(Intent intent) {
       // meetingId= intent.getStringExtra("meetingId");

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
      //  initNotifiManager();
      // initIntent();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        meetingId= intent.getStringExtra("meetingId");
        System.out.println("meetingId---------------"+meetingId);
        System.out.println("要传递给服务器的meetingId:"+meetingId);
        System.out.println("要传递给服务器的meetingId:"+meetingId);
        System.out.println("要传递给服务器的meetingId:"+meetingId);
        new PollingThread().start();
        return super.onStartCommand(intent, flags, startId);
    }
/*
    private void initNotifiManager(){
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_launcher_background;
        mBuilder=new Notification.Builder(this);
        mBuilder.setSmallIcon(icon);
    }*/

    @SuppressLint("WrongConstant")
    private void initIntent(){
       /* mIntent= new Intent(this, MessageActivity.class);
        mPendingIntent= PendingIntent.getActivity(this, 0, mIntent,
                Intent.FLAG_ACTIVITY_NEW_TASK);*/
    }
    int count = 0;
    class PollingThread extends Thread{
        public Boolean isRuning=true;

        @Override
        public void run() {
            while(isRuning){

                try {
                    System.out.println("Polling...");
                    //休息5秒钟
                    Thread.sleep(1000000);

                  //  if(serviceMessage!=null){

                        //更新通知栏 或者 做出判断，预警提示！
                      //  mBuilder.setContentTitle("人数预警！");
                        //mBuilder.setContentText(serviceMessage);
                       // mBuilder.setContentIntent(mPendingIntent); //执行intent
                        //mNotification=mBuilder.getNotification();//将builder对象转化成普通的notification
                       // mNotification.flags |= Notification.FLAG_AUTO_CANCEL;//点击通知后通知消失
                       // mManager.notify(messageNotificationID,mNotification);//运行notification
                        //mNotification.setLatestEventInfo(this, "人数预警", serviceMessage, mPendingIntent);
                        //mManager.notify(messageNotificationID, mNotification);
                        //每次通知完，通知id要递增一下，避免消息覆盖掉
                      //  messageNotificationID++;

                    //}
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //5秒钟访问一次服务器
                doService();


            }
        }
    }

    @Override
    public void onDestroy() {
        System.exit(0); //终止服务
        //或者，二选一，推荐使用System.exit(0)，这样进程退出的更干净
        //messageThread.isRunning = false;
        System.out.println("Service:onDestroy");
        super.onDestroy();
    }

    public void doService()
    {
        //访问服务器：
         final String HTTPPATH = Url.URLa+"PollingHTTPServlet?meetingId="+meetingId;
         new Thread(new Runnable() {
             @Override
             public void run() {
                 String strhttp=HttpUtils.httpURLConnectionGetData(HTTPPATH, "utf-8", "text/html");
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
            //发送广播
            Intent intent=new Intent();
           // List<String> list= new ArrayList<String>();
            String jsonStr=msg.obj.toString();
            System.out.println("获取到的服务器传过来的数据组:"+jsonStr);
            System.out.println("获取到的服务器传过来的数据组:"+jsonStr);
            System.out.println("获取到的服务器传过来的数据组:"+jsonStr);
           /* if(list==null){
                intent.putExtra("msgList","nomsg");
            }else{*/
               // Bundle bundle = new Bundle();
               // bundle.putSerializable("msgList", (Serializable) list);
                intent.putExtra("msg",jsonStr);
           // }

            intent.setAction("com.example.dell.demo2.pollingservice.PollingService");
            sendBroadcast(intent);
            super.handleMessage(msg);
        }
    };

}
