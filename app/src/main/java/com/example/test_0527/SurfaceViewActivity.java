package com.example.test_0527;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.hardware.Camera;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SurfaceViewActivity extends AppCompatActivity {

    private SurfaceView sfv_preview;
    private Button btn_take;
    private Camera camera = null;
    private int num=0;
    private SurfaceHolder.Callback cpHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            startPreview();
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopPreview();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);
        sfv_preview = (SurfaceView) findViewById(R.id.sfv_preview);
        btn_take = (Button) findViewById(R.id.btn_take);
        sfv_preview.getHolder().addCallback(cpHolderCallback);

        btn_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        String path = "";

                        if ((path = saveFile(data)) != null) {
//                            Intent it = new Intent(SurfaceviewActivity.this, PreviewActivity.class);
//                            it.putExtra("path", path);
//                            startActivity(it);
                            ImageView iv1=(ImageView)findViewById(R.id.imageView4);
                            ImageView iv2=(ImageView)findViewById(R.id.imageView5);
                            ImageView iv3=(ImageView)findViewById(R.id.imageView6);
                            if(num==1){iv1.setImageURI(Uri.fromFile(new File(path)));
                                iv1.setRotation(90);}
                            if(num==2){iv2.setImageURI(Uri.fromFile(new File(path)));
                                iv2.setRotation(90);}
                            if(num==3){iv3.setImageURI(Uri.fromFile(new File(path)));
                                iv3.setRotation(90);}
                        } else {
                            Toast.makeText(SurfaceViewActivity.this, "保存照片失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //保存临时文件的方法
    private String saveFile(byte[] bytes){
        try {
            // File file = File.createTempFile("img","");
            File file=new File(getExternalCacheDir(),System.currentTimeMillis()+".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    //开始预览
    private void startPreview(){
        camera = Camera.open();
        try {
            camera.setPreviewDisplay(sfv_preview.getHolder());
            camera.setDisplayOrientation(90);   //让相机旋转90度
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //停止预览
    private void stopPreview() {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

}