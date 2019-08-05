package com.example.dell.demo2.QRcode;

/**
 * Created by hh on 2018/4/27.
 */


import android.app.Application;


import com.uuzuche.lib_zxing.activity.ZXingLibrary;



public class MApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ZXingLibrary.initDisplayOpinion(this);
    }
}
