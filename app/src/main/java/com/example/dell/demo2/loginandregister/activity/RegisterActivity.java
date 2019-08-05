package com.example.dell.demo2.loginandregister.activity;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.dell.demo2.R;
import com.example.dell.demo2.loginandregister.web.RegistPostService;
import com.example.dell.demo2.web_url_help.Url;

import java.io.File;
import java.util.regex.Pattern;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;


public class RegisterActivity extends Activity implements View.OnClickListener {

    static int CONNECT_FAILED = 1;
    static int REGISTER_FAILED = 2;
    static int REGISTER_SUCCEEDED = 3;

    private Button  btnRegister, btnSend1, btn_change;
    private EditText etRe_phone, etRe_psw, etRe_psw2, et_code1;
    MyCountTimer timer;
    private ImageView iv_personal_icon;
    private ProgressDialog progress;
    // 返回的数据
    private String info;
    // 返回主线程更新数据
    Handler handler;

    private static final String TAG = "RegisterActivity";

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private static String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

    }

    public void init() {

       /* /*//*//* 设置背景图片透明度：
        View tableview = findViewById(R.id.regist_background);
        tableview.getBackground().setAlpha(200);
*/
        //第一：默认初始化
        BmobSMS.initialize(RegisterActivity.this, "d7fa04244a2d2b53a6cfc979979ec1f2");

        btnRegister = findViewById(R.id.btn_register);
        btnSend1 = findViewById(R.id.btn_send1);
        etRe_phone = findViewById(R.id.et_registerphone);
        etRe_psw = findViewById(R.id.et_registerpwd);
        etRe_psw2 = findViewById(R.id.et_registerpwd2);
        et_code1 = findViewById(R.id.et_verify_code1);
       // iv_personal_icon = findViewById(R.id.iv_personal_icon);

        btnRegister.setOnClickListener(RegisterActivity.this);
        btnSend1.setOnClickListener(RegisterActivity.this);
        //iv_personal_icon.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send1:
                requestSMSCode();
                break;
            case R.id.btn_register:
                String code = et_code1.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(RegisterActivity.this, "验证码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    registerUser(code);
                    break;
                }
            /*case R.id.iv_personal_icon:
                showChoosePicDialog();
                break;*/

        }
    }


    private void registerUser(final String security_code) {
        String Register_phone = etRe_phone.getText().toString();
        String Register_psw = etRe_psw.getText().toString();
        String Register_psw2 = etRe_psw2.getText().toString();

        // 检测网络
        if (!checkNetwork()) {
            Toast toast = Toast.makeText(RegisterActivity.this, "网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if (TextUtils.isEmpty(Register_phone)) {
            Toast.makeText(RegisterActivity.this, "手机号不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isPoneAvailable(Register_phone.trim())) {
            System.out.println("看看值：" + !isPoneAvailable(Register_phone));
            Toast.makeText(RegisterActivity.this, "手机号码格式错误！", Toast.LENGTH_SHORT).show();
            return;
        }
        //使密码必须包含小写字母，数字，可以是字母数字下划线组成并且长度是6到16
        Pattern z1_ = Pattern.compile("^(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9_]{6,16}$");
        //判断是否都成立，都包含返回true
        boolean pwd = z1_.matcher(Register_psw).matches();
        if (!pwd) {
            Toast.makeText(RegisterActivity.this, "密码必须包含小写字母，数字，可以是字母数字下划线组成并且长度是6到16！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Register_psw)) {
            Toast.makeText(RegisterActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Register_psw.equals(Register_psw2)) {
            Toast.makeText(RegisterActivity.this, "确认密码不一致!", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
        progress.setMessage("正在验证并注册中...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        BmobSMS.verifySmsCode(RegisterActivity.this, Register_phone, security_code, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    new RegisterPostThread(etRe_phone.getText().toString(),
                            etRe_psw2.getText().toString(), imagePath).start();

                    //Handle,Msg返回成功信息，跳转到其他Activity
                    handler = new Handler() {
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            progress.dismiss();
                            if (msg.what == 222) {  // 处理发送线程传回的消息
                                if (msg.obj != null) {
                                    if (msg.obj.toString().equals("SUCCEEDED")) {
                                        //跳转
                                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if(msg.obj.toString().equals("FAILED")){
                                        Toast.makeText(RegisterActivity.this, "账号已被注册", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "服务器连接失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    };
                } else {
                    Log.v("注册", "有没有e" + e.toString());
                    Toast.makeText(RegisterActivity.this, "验证错误,注册失败" + e.toString(), Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }


            }

        });
    }


    //发送验证码的方法
    private void requestSMSCode() {
        String number = etRe_phone.getText().toString();
        Log.v("注册页面", number);
        if (!TextUtils.isEmpty(number)) {
            timer = new MyCountTimer(60000, 1000);
            timer.start();
            BmobSMS.requestSMSCode(RegisterActivity.this, number, "注册验证码模板", new RequestSMSCodeListener() {

                @Override
                public void done(Integer smsId, BmobException ex) {
                    // TODO Auto-generated method stub
                    if (ex == null) {// 验证码发送成功
                        // 用于查询本次短信发送详情
                        Log.v("注册页面", "发送成功");
                        Toast.makeText(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    } else {//如果验证码发送错误，可停止计时
                        timer.cancel();
                        Log.v("注册页面", ex.toString());
                        Toast.makeText(RegisterActivity.this, "验证码发送失败，原因：" + ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
    }

    // 检测Wifi和网络
    private boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) RegisterActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    //自定义计时器
    public class MyCountTimer extends CountDownTimer {
        public MyCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnSend1.setText((millisUntilFinished / 1000) + "秒后重发");
        }

        @Override
        public void onFinish() {
            btnSend1.setText("重新发送验证码");
        }
    }


    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }


    //注册Thread调用RegisterPostService，返回Msg
    public class RegisterPostThread extends Thread {
        public String mobilephone, password, imagePath;

        public RegisterPostThread(String mobilephone, String password, String imagePath) {
            this.mobilephone = mobilephone;
            this.password = password;
            this.imagePath = imagePath;
        }

        @Override
        public void run() {
            // Sevice传回int
            int responseInt = 0;
            if (!mobilephone.equals("")) {
                /*// 要发送的数据
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("mobilephone", mobilephone));
                params.add(new BasicNameValuePair("password", password));*/
                // 发送数据，获取对象
                String url = Url.URLa + "RegistServlet";
                responseInt = RegistPostService.send(url, mobilephone, password, imagePath);
                Log.i("tag", "RegisterActivity: responseInt = " + responseInt);
                // 准备发送消息
                Message msg = handler.obtainMessage();
                // 设置消息默认值
                msg.what = 222;
                // 服务器返回信息的判断和处理
                if(responseInt == CONNECT_FAILED){
                    msg.obj = "CONNECT_FAILED";
                }else if (responseInt == REGISTER_SUCCEEDED) {
                    msg.obj = "SUCCEEDED";
                }else{
                    msg.obj = "FAILED";
                }
                handler.sendMessage(msg);
            }
        }
    }

    private boolean isPoneAvailable(String number) {
        String myreg = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (!number.matches(myreg)) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 显示修改头像的对话框
     */
    public void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        takePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            //int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            int check=0;

            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment
                .getExternalStorageDirectory(), "image.jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(RegisterActivity.this, "com.lt.uploadpicdemo.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Log.d(TAG, "setImageToView:" + photo);
            photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            iv_personal_icon.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath + "");
        /*if(imagePath != null){
            // 拿着imagePath上传了
            // ...
            Log.d(TAG,"imagePath:"+imagePath);
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，重新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }


}

