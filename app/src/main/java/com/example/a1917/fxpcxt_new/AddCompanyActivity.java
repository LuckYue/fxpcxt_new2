package com.example.a1917.fxpcxt_new;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.entity.Enterprise;
import com.google.gson.Gson;
import com.lljjcoder.citypickerview.widget.CityPickerView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddCompanyActivity extends AppCompatActivity {
    private EditText add_company_name,add_company_linkman,add_company_phone,add_company_workers,
            add_company_industryName,add_company_ower,add_company_owerPost,add_company_owerPhone,add_company_owerOffice;
    private TextView add_company_address,add_company_fundTime;
    private Button btn_add_company_save;
    Enterprise enterprise=new Enterprise();
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);
        //初始化控件
        initLayout();
        //地址的出发事件
        add_company_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //地址选择器
                selectAddress();
                add_company_address.setText(address);
            }
        });
        add_company_fundTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                showDatePickerDialog(AddCompanyActivity.this,2,add_company_fundTime,calendar);
            }
        });
        btn_add_company_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获得控件中的值，放入实体类中
                getValues();
                //连接后台，传送数据
                saveEnterprise();
                //根据后台返回的结果弹出信息
                if(result!=null){
                    Toast.makeText(AddCompanyActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
    public void initLayout(){
        add_company_name=findViewById(R.id.add_company_name);
        add_company_linkman=findViewById(R.id.add_company_linkman);
        add_company_phone=findViewById(R.id.add_company_phone);
        add_company_address=findViewById(R.id.add_company_address);
        add_company_fundTime=findViewById(R.id.add_company_fundTime);
        add_company_workers=findViewById(R.id.add_company_workers);
        add_company_industryName=findViewById(R.id.add_company_industryName);
        add_company_ower=findViewById(R.id.add_company_ower);
        add_company_owerPost=findViewById(R.id.add_company_owerPost);
        add_company_owerPhone=findViewById(R.id.add_company_owerPhone);
        add_company_owerOffice=findViewById(R.id.add_company_owerOffice);
        btn_add_company_save=findViewById(R.id.add_company_save);
    }
    public void getValues(){
        enterprise.setName(add_company_name.getText().toString());
        enterprise.setLinkman(add_company_linkman.getText().toString());
        enterprise.setPhone(add_company_phone.getText().toString());
        enterprise.setAddress(add_company_address.getText().toString());
        enterprise.setFundTime(add_company_fundTime.getText().toString());
        enterprise.setWorkers(Integer.parseInt(add_company_workers.getText().toString()));
        enterprise.setIndustryName(add_company_industryName.getText().toString());
        enterprise.setOwerName(add_company_ower.getText().toString());
        enterprise.setOwerPost(add_company_owerPost.getText().toString());
        enterprise.setOwerPhone(add_company_owerPhone.getText().toString());
        enterprise.setOwerOffice(add_company_owerOffice.getText().toString());
    }
    public void saveEnterprise(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/enterprise/save";
                try {
                    Log.e("打印enterprise的名字，联系人",enterprise.getName()+enterprise.getLinkman());
                    save(url,new Gson().toJson(enterprise));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public void save(String url,String json)throws IOException {
        Log.e("json",json);
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
            result=response.body().string();
        }else {
            result=null;
        }
    }
    static String address;
    public void selectAddress(){
        CityPickerView cityPickerView = new CityPickerView(AddCompanyActivity.this);
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity , themeResId,new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                tv.setText(year + "年" + (monthOfYear+1)+ "月" + dayOfMonth + "日");

            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}
