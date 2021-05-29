package com.example.test_0527;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler;
    private HttpRequest httpRequest;
    String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Http Request
        httpRequest = new HttpRequest();
        mHandler=new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    status = jsonObject.get("status").toString();
                    System.out.println(status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaa"+msg.obj);
                if(status.equals("fail"))
                    Toast.makeText(getApplicationContext(),"用户名已经被注册",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_LONG).show();
            }
        };
    }

    // 物理按键事件处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            Toast.makeText(this,"Volume Down",Toast.LENGTH_LONG).show();
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            Toast.makeText(this,"Volume Up",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    // 按钮_点我
    public void clickMe(View v){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("AlertDialog")
                .setIcon(R.drawable.h2)
                .setMessage("再点一下")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Yes",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_LONG).show();
                    }
                })
                .create();
        dialog.show();
    }

    // 按钮_登录
    public void login(View v) {

        SharedPreferences sp = getSharedPreferences("loginData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        TextView account = findViewById(R.id.editTextNumber);
        TextView password = findViewById(R.id.editTextTextPassword);

        editor.putString("username",account.getText().toString());
        editor.putString("password",password.getText().toString());
        editor.putBoolean("isLogin",true);
        editor.commit();

        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        // intent.putExtra("account",account.getText().toString());
        // intent.putExtra("password",password.getText().toString());
        Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    // 跳转至MainActivity2
    public void skip(View v) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }

    // 时间
    public void timeHandle(View v) {
        Intent intent = new Intent(MainActivity.this, MainActivity3.class);
        startActivity(intent);
    }

    // 数据存储
    public void saveDate(View v) throws IOException {
        String fileName = "data.txt";                       // 文件名称
        String content = "hello_world";                     // 保存数据
        FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
        fos.write(content.getBytes());
        fos.close();
        Toast.makeText(getApplicationContext(),"file saving",Toast.LENGTH_LONG).show();
    }

    //  有参请求
    public void httpPost(View v) {
        // {"studentId":"","hometown":"","password":"aaa","name":"","id":6,"hobby":"","username":"ljh"}
        HashMap map = new HashMap();
        map.put("studentId","1808030219");
        map.put("hometown","Hunan");
        map.put("password","zmq");
        map.put("name","周孟卿");
        map.put("hobby","photography");
        map.put("username","zmq");
        // http://www.jnlxsyj.com:8086/android/getAllData
        httpRequest.postRequest("http://124.128.84.40:8086/android/wAndroidInsert",map,mHandler);


    }

    // ID
    public void skipToID(View v) {
        Intent intent = new Intent(MainActivity.this, idActivity.class);
        startActivity(intent);
    }

}