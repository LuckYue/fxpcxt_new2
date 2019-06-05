package com.example.a1917.fxpcxt_new;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.a1917.fxpcxt_new.entity.HazardClearRecords;
import com.example.a1917.fxpcxt_new.util.FileUtil;
import com.google.gson.Gson;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddDangerActivity extends AppCompatActivity {
    private EditText add_danger_hazardName,add_danger_enterpriseNmae,add_danger_checkerName,add_danger_checkReception,add_danger_inspectionBasis;
    private ImageView add_danger_checkImage;
    private Spinner add_danger_Spinner;
    private Button add_danger_save;//add_uploadCheckImage;
    HazardClearRecords hazardClearRecords=new HazardClearRecords();
    //private final static int RESULT_LOAD_IMAGE=1;
    //private final static int RESULT_CAMERA_IMAGE=1;
    public final static int RC_TAKE_PHOTO=1;
    public final static int RC_CHOOSE_PHOTO=2;
    public static String result=null;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(180,TimeUnit.SECONDS)
            .readTimeout(180,TimeUnit.SECONDS)
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_danger);
        initView();
        List<String> typeSpinnerText=new ArrayList<String>();
        typeSpinnerText.add("职业健康");
        typeSpinnerText.add("安全管理");
        typeSpinnerText.add("特种设备");
        typeSpinnerText.add("电气安全");
        typeSpinnerText.add("消防");
        ArrayAdapter<String> typeText=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typeSpinnerText);
        //获取后台数据进行展示,
        typeText.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_danger_Spinner.setAdapter(typeText);
        //将得到的图片上传
       /* add_uploadCheckImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCheckImage();
            }
        });*/
        add_danger_save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                uploadCheckImage();
                initInfo();
                saveHazardClearRecords();
                if(result != null){
                    Toast.makeText(getApplicationContext(), "成功增加排查记录", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        add_danger_checkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click","触发事件");
                showPopueWindow();
            }
        });

    }
    public  void saveHazardClearRecords(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/hazardclearancerecords/save";
                try {
                    save(url,new Gson().toJson(hazardClearRecords));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public void save(String url,String json) throws IOException {
        //OkHttpClient client=new OkHttpClient();
        RequestBody body=RequestBody.create(JSON,json);
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response=client.newCall(request).execute();
        if(response.isSuccessful()){
            result=response.body().string();
        }else {
            result=null;
        }
    }
    public  void initView(){
        add_danger_hazardName=findViewById(R.id.add_danger_hazardName);
        add_danger_enterpriseNmae=findViewById(R.id.add_danger_enterpriseNmae);
        add_danger_checkerName=findViewById(R.id.add_danger_checkerName);
        add_danger_checkImage=findViewById(R.id.add_danger_checkImage);
        add_danger_checkReception=findViewById(R.id.add_danger_checkReception);
        add_danger_inspectionBasis=findViewById(R.id.add_danger_inspectionBasis);
        add_danger_Spinner=findViewById(R.id.add_danger_Spinner);
        //add_danger_checkTime=findViewById(R.id.add_danger_checkTime);
        add_danger_save=findViewById(R.id.add_danger_save);
        //add_uploadCheckImage=findViewById(R.id.add_uploadCheckImage);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initInfo(){
        hazardClearRecords.setHazardName(add_danger_hazardName.getText().toString());
        hazardClearRecords.setEnterpriseName(add_danger_enterpriseNmae.getText().toString());
        hazardClearRecords.setCheckerName(add_danger_checkerName.getText().toString());
        //选择本地相册还是拍照
        hazardClearRecords.setCheckImg(photoPath);
        hazardClearRecords.setChangeReception(add_danger_checkReception.getText().toString());
        SimpleDateFormat time=new SimpleDateFormat("yyyy--mm--dd HH:mm:ss");
        String date=time.format(System.currentTimeMillis());
        try {
            hazardClearRecords.setCheckTime(time.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
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

               /* Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                popupWindow.dismiss();*/

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
 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                Uri uri = data.getData();
                String filePath = FileUtil.getFilePathByUri(this, uri);

                if (!TextUtils.isEmpty(filePath)) {
                    RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                    //将照片显示在 ivImage上
                    Glide.with(this).load(filePath).apply(requestOptions1).into(add_danger_checkImage);
                }
                break;
        }
    }*/
    //private String mTempPhotoPath;
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
    static File file;
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
                    Glide.with(this).load(photoPath).apply(requestOptions1).into(add_danger_checkImage);
                }
                file=new File(photoPath);
                break;
            case RC_TAKE_PHOTO:
                RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                //将图片显示在ivImage上
                Glide.with(this).load(photoPath).apply(requestOptions).into(add_danger_checkImage);
                file=new File(photoPath);
                break;
        }
    }
    public void uploadCheckImage(){
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
        Log.e("file",new Gson().toJson(file));
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
                Log.e("打印照片路径",response.body().string());
                String p = response.body().string();
                hazardClearRecords.setCheckImg(p.substring(23,p.length()-12));
            }else {
                Log.e("上传图片失败",response.message());
                hazardClearRecords.setCheckImg(null);
            }
        }
    }
}
