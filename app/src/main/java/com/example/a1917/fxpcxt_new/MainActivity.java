package com.example.a1917.fxpcxt_new;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.R;
import com.example.a1917.fxpcxt_new.common.CommonResponse;
import com.example.a1917.fxpcxt_new.common.LoginContext;
import com.example.a1917.fxpcxt_new.entity.Enterprise;
import com.example.a1917.fxpcxt_new.entity.User;
import com.example.a1917.fxpcxt_new.route.Routs;
import com.example.a1917.fxpcxt_new.util.CallBackUtil;
import com.example.a1917.fxpcxt_new.util.GsonUtil;
import com.example.a1917.fxpcxt_new.util.OkhttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText etUserName, etPassword;
    private TextView faceLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUserName = findViewById(R.id.userName);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        faceLogin=findViewById(R.id.faceLogin);
        initListener();
    }

    private void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                //判断用户名是否为空
                if (TextUtils.isEmpty(userName)) {//用户名为空
                    etUserName.setError("用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {//密码为空
                    etPassword.setError("密码不能为空");
                    return;
                }
                login(userName, password);
            }
        });
        faceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,FaceJudgeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(final String userName, final String password) {
        User user=new User();
        user.setAccount(userName);
        user.setPassword(password);
        //调用okhttputil中方法
        OkhttpUtil.okHttpPostJson(Routs.LOGIN_URL, GsonUtil.GsonToString(user), new CallBackUtil<CommonResponse<LoginContext>>() {
            @Override
            public CommonResponse<LoginContext> onParseResponse(Call call, Response response) {
                String p = null;
                try {
                    p = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CommonResponse<LoginContext> commonResponse = GsonUtil.getGson().fromJson(p,new TypeToken<CommonResponse<LoginContext>>(){}.getType());
                return commonResponse;
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(CommonResponse<LoginContext> response) {
                if(response.isSuccess()){
                    LoginContext loginContext = response.getData();
                    SharedPreferences sharedPreferences = getSharedPreferences("Token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("Token", loginContext.getToken());
                    editor.commit();//提交修改
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, UserMenuActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


