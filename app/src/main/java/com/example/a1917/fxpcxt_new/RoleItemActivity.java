package com.example.a1917.fxpcxt_new;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.entity.Function;
import com.example.a1917.fxpcxt_new.entity.Role;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RoleItemActivity extends AppCompatActivity {
    private EditText role_id,role_name,role_status;
    private CheckBox role_function1,role_function2,role_function3,role_function4,role_function5;
    private Button btn_save,btn_delete;
    private List<CheckBox> checkBoxes=new ArrayList<>();
    static String updateResult=null,deleteResult=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_item);
        //从fragment中获取传入的值
        Intent intent= getIntent();
        if(intent != null){
            Role role = (Role) intent.getSerializableExtra("role");
            //初始化EditText，并赋值
            initLayout();
            //从后台获取checkBox的数据
            initCheckBox();
            //将role中的值放到页面中
            setValues(role);
        }
        //触发事件
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取页面中的数据
                Role tempRole=new Role();
                tempRole=initRole();
                updateRole(tempRole);
                if(updateResult!=null){
                    Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Role tempRole=new Role();
                tempRole=initRole();
                deleteRole(tempRole);
                if(deleteResult!=null){
                    Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });


    }
    //将页面中的数据放到tempRole中，再上传后台
    public Role initRole(){
        Role role=new Role();
        role.setId(Long.parseLong(role_id.getText().toString()));
        role.setName(role_name.getText().toString());
        role.setStatus(Boolean.parseBoolean(role_status.getText().toString()));
        List<Function> tempList=new ArrayList<>();
        Function function=new Function();
        for(int i=0;i<checkBoxes.size();i++){
            if(checkBoxes.get(i).isChecked()){
                function.setId((long)(i+1));
                function.setName(checkBoxes.get(i).getText().toString());
                tempList.add(function);
            }
        }
        role.setFunctions(tempList);
        return role;
    }
    public  void updateRole(Role role){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/role/update";
                try {
                    String result=update(url,new Gson().toJson(role));
                    updateResult=result;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
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
        Response response=client.newCall(request).execute();
        if(response.isSuccessful()){
            String result=response.body().toString();
            return result;
        }else {
            return null;
        }

    }
    //删除角色
    public  void  deleteRole(Role role){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/role/delete";
                try {
                    deleteResult=delete(url,role);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String delete(String url,Role role) throws IOException{
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(180,TimeUnit.SECONDS)
                .readTimeout(180,TimeUnit.SECONDS)
                .build();
        RequestBody formBody=new FormBody.Builder()
                .add("id",role.getId().toString())
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response=client.newCall(request).execute();
        if(response.isSuccessful()){
            String result=response.body().toString();
            return result;
        }else {

            return  null;
        }
    }
    //将role中的值放到页面中
    public void setValues(Role role){
        role_id.setText(role.getId()+"");
        role_name.setText(role.getName());
        role_status.setText(role.getStatus()+"");
        if(role.getFunctions().size()!=0){
            for(int i=0;i<role.getFunctions().size();i++){
                int k=new Long(role.getFunctions().get(i).getId()).intValue();
                checkBoxes.get(k).setChecked(true);
            }
        }
    }
    //后台获取checkBox的值
    List<Function> list=null;
    public void initCheckBox(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/function/selectAll";
                try {
                    list=selectAll(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (list == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //将取出的方法放到复选框中
        for(int i=0;i<checkBoxes.size();i++){
            checkBoxes.get(i).setText(list.get(i).getName());
        }
    }

    public  List<Function> selectAll(String url) throws IOException {
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(180,TimeUnit.SECONDS)
                .readTimeout(180,TimeUnit.SECONDS)
                .build();
        Request request=new Request.Builder()
                .url(url)
                .build();
        Response response=client.newCall(request).execute();

        String p = response.body().string();
        String body = p.substring(23,p.length()-12);
        Log.e("Role---------------",body);
        list=new Gson().fromJson(body,new TypeToken<List<Function>>(){}.getType());
        Log.e("11listlistlistlsit",list.get(0).getName());
        Log.e("22listlistlistlsit",list.get(0).getId().toString());
        return list;
    }
    public void initLayout(){
        role_id=findViewById(R.id.role_id);
        role_name=findViewById(R.id.role_name);
        role_status=findViewById(R.id.role_status);
        btn_save=findViewById(R.id.role_save);
        btn_delete=findViewById(R.id.role_delete);
        role_function1=findViewById(R.id.role_function1);
        role_function2=findViewById(R.id.role_function2);
        role_function3=findViewById(R.id.role_function3);
        role_function4=findViewById(R.id.role_function4);
        role_function5=findViewById(R.id.role_function5);
        checkBoxes.add(role_function1);
        checkBoxes.add(role_function2);
        checkBoxes.add(role_function3);
        checkBoxes.add(role_function4);
        checkBoxes.add(role_function5);
    }
}
