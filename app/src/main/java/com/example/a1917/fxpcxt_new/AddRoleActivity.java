package com.example.a1917.fxpcxt_new;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.entity.Function;
import com.example.a1917.fxpcxt_new.entity.Role;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.citypickerview.widget.CityPickerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddRoleActivity extends AppCompatActivity {
    private EditText add_role_name,add_role_status;
    private CheckBox add_role_function1,add_role_function2,add_role_function3,add_role_function4,add_role_function5;
    private Button btn_role_save;
    private TextView add_role_data;
    private List<CheckBox> checkBoxList = new ArrayList<>();
    Role role=new Role();
    Function function=new Function();
    List<Function> functions=null;
    static String result=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_role);
        initLayout();
        initCheckBox();
        //保存触发事件
        btn_role_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取页面中的数据
                role.setName(add_role_name.getText().toString());
                role.setStatus(Boolean.parseBoolean(add_role_status.getText().toString()));
                for(int i=0;i<=checkBoxList.size();i++)
                {
                    if(checkBoxList.get(i).isChecked()){
                        //将复选框中的值放入role中
                        function.setId((long)(i+1));
                        function.setName(checkBoxList.get(i).getText().toString());
                        list.add(function);

                    }
                }
                add_role_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectAddress();
                        add_role_data.setText(address);
                        role.setDataOperate(address);
                    }
                });
                //将list放在role中
                role.setFunctions(list);
                //上传数据到后台
                saveRole(role);
                if(result != null){
                    Toast.makeText(getApplicationContext(), "成功增加角色", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
    public void saveRole(Role role){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/role/save";
                try {
                    result=save(url,new Gson().toJson(role));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public String save(String url,String json) throws IOException{
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
            String result=response.body().string();
            return result;
        }else {
            return null;
        }

    }
    public void initLayout(){
        add_role_name=findViewById(R.id.add_role_name);
        add_role_status=findViewById(R.id.add_role_name);
        add_role_function1=findViewById(R.id.add_role_function1);
        add_role_function2=findViewById(R.id.add_role_function2);
        add_role_function3=findViewById(R.id.add_role_function3);
        add_role_function4=findViewById(R.id.add_role_function4);
        add_role_function5=findViewById(R.id.add_role_function5);
        btn_role_save=findViewById(R.id.add_role_save);
        checkBoxList.add(add_role_function1);
        checkBoxList.add(add_role_function2);
        checkBoxList.add(add_role_function3);
        checkBoxList.add(add_role_function4);
        checkBoxList.add(add_role_function5);
        add_role_data=findViewById(R.id.add_role_data);

    }
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
        while(list == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //将取出的方法放到复选框中
        add_role_function1.setText(list.get(0).getName());
        add_role_function2.setText(list.get(1).getName());
        add_role_function3.setText(list.get(2).getName());
        add_role_function4.setText(list.get(3).getName());
        add_role_function5.setText(list.get(4).getName());
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
        list=new Gson().fromJson(body,new TypeToken<List<Function>>(){}.getType());
        return list;
    }
    static String address;
    public void selectAddress(){
        CityPickerView cityPickerView = new CityPickerView(AddRoleActivity.this);
        cityPickerView.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                //Toast.makeText(MainActivity.this,province+"-"+city+"-"+district,Toast.LENGTH_LONG).show();
                address=province+city+district;
            }
        });
        cityPickerView.show();

        //return address;
    }
}
