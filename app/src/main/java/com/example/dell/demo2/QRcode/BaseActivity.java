package com.example.dell.demo2.QRcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.dell.demo2.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}

