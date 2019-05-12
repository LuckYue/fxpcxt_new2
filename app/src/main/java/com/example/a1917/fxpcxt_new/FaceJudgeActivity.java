package com.example.a1917.fxpcxt_new;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.a1917.fxpcxt_new.common.CommonResponse;
import com.example.a1917.fxpcxt_new.util.FileUtil;
import com.google.gson.Gson;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FaceJudgeActivity extends AppCompatActivity {
    private SurfaceView scrollView;
    private Button btn_surfaceLogin;
    public final static int RC_TAKE_PHOTO=1;
    public static String loginResult;

    private String TAG = "摄像头";
    private Button mBtn = null;
    private Camera mcamera = null;
    private SurfaceView mSfview = null;
    private SurfaceHolder mHolder = null; // 控制实例
    private FrameLayout mFrame = null;
    private CameraPreview mPreview = null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_judge);
        //初始化控件
        //initLayout();
        mBtn = (Button)findViewById(R.id.btn);
        mSfview = (SurfaceView)findViewById(R.id.surface_view);
        btn_surfaceLogin=findViewById(R.id.surface_login);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcamera = getCameraInstance();
//                mFrame = (FrameLayout)findViewById(R.id.frame);
                mPreview = new CameraPreview(FaceJudgeActivity.this,mcamera);
//                mFrame.addView(mPreview);
            }
        });

       btn_surfaceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.PictureCallback jpegCallback=new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + ".jpg");
                                        FileOutputStream fos = new FileOutputStream(file);
                                        //旋转角度，保证保存的图片方向是对的
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        Matrix matrix = new Matrix();
                                        matrix.setRotate(90);
                                        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                        fos.flush();
                                        fos.close();
                                        faceLogin(file);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        mcamera.startPreview();
                    }
                };
                mcamera.takePicture(null,null,null,jpegCallback);
            }
        });
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        Camera.Parameters mParameters = null;
        try {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        c = Camera.open(camIdx);
                        break;
                    } catch (RuntimeException e) {

                    }
                }
            }
            c.setDisplayOrientation(90); // 设置摄像头实例的方向
            mParameters = c.getParameters(); // 设置摄像头的参数
            //设置放大倍数
            //mParameters.setZoom(12);
            //开启闪光灯
            //mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            c.setParameters(mParameters);
        }catch (Exception e) {
            Log.d("摄像头", "Error setting camera preview: " + e.getMessage());
        }

        return c;
    }

    // A basic Camera preview class
    class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private Camera mCamera = null;
        public CameraPreview(Context context, Camera camera) {
            super(context);

            mCamera = camera; // 保存

            mHolder = mSfview.getHolder(); //获取控制实例
            //mHolder = getHolder(); //获取控制
            mHolder.addCallback(this); //注册mHolder
            setFocusable(true); //聚焦
            setFocusableInTouchMode(true);
            this.setKeepScreenOn(true); //保持屏幕长亮
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            try {
                mCamera.setPreviewDisplay(mHolder); // 使用 mHolder 去控制预览
                mCamera.startPreview(); // 显示预览
            }catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }


        // surface创建
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // 记住前往不能在这里处理下面的语句
            // 最终虽然程序可以跑起来，但是但点击按钮的时候，我们是看不到摄像头预览的

//            mHolder.addCallback(this); //注册mHolder
//
//            try {
//                mCamera.setPreviewDisplay(mHolder);
//                mCamera.startPreview();
//            }catch (IOException e) {
//                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
//            }
        }
        // surface销毁
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.stopPreview(); // 在surfaceView销毁的时候，我们可以停掉预览
            //camera.setPreviewCallback(null);
            mCamera.release(); // 释放摄像头
            mCamera = null; // 将实例置空
            mBtn.setText("终止"); // 更新UI
        }

        // surface 状态改变
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
            if(mHolder.getSurface() == null) {
                return;
            }

            try {
                mCamera.stopPreview(); // 停止预览
            }catch (Exception e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());

            }

            try {
                mCamera.setPreviewDisplay(mHolder); // 再次预览
                mCamera.stopPreview();
            }catch (Exception e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

    }
    public void faceLogin(File file){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/face/getUserByFace";
                try {
                    login(url,file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void login(String url,File file) throws IOException {
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(180,TimeUnit.SECONDS)
                .readTimeout(180,TimeUnit.SECONDS)
                .build();
        if(file != null){
            //OkHttpClient client=new OkHttpClient();
            String fileName = file.getName();
            RequestBody body=RequestBody.create(MediaType.parse("image/jpg"),file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName, body)
                    .build();
            Request request=new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                CommonResponse commonRespones = new Gson().fromJson(response.body().string(),CommonResponse.class);
                if(commonRespones.isSuccess()){
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "人脸登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(FaceJudgeActivity.this,UserMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else{
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "人脸验证失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }else {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "请换种方式登录", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        }
    }
}
