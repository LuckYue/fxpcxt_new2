package com.example.a1917.fxpcxt_new;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.entity.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddUserActivity extends AppCompatActivity {
    private EditText mAccount, mPassword, mName, mUnitName, mStatus, mPhone, mOrgnazation;
    private Button btn_save;
    User user = new User();
    static String result=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        initLayout();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将user提交到后台
                String url = "http://192.168.43.200:7001/userInfo/save";
                saveUser(url, new Gson().toJson(user));
                if(result !=null){
                    Toast.makeText(getApplicationContext(), "成功增加用户", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    public void saveUser(String url, String json) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result = save(url, json);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    public String save(String url, String json) throws IOException {
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(180,TimeUnit.SECONDS)
                .readTimeout(180,TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            String result1 = response.body().string();
            return result1;
        }else{
            return null;
        }

    }

    public void initLayout() {
        mAccount = findViewById(R.id.add_account);
        mPassword = findViewById(R.id.add_password);
        mName = findViewById(R.id.add_name);
        mUnitName = findViewById(R.id.add_unitName);
        mStatus = findViewById(R.id.add_status);
        mPhone = findViewById(R.id.add_phone);
        mOrgnazation = findViewById(R.id.add_orgnazation);
        btn_save = findViewById(R.id.add_save);

        String account = mAccount.getText().toString();
        String password = mPassword.getText().toString();
        String name = mName.getText().toString();
        String unitName = mUnitName.getText().toString();
        String status = mStatus.getText().toString();
        String phone = mPhone.getText().toString();
        String orgnazation = mOrgnazation.getText().toString();

        user.setAccount(account);
        user.setPassword(password);
        user.setName(name);
        user.setUnitName(unitName);
        user.setStatus(Boolean.parseBoolean(status));
        user.setPhone(phone);
        user.setOrgName(orgnazation);
    }

}
