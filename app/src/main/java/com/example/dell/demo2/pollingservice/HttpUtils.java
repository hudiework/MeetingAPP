package com.example.dell.demo2.pollingservice;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2018/3/15.
 */

public class HttpUtils {
    public static int BUFFER_SIZE = 4096;
    private static final String TAG = "httpUtils";
    private static final int TIME_OUT = 10 * 10000000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

    //用get方法获取服务器数据
    public static <T> String httpURLConnectionGetData(String httpPathString, String encodeString, String contentType) {
        String responseString = "";
        System.out.println("httpPathString-->" + httpPathString);
        try {
            URL url = new URL(httpPathString);// 创建URL对象
            URLConnection rulConnection = url.openConnection();// 生成链接对象
            HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;// 生成链接对象
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpUrlConnection.setDoInput(true);
            // Post 请求不能使用缓存
            httpUrlConnection.setUseCaches(true);
            // 设置请求超时时间
            httpUrlConnection.setReadTimeout(5 * 1000);
            // 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
            httpUrlConnection.connect();
            if (httpUrlConnection.getResponseCode() == httpUrlConnection.HTTP_OK) {
                // 调用HttpURLConnection连接对象的getInputStream()函数,
                // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
                InputStream inStrm = httpUrlConnection.getInputStream(); // <===注意，实际发送请求的代码段就在这里
                // str=inStrm.read()+"";
                // 上边的httpConn.getInputStream()方法已调用,本次HTTP请求已结束,下边向对象输出流的输出已无意义，
                // 既使对象输出流没有调用close()方法，下边的操作也不会向对象输出流写入任何数据.
                // 因此，要重新发送数据时需要重新创建连接、重新设参数、重新创建流对象、重新写数据、
                // 重新发送数据(至于是否不用重新这些操作需要再研究)
                responseString = inputStreamTOString(inStrm, encodeString);
                inStrm.close();
            }
            httpUrlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return responseString;
    }
    /**
     * 将InputStream转换成String
     * @throws Exception
     */
    public static String inputStreamTOString(InputStream in, String encodeString) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);
        return new String(outStream.toByteArray());
    }


}
