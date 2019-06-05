package com.example.a1917.fxpcxt_new;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.common.CommonResponse;
import com.example.a1917.fxpcxt_new.route.Routs;
import com.example.a1917.fxpcxt_new.util.CallBackUtil;
import com.example.a1917.fxpcxt_new.util.GsonUtil;
import com.example.a1917.fxpcxt_new.util.OkhttpUtil;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.data.ExFilePickerResult;

public class UploadFileActivity extends AppCompatActivity {
    private AppCompatActivity mActivity;
    private final int EX_FILE_PICKER_RESULT = 0xfa01;
    private String startDirectory = null;// 记忆上一次访问的文件目录路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_upload_file);
        findViewById(R.id.choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExFilePicker exFilePicker = new ExFilePicker();
                exFilePicker.setCanChooseOnlyOneItem(true);// 单选
                exFilePicker.setQuitButtonEnabled(true);

                if (TextUtils.isEmpty(startDirectory)) {
                    exFilePicker.setStartDirectory(Environment.getExternalStorageDirectory().getPath());
                } else {
                    exFilePicker.setStartDirectory(startDirectory);
                }

                exFilePicker.setChoiceType(ExFilePicker.ChoiceType.FILES);
                exFilePicker.start(mActivity, EX_FILE_PICKER_RESULT);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EX_FILE_PICKER_RESULT) {
            ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
            if (result != null && result.getCount() > 0) {
                String path = result.getPath();
                List<String> names = result.getNames();
                List<File> files = null;
                //File file = new File(path,names.get(0));

                for (int i = 0; i < names.size(); i++) {
                    File file = new File(path, names.get(i));
                    files.add(file);
                    try {
                        Uri uri = Uri.fromFile(file); //这里获取了真实可用的文件资源
                        Toast.makeText(mActivity, "选择文件:" + uri.getPath(), Toast.LENGTH_SHORT)
                                .show();

                        startDirectory = path;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("是否上传文件");
                // 确定按钮的点击事件

                builder.setPositiveButton("是",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,int whichButton) {
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("type","IndustryAndHazardType");
                        for(int i=0;i<names.size();i++){
                            //上传文件
                            OkhttpUtil.okHttpUploadFile(Routs.UPLOAD_FILES, files.get(i), "file", "",hashMap, new CallBackUtil<CommonResponse>() {
                                @Override
                                public CommonResponse onParseResponse(Call call, Response response) {
                                    String p = null;
                                    try {
                                        p = response.body().string();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    CommonResponse commonResponse = GsonUtil.getGson().fromJson(p,new TypeToken<CommonResponse>(){}.getType());
                                    return commonResponse;
                                }

                                @Override
                                public void onFailure(Call call, Exception e) {
                                    Toast.makeText(mActivity, "网络异常", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(CommonResponse response) {
                                    if(!response.isSuccess()){
                                        Toast.makeText(mActivity, response.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        Toast.makeText(mActivity, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();

                    }

                });

                // 取消按钮的点击事件

                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }

                });
                builder.create().show();

            }
        }
    }

}
