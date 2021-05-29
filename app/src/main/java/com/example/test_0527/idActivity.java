package com.example.test_0527;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;

public class idActivity extends AppCompatActivity {

    ImageView imageView;
    Uri imageUri;
    Handler handler;
    Handler handler1;
    String username;
    String id;
    String address;
    String picBase64;
    TextView tv_name;
    TextView tv_id;
    TextView tv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        imageView = (ImageView) findViewById(R.id.imageView2);
        tv_name = (TextView) findViewById(R.id.textView4);
        tv_id = (TextView) findViewById(R.id.textView5);
        tv_address = (TextView) findViewById(R.id.textView6);

        handler = new Handler(){
            public void handleMessage(Message msg) {
                try {
                    JSONObject json = new JSONObject((String)msg.obj);
                    username=json.getJSONObject("words_result")
                            .getJSONObject("姓名").
                                    getString("words");
                    id=json.getJSONObject("words_result")
                            .getJSONObject("公民身份号码").
                                    getString("words");
                    address=json.getJSONObject("words_result")
                            .getJSONObject("住址").
                                    getString("words");
                    tv_name.setText("姓名："+username);
                    tv_id.setText("身份号码："+id);
                    tv_address.setText("住址："+address);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            };
        };

        handler1=new Handler(){
            public void handleMessage(Message msg) {
                //tv_address.setText((String)msg.obj);
                Toast.makeText(idActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
            };
        };
        //兼容24以下相机
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ////builder.detectFileUriExposure();
    }

    public void openCameraSaveToLocal(View v){
        String fileName = System.currentTimeMillis() + ".jpg"; // 使用系统时间来对照片进行命名，保证唯一性
        File outputImage = new File(getExternalCacheDir(), fileName);
        System.out.println(getExternalCacheDir());
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        //判断版本号
        //if (Build.VERSION.SDK_INT < 24) {
        imageUri = Uri.fromFile(outputImage);
        // } else {  //manifest加provider，xml文件夹加file文件
        //    imageUri = FileProvider.getUriForFile(IDActivity.this,
        //             "cn.edu.upc.cs.ljh.myapplication1", outputImage);
        // }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);

    }
    public void openCamera(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 关键：新建相机的 Intent
        startActivityForResult(intent, 1); // 加载相机 Activity
    }
    public void openLocalPic(View v){
        //打开本地相册
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //设定结果返回
        startActivityForResult(i, 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm=null;
        try{
            if (requestCode == 1 && resultCode == RESULT_OK) {
                bm = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bm);
                String result=MyTools.bitmapToBase64(bm);
            }else if (requestCode == 2 && resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                //电脑端虚拟机测试用，安装手机前注释掉
                //// bm = BitmapFactory.decodeResource(getResources(),R.drawable.myid );
                imageView.setImageBitmap(bm);
            }
            picBase64=MyTools.bitmapToBase64(bm);
            final Map<String,String> params = new HashMap<String,String>();
            params.put("image", picBase64);
            params.put("id_card_side", "front");
            new Thread(new Runnable() {//创建子线程
                public void run() {
                    try {
                        //服务器请求路径
                        String strUrlPath = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?access_token=24.fade988b83c7089b31136934274ab0d3.2592000.1624606645.282335-23815654";
                        String strResult=MyTools.submitPostData(strUrlPath,params, "utf-8");
                        Message msg = Message.obtain();
                        msg.obj = strResult;//传递的数据
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();//启动子线程
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void upload(View v){
        final Map<String,String> params = new HashMap<String,String>();
        params.put("pic", picBase64);
        params.put("username", username);
        params.put("id", id);
        params.put("address", address);
        new Thread(new Runnable() {//创建子线程
            public void run() {
                try{
                    String strUrlPath = "http://58.87.74.112:8080/2016/android/saveIDInfo.jsp";
                    final String strResult=MyTools.submitPostData(strUrlPath,params, "utf-8");
                    Message msg = Message.obtain();
                    msg.obj = strResult;//传递的数据
                    handler1.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


}