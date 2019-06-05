package com.example.a1917.fxpcxt_new;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.a1917.fxpcxt_new.entity.HazardClearRecords;
import com.example.a1917.fxpcxt_new.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecheckActivity extends AppCompatActivity {
    private EditText danger_status,danger_level,danger_changerName,danger_changeReception,danger_inspectionBasis;
    private TextView danger_id,danger_hazardName,danger_enterpriseName,danger_CheckerName,danger_checkReception,danger_checkTime,danger_changeTime,danger_hazardType;
    private ImageView danger_checkImage,danger_changeImage,imageView,tempImageViem;
    private Button danger_save,danger_delete,downloadCheckImage,downloadChangeImage;
    HazardClearRecords hazardClearRecords=new HazardClearRecords();
    public final static int IS_SUCCESS=1;
    public final static int IS_FAIL=0;
    public final static int RC_TAKE_PHOTO=1;
    public final static int RC_CHOOSE_PHOTO=2;
    private String checkImagePath,changeImagePath;
    private static String updateResult=null,deleteResult=null,result;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(180,TimeUnit.SECONDS)
            .readTimeout(180,TimeUnit.SECONDS)
            .build();
    private static Boolean isFirst=true;
    private static Boolean isReturn=false;
    private static File firstFile,secondFile;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IS_SUCCESS:
                    byte[] bytes = (byte[]) msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);
                    break;
                case IS_FAIL:
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recheck);
        //接收fragment中传过来的对象
        Intent intent=getIntent();
        if(intent != null){
            hazardClearRecords=(HazardClearRecords)intent.getSerializableExtra("hazardClearRecords");
            //初始化控件
            danger_id=findViewById(R.id.danger_id2);
            danger_hazardName=findViewById(R.id.danger_hazardName2);
            danger_enterpriseName=findViewById(R.id.danger_enterpriseName2);
            danger_CheckerName=findViewById(R.id.danger_CheckerName2);
            danger_checkReception=findViewById(R.id.danger_checkReception2);
            danger_checkTime=findViewById(R.id.danger_checkTime2);
            danger_status=findViewById(R.id.danger_status2);
            danger_level=findViewById(R.id.danger_level2);
            danger_changerName=findViewById(R.id.danger_changerName2);
            danger_changeReception=findViewById(R.id.danger_changeReception2);
            danger_changeTime=findViewById(R.id.danger_changeTime2);

            danger_checkImage=findViewById(R.id.danger_checkImage2);
            danger_changeImage=findViewById(R.id.danger_changeImage2);

            //downloadCheckImage=findViewById(R.id.downloadCheckImage);
            //downloadChangeImage=findViewById(R.id.downloadChangeImage);
            danger_hazardType=findViewById(R.id.danger_hazardType2);
            danger_inspectionBasis=findViewById(R.id.danger_inspectionBasis2);
            danger_save=findViewById(R.id.danger_save2);
            danger_delete=findViewById(R.id.danger_delete2);
            //给各个控件赋值
            danger_id.setText(hazardClearRecords.getId()+"");
            danger_hazardName.setText(hazardClearRecords.getHazardName());
            danger_enterpriseName.setText(hazardClearRecords.getEnterpriseName());
            danger_CheckerName.setText(hazardClearRecords.getCheckerName());
            danger_checkReception.setText(hazardClearRecords.getCheckReception());
            danger_checkTime.setText(hazardClearRecords.getCheckTime().toString());
            danger_status.setText(hazardClearRecords.getStatus().toString());
            danger_level.setText(hazardClearRecords.getHazardLevel());
            danger_changerName.setText(hazardClearRecords.getChangerName());
            danger_changeReception.setText(hazardClearRecords.getChangeReception());
            danger_changeTime.setText(hazardClearRecords.getChangeTime().toString());
            danger_hazardType.setText(hazardClearRecords.getHazardType());
            danger_inspectionBasis.setText(hazardClearRecords.getInspectionBasis());
            if(hazardClearRecords.getCheckImg()!=null && !"".equals(hazardClearRecords.getCheckImg())){
                //直接下载图片显示在imageview中
                getCheckImgUrl();
            }else{
                Toast.makeText(this, "上传排查图片", Toast.LENGTH_SHORT).show();
                //上传新的排查图片
                danger_checkImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tempImageViem=danger_checkImage;
                        isFirst=true;
                        showPopueWindow();
                       /* File file=new File(photoPath);
                        //上传图片获得图片路径
                        getImagePath(file);
                        hazardClearRecords.setCheckImg(result);*/
                        //checkImagePath=photoPath;
                    }
                });
            }
            if(hazardClearRecords.getChangeImg()!=null && !"".equals(hazardClearRecords.getChangeImg())){
                getChangeImgUrl();
            }else{
                Toast.makeText(this, "上传整改图片", Toast.LENGTH_SHORT).show();
                //上传整改图片
                danger_changeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tempImageViem=danger_changeImage;
                        isFirst=false;
                        showPopueWindow();
                       /* File file=new File(photoPath);
                        getImagePath(file);
                        hazardClearRecords.setChangeImg(result);*/
                        //changeImagePath=photoPath;
                    }
                });

            }
        }

        //保存的触发事件
        danger_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //获取所有的数据放入实体类中
                initInfo();
                hazardClearRecords.setStatus("完成");
                hazardClearRecords.setCheckStatus("审核结束");
                Log.e("firstFile", new Gson().toJson(firstFile) );
                Log.e("secordFile", new Gson().toJson(secondFile) );
                //将获取的实体类信息传到后台
                String url = "http://192.168.43.200:7001/hazardclearancerecords/update";
                updateNew(url);
                if(updateResult!=null){
                    Toast.makeText(RecheckActivity.this, "整改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        //删除的触发事件
        danger_delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //获取id，传入后台删除这条记录
                initInfo();
                hazardClearRecords.setStatus("已整改");
                hazardClearRecords.setCheckStatus("不通过");
                String url = "http://192.168.43.200:7001/hazardclearancerecords/update";
                //deleteRecord(hazardClearRecords);
                updateNew(url);

                if(deleteResult!=null){
                    Toast.makeText(RecheckActivity.this, "整改不成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        //下载排查图片的触发事件
       /* downloadCheckImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hazardClearRecords.getCheckImg()==null){
                    Toast.makeText(DangerItemActivity.this, "图片还未上传", Toast.LENGTH_SHORT).show();
                }else {
                    //得到图片地址
                    getCheckImgUrl();
                }
            }
        });*/



        //下载整改图片的触发事件
        /*downloadChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hazardClearRecords.getChangeImg()==null){
                    Toast.makeText(DangerItemActivity.this, "图片还未上传", Toast.LENGTH_SHORT).show();
                } else{
                    //得到图片地址
                    getChangeImgUrl();
                }
            }
        });*/
        //上传新的排查图片
        danger_checkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempImageViem=danger_checkImage;
                isFirst=true;
                showPopueWindow();
                //checkImagePath=photoPath;
            }
        });
        //上传整改图片
        danger_changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempImageViem=danger_changeImage;
                isFirst=false;
                showPopueWindow();
            }
        });

    }
    //从后台获取Image
    public void getCheckImgUrl(){
        Log.e("getCheckImgUrl:","getCheckImgUrl");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.43.200:7001/file/download";
                String path = hazardClearRecords.getCheckImg();
                try {
                    imageView=danger_checkImage;
                    getCheckImg(url,path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void getCheckImg(String url,String path)throws IOException {
        Log.e("path:", path);
        //OkHttpClient client=new OkHttpClient();
        RequestBody fromBody= new FormBody.Builder()
                .addEncoded("url",path)
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(fromBody)
                .build();
        Response response=client.newCall(request).execute();
        Message message = handler.obtainMessage();
        if(response.isSuccessful()){
            byte[] bytes = response.body().bytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    danger_checkImage.setImageBitmap(bitmap);
                }
            });
        }else {
            handler.sendEmptyMessage(IS_FAIL);
        }
    }
    public void getChangeImgUrl(){
        Log.e("getChangeImgUrl:","getChangeImgUrl");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.43.200:7001/file/download";
                String path = hazardClearRecords.getChangeImg();
                try {
                    imageView=danger_changeImage;
                    getChangeImg(url,path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void getChangeImg(String url,String path) throws IOException{
        Log.e("path:", path);
        //OkHttpClient client=new OkHttpClient();
        RequestBody fromBody= new FormBody.Builder()
                .addEncoded("url",path)
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(fromBody)
                .build();
        Response response=client.newCall(request).execute();
        Message message = handler.obtainMessage();
        if(response.isSuccessful()){
            byte[] bytes = response.body().bytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    danger_changeImage.setImageBitmap(bitmap);
                }
            });
        }else {
            handler.sendEmptyMessage(IS_FAIL);
        }
    }
    //将所有数据放到实体对象中
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initInfo(){
        hazardClearRecords.setId(Long.parseLong(danger_id.getText().toString()));
        hazardClearRecords.setHazardName(danger_hazardName.getText().toString());
        hazardClearRecords.setEnterpriseName(danger_enterpriseName.getText().toString());
        hazardClearRecords.setCheckerName(danger_CheckerName.getText().toString());
        //hazardClearRecords.setCheckImg(checkImagePath);
        hazardClearRecords.setHazardType(danger_hazardType.getText().toString());
        hazardClearRecords.setInspectionBasis(danger_inspectionBasis.getText().toString());
        hazardClearRecords.setChangeReception(danger_checkReception.getText().toString());
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date=time.format(new Date());
        try {
            hazardClearRecords.setCheckTime(time.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //hazardClearRecords.setStatus(Boolean.parseBoolean(danger_status.getText().toString()));
        /*hazardClearRecords.setStatus("完成");
        hazardClearRecords.setCheckStatus("审核结束");*/
        hazardClearRecords.setHazardLevel(danger_level.getText().toString());
        hazardClearRecords.setChangerName(danger_changerName.getText().toString());
        //hazardClearRecords.setChangeImg(changeImagePath);
        hazardClearRecords.setChangeReception(danger_changeReception.getText().toString());
        SimpleDateFormat time1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date1=time.format(new Date());
        try {
            hazardClearRecords.setChangeTime(time.parse(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    //从相册选择或直接拍照得到图片路径
    private void showPopueWindow(){
        View popView = View.inflate(this,R.layout.popupwindow_camera_need,null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        // popupWindow.setAnimationStyle(R.style.anim_popup_dir);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
                    ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_CHOOSE_PHOTO);
                } else {
                    //已授权，获取照片
                    choosePhoto();
                }

            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                popupWindow.dismiss();

            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);

    }
    /**
     权限申请结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_TAKE_PHOTO:   //拍照权限申请返回
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                break;
            case RC_CHOOSE_PHOTO:   //相册选择照片权限申请返回
                choosePhoto();
                break;
        }
    }
    //public static final int RC_CHOOSE_PHOTO = 2;

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }
    private Uri imageUri;
    //private String filePath;
    private String photoPath;
    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile = new File(fileDir, "photo.jpeg");
        photoPath = photoFile.getAbsolutePath();
        imageUri = FileProvider7.getUriForFile(this, photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                Uri uri = data.getData();
                photoPath = FileUtil.getFilePathByUri(this, uri);

                if (!TextUtils.isEmpty(photoPath)) {
                    RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                    //将照片显示在 ivImage上
                    Glide.with(this).load(photoPath).apply(requestOptions1).into(tempImageViem);
                }
                Log.e("photoPath", "onActivityResult: "+photoPath);
                if(isFirst){
                    firstFile=new File(photoPath);
                }else{
                    secondFile=new File(photoPath);
                }
                break;
            case RC_TAKE_PHOTO:
                RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                //将图片显示在ivImage上
                Glide.with(this).load(photoPath).apply(requestOptions).into(tempImageViem);
                if(isFirst){
                    firstFile=new File(photoPath);
                }else{
                    secondFile=new File(photoPath);
                }
                break;
        }
    }
    //save触发事件上传到后台
    public void updateNew(String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(firstFile!=null&& (hazardClearRecords.getCheckImg()==null || hazardClearRecords.getChangeImg().length()<=0)){
                        getImagePath(firstFile);
                        while (!isReturn){
                        }
                        hazardClearRecords.setCheckImg(result);
                        isReturn = false;
                    }
                    if(secondFile!=null&&(hazardClearRecords.getChangeImg()==null || hazardClearRecords.getChangeImg().length()<=0)){
                        getImagePath(secondFile);
                        while (!isReturn){
                        }
                        hazardClearRecords.setChangeImg(result);
                        isReturn = false;
                    }
                    update(url,new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create().toJson(hazardClearRecords));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public void update(String url,String json)throws IOException{
        //OkHttpClient client=new OkHttpClient();
        RequestBody body = RequestBody.create(JSON,json);
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response=client.newCall(request).execute();
        if(response.isSuccessful()){
            updateResult=response.body().string();
        }else {
            updateResult=null;
        }

    }
    //delete触发事件连接后台删除
    public void  deleteRecord(HazardClearRecords hazardClearRecords){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/hazardclearancerecords/delete";
                try {
                    delete(url,new Gson().toJson(hazardClearRecords));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void delete(String url,String json)throws IOException{
        //OkHttpClient client=new OkHttpClient();
        RequestBody formBody=new FormBody.Builder()
                .add("id",json)
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response=client.newCall(request).execute();
        if(response.isSuccessful()){
            deleteResult=response.body().string();
        }else {
            deleteResult=null;
        }
    }
    public void getImagePath(File file){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/file/upload";
                try {
                    upload(url,file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void upload(String url,File file)throws IOException {
        Log.e("file", new Gson().toJson(file));
        if(file != null){
            //OkHttpClient client=new OkHttpClient();
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            String fileName = file.getName();
            RequestBody body=RequestBody.create(MediaType.parse("multipart/form-data"),file);

            requestBody.addFormDataPart("file", fileName, body);
            Request request=new Request.Builder()
                    .url(url)
                    .post(requestBody.build())
                    .build();
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                String p = response.body().string();
                result = p.substring(23,p.length()-12);
                //Log.e("打印照片路径",hazardClearRecords.getChangeImg());
            }else {
                //hazardClearRecords.setCheckImg(null);
                result=null;
            }
            isReturn = true;
        }
    }

}
