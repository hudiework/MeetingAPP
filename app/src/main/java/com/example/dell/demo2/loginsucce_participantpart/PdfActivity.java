package com.example.dell.demo2.loginsucce_participantpart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

public class PdfActivity extends AppCompatActivity {
    private TextView tv_myPdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        init();
    }

    /**
     * 初始化控件
     */
    public void init(){
        tv_myPdf=findViewById(R.id.tv_myPdf);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        readPdfContent(path);
    }


    /**
     * 读取Pdf文件的内容
     * @param path :文件地址
     */
    public void readPdfContent(String path){
        try {
            PdfReader pr = new PdfReader(path);
            int page = pr.getNumberOfPages();
            String content = "";
            for(int i = 1 ;i<page+1;i++){
                content += PdfTextExtractor.getTextFromPage(pr, i);
            }
            tv_myPdf.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
