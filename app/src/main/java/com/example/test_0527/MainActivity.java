package com.example.test_0527;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    public void click_me(View v){
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
        TextView account = findViewById(R.id.editTextNumber);
        TextView password = findViewById(R.id.editTextTextPassword);

        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("account",account.getText().toString());
        intent.putExtra("password",password.getText().toString());

        startActivity(intent);
    }

    public void handle(View v) {
        Intent intent = new Intent(MainActivity.this, MainActivity3.class);
        startActivity(intent);
    }

    public void save_date(View v) throws IOException {
        String fileName = "data.txt";                       // 文件名称
        String content = "hello_world";                     // 保存数据
        FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
        fos.write(content.getBytes());
        fos.close();
        Toast.makeText(getApplicationContext(),"file saving",Toast.LENGTH_LONG).show();
    }

}