package com.example.a1917.fxpcxt_new;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.entity.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserItemActivity extends AppCompatActivity {
    private EditText id,account,password,name,unitName,status,phone,orgnazation;
    private Button btn_save,btn_delete;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_item);
        Intent intent= getIntent();
        if(intent!=null){
            User user = (User) intent.getSerializableExtra("user");
            id=findViewById(R.id.id);
            id.setText(user.getId() + "");
            account=findViewById(R.id.account);
            account.setText(user.getAccount().toString());
            password=findViewById(R.id.password);
            password.setText(user.getPassword());
            name=findViewById(R.id.name);
            name.setText(user.getName());
            unitName=findViewById(R.id.unitName);
            unitName.setText(user.getUnitName());
            status=findViewById(R.id.status);
            Log.e("-=-=-=--=-=",user.getStatus().toString());
            status.setText(user.getStatus().toString()+"");
            phone=findViewById(R.id.phone);
            phone.setText(user.getPhone());
            orgnazation=findViewById(R.id.orgnazation);
            Log.e("-=-=-=--=-=",user.getOrgId().toString());
            orgnazation.setText(user.getOrgId().toString()+"");
        }
        btn_save=findViewById(R.id.save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user=initUser();
             //连接后台进行修改操作
                updateUser(user);
                if(condition==true){
                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        btn_delete=findViewById(R.id.delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user=initUser();
                //连接后台进行删除操作
                deleteUser(user);
                if(condition==true){
                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }
    static Boolean condition=false;
    public  void updateUser(User user){
        String url="http://192.168.43.200:7001/userInfo/update";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String result=update(url,new Gson().toJson(user));
                    //判断result，给出fanying
                    if(result!=null){
                        condition=true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
    public  static final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
    public String update(String url,String json)throws IOException{

        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(180,TimeUnit.SECONDS)
                .readTimeout(180,TimeUnit.SECONDS)
                .build();
        RequestBody body=RequestBody.create(JSON,json);
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response= null;
        response = client.newCall(request).execute();
        if(response.isSuccessful()){
            String result=response.body().string();
            return result;
        }else {
            return null;
        }
    }
    public void deleteUser(User user){
        String url="http://192.168.43.200:7001/userInfo/delete";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String result=delete(url,user);
                    //判断返回的result
                    if(result!=null){
                        condition=true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public  String delete(String url,User user) throws IOException{
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(180,TimeUnit.SECONDS)
                .readTimeout(180,TimeUnit.SECONDS)
                .build();
        RequestBody formBody=new FormBody.Builder()
                .add("id",user.getId().toString())
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response=client.newCall(request).execute();
        if(response.isSuccessful()){
            String result=response.body().string();
            return result;
        }else {
            return null;
        }

    }
    public User initUser(){
        User user=new User();
        user.setId(Long.parseLong(id.getText().toString()));
        user.setAccount(account.getText().toString());
        user.setPassword(password.getText().toString());
        user.setName(name.getText().toString());
        user.setUnitName(unitName.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setStatus(Boolean.parseBoolean(status.getText().toString()));
        user.setOrgName(orgnazation.getText().toString());
        Log.e("up-------------",user.getAccount());
        return user;
    }
}
