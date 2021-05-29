package com.example.test_0527;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;


public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        SharedPreferences sp = getSharedPreferences("loginData",MODE_PRIVATE);
        Boolean isLogin = sp.getBoolean("isLogin",false);
        // 数据传递
//        Intent intent = getIntent();
//        String account = intent.getStringExtra("account");
//        String password = intent.getStringExtra("password");

        Button btn = (Button)findViewById(R.id.button2);
        if(isLogin){
            btn.setText("退出登录");
            String username = sp.getString("username","");
            String password = sp.getString("password","");
            TextView text_account = findViewById(R.id.editTextNumber2);
            TextView text_password = findViewById(R.id.editTextNumber3);
            text_account.setText(username);
            text_password.setText(password);
        }
        else {
            btn.setText("返回登录");
            String username = "未登录";
            String password = "未登录";
            TextView text_account = findViewById(R.id.editTextNumber2);
            TextView text_password = findViewById(R.id.editTextNumber3);
            text_account.setText(username);
            text_password.setText(password);
        }

    }

    // 跳转
    public void skipToMain(View v) {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }

    // 退出登录
    public void logOut(View v){
        SharedPreferences sp = getSharedPreferences("loginData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }


    // Fragment跳转
    public void toFirst(View v){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.Fragment, FirstFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
    // Fragment跳转
    public void toSecond(View v){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.Fragment, SecondFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
    // Fragment跳转
    public void toThird(View v){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.Fragment, ThirdFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
}