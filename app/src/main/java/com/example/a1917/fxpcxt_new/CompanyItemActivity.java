package com.example.a1917.fxpcxt_new;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.entity.Enterprise;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CompanyItemActivity extends AppCompatActivity {
    private TextView company_id,company_name,company_address,company_fundTime,company_industryName,company_ower,company_owerPost,company_owerPhone,company_owerOffice;
    private EditText company_linkman,company_phone,company_workers;
    private Button btn_company_save,btn_company_delete;
    Enterprise enterprise=new Enterprise();
    String saveResult,deleteResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_item);
        //接收从CompanyFragment传来的数据
        Intent intent=getIntent();
        if(intent != null){
            enterprise=(Enterprise) intent.getSerializableExtra("enterprise");
            //初始化页面中的控件
            initLayout();
            //把接收的数据展示出来
            showData();

        }
        //save的触发事件
        btn_company_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获得控件的值，放入实体类中
                Enterprise newEnterprise=renovateInfo();
                //将带有控件信息的实体类传入后台
                saveNewEnterprise(newEnterprise);
                if(saveResult != null){
                    Toast.makeText(CompanyItemActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        //delete的触发事件
        btn_company_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取enterprise的id
                Long id=enterprise.getId();
                //根据获得的id去进行删除操作
                deleteEnterprise(id);
                if(deleteResult != null){
                    Toast.makeText(CompanyItemActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    public  void initLayout(){
        company_id=findViewById(R.id.company_id);
        company_name=findViewById(R.id.company_name);
        company_address=findViewById(R.id.company_address);
        company_fundTime=findViewById(R.id.company_fundTime);
        company_industryName=findViewById(R.id.company_industryName);
        company_ower=findViewById(R.id.company_ower);
        company_owerPost=findViewById(R.id.company_owerPost);
        company_owerPhone=findViewById(R.id.company_owerPhone);
        company_owerOffice=findViewById(R.id.company_owerOffice);

        company_linkman=findViewById(R.id.company_linkman);
        company_phone=findViewById(R.id.company_phone);
        company_workers=findViewById(R.id.company_workers);
        btn_company_save=findViewById(R.id.company_save);
        btn_company_delete=findViewById(R.id.company_delete);
    }
    public void showData(){
        company_id.setText(enterprise.getId().toString());
        company_name.setText(enterprise.getName());
        company_address.setText(enterprise.getAddress());
        company_fundTime.setText(enterprise.getFundTime());
        company_industryName.setText(enterprise.getIndustryName());
        company_ower.setText(enterprise.getOwerName());
        company_owerPost.setText(enterprise.getOwerPost());
        company_owerPhone.setText(enterprise.getOwerPhone());
        company_owerOffice.setText(enterprise.getOwerOffice());

        company_linkman.setText(enterprise.getLinkman());
        company_phone.setText(enterprise.getPhone());
        company_workers.setText(enterprise.getWorkers()+"");
    }
   //刷新控件的值，放入实体类中
    public Enterprise renovateInfo(){
        Enterprise newEnterprise=new Enterprise();
        newEnterprise.setId(Long.parseLong(company_id.getText().toString()));
        newEnterprise.setName(company_name.getText().toString());
        newEnterprise.setAddress(company_address.getText().toString());
        newEnterprise.setFundTime(company_fundTime.getText().toString());
        newEnterprise.setIndustryName(company_industryName.getText().toString());
        newEnterprise.setOwerName(company_ower.getText().toString());
        newEnterprise.setOwerPost(company_owerPost.getText().toString());
        newEnterprise.setOwerPhone(company_owerPhone.getText().toString());
        newEnterprise.setOwerOffice(company_owerOffice.getText().toString());

        newEnterprise.setLinkman(company_linkman.getText().toString());
        Log.e("linkman",newEnterprise.getLinkman());
        newEnterprise.setPhone(company_phone.getText().toString());
        newEnterprise.setWorkers(Integer.parseInt(company_workers.getText().toString()));
        return newEnterprise;
    }
    //save连接后台
    public void saveNewEnterprise(Enterprise newEnterprise){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/enterprise/update";
                try {
                    save(url,new Gson().toJson(newEnterprise));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public void save(String url,String json)throws IOException {
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
            saveResult=response.body().string();
        }else{
            saveResult=null;
        }
    }
    //delete连接后台
    public void deleteEnterprise(Long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/enterprise/delete";
                try {
                    delete(url,id.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void delete(String url,String json)throws IOException{
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(180,TimeUnit.SECONDS)
                .readTimeout(180,TimeUnit.SECONDS)
                .build();
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
}
