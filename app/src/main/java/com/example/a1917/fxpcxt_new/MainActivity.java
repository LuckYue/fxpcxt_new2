package com.example.a1917.fxpcxt_new;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.a1917.fxpcxt_new.entity.User;
import com.google.gson.Gson;

import java.io.IOException;
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
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(180,TimeUnit.SECONDS)
            .readTimeout(180,TimeUnit.SECONDS)
            .build();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Boolean result = (Boolean) msg.obj;
                if (result != null && result) {
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, UserMenuActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/userInfo/login";
                try {
                    Log.e("-----------",url + new Gson().toJson(user));
                    final String result=judgeLogin(url,new Gson().toJson(user));
                    //mHandler.obtainMessage(1,result).sendToTarget();
                    if (result != null ) {
                        //Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, UserMenuActivity.class);
                        startActivity(intent);
                    } else {
                        //Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public  static final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
    public String judgeLogin(String url,String json)throws IOException{

        RequestBody body=RequestBody.create(JSON,json);
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();

        if(response.isSuccessful()){
            Log.e("aaaaaaaaaaa",response.body().string());
            return "0";
        }
        //String result=response.body().string();
        //Log.e("aaaaaaaaaaa",result);
        else {
            Log.e("bbbbbbbbb",response.body().string());
            return null;
        }

    }
}


