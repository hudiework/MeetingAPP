package com.example.dell.demo2.loginsucce_participantpart.PayAndBill;

import android.app.Dialog;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.demo2.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateBillMainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_createBill;//生成电子发票的按钮
    private EditText et_identify_num, et_identify_name;//输入“纳税识别号”，“纳税人姓名”
    private Dialog myDialog; // 保存进度框
    private String msg;//这是用户填写的信息，用来生成文件

    private static final int PDF_SAVE_START = 1;// 保存PDF文件的开始意图
    private static final int PDF_SAVE_RESULT = 2;// 保存PDF文件的结果开始意图
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill_main);
        init();
    }


    //保存中....信息提示
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PDF_SAVE_START: //保存PDF文件开始意图
                    if (!myDialog.isShowing())
                        LoadingDialogUtils.startDialog(myDialog);
                    break;

                case PDF_SAVE_RESULT://保存PDF文件的结果开始意图
                    if (myDialog.isShowing())
                        LoadingDialogUtils.closeDialog(myDialog);
                    Toast.makeText(CreateBillMainActivity.this, "保存成功", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
            return false;
        }
    });


    //初始化控件
    public void init() {
        btn_createBill = findViewById(R.id.btn_createBill);
        et_identify_num = findViewById(R.id.et_identify_num);
        et_identify_name = findViewById(R.id.et_identify_name);
        btn_createBill.setOnClickListener(this);

        initProgress();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_createBill: {//点击了“生成电子发票按钮”
                turnToPdf();
            }
            break;
        }
    }


    /**
     * 识别用户输入结果，并生成PDF文件
     */
    private void turnToPdf() {
        String identifyNum = et_identify_num.getText().toString();
        String identifyName = et_identify_name.getText().toString();
        //判断用户输入是否合法（***********没有加正则表达式，最后记着加！！！***）
        if (identifyNum.equals("") || identifyNum == null || identifyName.equals("") || identifyName == null) {
            Toast.makeText(this, "请完善信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        msg = "纳税人识别号：" +identifyNum+ ",纳税人名称:" + identifyName;
        File file = new File(PdfUtils.ADDRESS);
        if (!file.exists())
            file.mkdirs();
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String pdf_address = PdfUtils.ADDRESS + File.separator + "PDF_"
                + sdf.format(date) + ".pdf";//生成文件名
        handler.sendEmptyMessage(PDF_SAVE_START);//开始保存文件。
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                Document doc = new Document();// 创建一个document对象
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(pdf_address); // pdf_address为Pdf文件保存到sd卡的路径
                    PdfWriter.getInstance(doc, fos);
                    doc.open();
                    doc.setPageCount(1);
                    boolean dd= doc.add(new Paragraph(msg,setChineseFont())); // msg为保存的字符串
                    // ,setChineseFont()为pdf字体
                    // 一定要记得关闭document对象
                    System.out.println("保存成功啦啦啦啦啦！！！！"+dd);
                    System.out.println("保存成功啦啦啦啦啦！！！！"+dd);
                    System.out.println("保存成功啦啦啦啦啦！！！！"+dd);
                    System.out.println("保存成功啦啦啦啦啦！！！！");
                    doc.close();
                    fos.flush();
                    fos.close();
                    handler.sendEmptyMessage(PDF_SAVE_RESULT);//文件保存成功，关闭动画
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 设置PDF字体(较为耗时)
     */
    public Font setChineseFont() {
        BaseFont bf = null;
        Font fontChinese = null;
        try {
            // STSong-Light : Adobe的字体
            // UniGB-UCS2-H : pdf 字体
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                    BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bf, 12, Font.NORMAL);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }


    /**
     * 初始化识别进度框
     */
    private void initProgress() {
        myDialog = LoadingDialogUtils.initWaitDialog(CreateBillMainActivity.this,
                "正在保存电子发票...", false, true);
    }
}
