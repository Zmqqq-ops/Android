package com.example.test_0527;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity3 extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        tv=(TextView)findViewById(R.id.textViewTime);

        new Thread(){
            public void run(){
                while (true){
                    refreshMSG();
                }
            }
        }.start();
    }

    public void refreshMSG(){
        try{
            Thread.sleep(500);
        }
        catch (InterruptedException e){}
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String dd=df.format(new Date());
        Message message = Message.obtain();
        message.obj=dd;
        mHandler.sendMessage(message);
    }

    Handler mHandler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            tv.setText(msg.obj+"");
        }
    };

    public void skipToMain(View v) {
        Intent intent = new Intent(MainActivity3.this, MainActivity.class);
        startActivity(intent);
    }

//    // Sql
//    public void insert(String name,String price) {
//        MyHelper helper = new MyHelper(MainActivity3.this);
//        SQLiteDatabase db = helper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("name", name);
//        values.put("price", price);
//        long id = db.insert("information",null,values);
//        db.close();
//    }
//
//    public int delete(long id){
//        MyHelper helper = new MyHelper(MainActivity3.this);
//        SQLiteDatabase db = helper.getWritableDatabase();
//        int number = db.delete("information", "_id=?", new String[]{id+""});
//        db.close();
//        return number;
//    }
//
//    public int update(String name, String price) {
//        MyHelper helper = new MyHelper(MainActivity3.this);
//        SQLiteDatabase db = helper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("price", price);
//        int number = db.update("information", values, " name =?", new        	String[]{name});
//        db.close();
//        return number;
//    }
//
//    public void find(int id){
//        MyHelper helper = new MyHelper(MainActivity3.this);
//        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.query("information", null, "id=?", new String[]{id+""},null, null, null);
//        if (cursor.getCount() != 0){
//            while (cursor.moveToNext()){
//                String _id = cursor.getString(cursor.getColumnIndex("id"));
//                String name = cursor.getString(cursor.getColumnIndex("name"));
//                String price = cursor.getString(cursor.getColumnIndex("price"));
//            }
//        }
//        cursor.close();
//        db.close();
//    }
//
//    public void My_insert(View v){
//        insert("TEST","12333");
//        Toast.makeText(getApplicationContext(),"插入成功",Toast.LENGTH_LONG).show();
//    }
}