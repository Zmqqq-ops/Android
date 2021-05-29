package com.example.test_0527;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class HttpRequest {

    public void getRequest(String url, Handler mHandler){
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL URL = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) URL.openConnection();
                    conn.setConnectTimeout(1000);
                    // 此处应该是GET 但后端都是用的POST 所以这个方法就用做无参数的POST请求
                    conn.setRequestMethod("POST");
                    int code = conn.getResponseCode();
                    System.out.println(code);
                    Message msg = Message.obtain();
                    if (code == 200) {
                        conn.getResponseMessage();
                        InputStream is = conn.getInputStream();
                        byte[] data = copyStream(is);
                        String res = new String(data, "UTF-8");
                        JSONObject jsonRes = new JSONObject(res);
                        // 成功
                        if(jsonRes.get("status").toString().equals("success")){
                            msg.arg1 = 1;
                            JSONArray array = new JSONArray(jsonRes.get("data").toString());
                            msg.obj= array;
                            mHandler.sendMessage(msg);
                            JSONObject object = array.getJSONObject(0);
                            System.out.println(object.get("identity"));
                        }else{
                            msg.arg1 = 0;
                        }

                    }
                    conn.disconnect();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //  请求参数利用map 将其传入, 查询结果放置在mag.obj上
    public void postRequest(String url, Map map, Handler mHandler){
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL URL = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) URL.openConnection();
                    conn.setConnectTimeout(8000);
                    conn.setRequestMethod("POST");
                    String data = new JSONObject(map).toString();
                    Log.i("data",data);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Content-Length", data.getBytes().length + "");
                    Log.i("dataLenth",String.valueOf(data.getBytes().length));
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    OutputStream os = conn.getOutputStream();
                    os.write(data.getBytes());
                    int code = conn.getResponseCode();
                    Log.i("code",String.valueOf(code));
                    if (code == 200) {
                        conn.getResponseMessage();
                        InputStream is = conn.getInputStream();
                        byte[] dataRes = copyStream(is);
                        String res = new String(dataRes, "UTF-8");
                        Log.i("res",res);
                        Message msg = Message.obtain();
                        JSONObject jsonRes = new JSONObject(res);
                        // 返回成功
                        if(jsonRes.get("status").toString().equals("success")){
                            msg.arg1 = 1;
                            if(jsonRes.get("data")!=null||jsonRes.get("data").toString().equals("null")){
                                JSONArray array = new JSONArray(jsonRes.get("data").toString());
                                // 这里传过去的是data中的数据,以JSONArray的形式
                                msg.obj= array;
                                mHandler.sendMessage(msg);
                                JSONObject object = array.getJSONObject(0);
                                System.out.println(object.get("identity"));
                            }
                        }else{
                            // 返回失败
                            msg.arg1 = 0;
                        }
                    }
                    conn.disconnect();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static byte[] copyStream(InputStream input)
            throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = input.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        input.close();
        return outStream.toByteArray();
    }

}
