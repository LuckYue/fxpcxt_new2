package com.example.a1917.fxpcxt_new.fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.example.a1917.fxpcxt_new.R;
import com.example.a1917.fxpcxt_new.entity.Enterprise;
import com.example.a1917.fxpcxt_new.entity.Role;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyFragment extends Fragment {

    private Spinner province_spinner,city_spinner,county_spinner;

    private List<String> provinceList=new ArrayList<>();
    private List<String> cityList=new ArrayList<>();
    private List<String> countyList=new ArrayList<>();

    public CompanyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_company,container,false);
        province_spinner=view.findViewById(R.id.province_spinner);
        city_spinner=view.findViewById(R.id.city_spinner);
        county_spinner=view.findViewById(R.id.county_spinner);

        Resources resources=getResources();
        //String [] province=resources.getStringArray(R.array.);
        //a创建省市县三级联动

        //得到县名
        String countyName=null;
        //根据县名链接后台显示企业信息
        showEnterpriseInfo(countyName);
        return view;
    }
    public void showEnterpriseInfo(String countyName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/enterprise/selectAll";
                try {
                    showEnterprise(url,countyName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public void showEnterprise(String url,String json)throws IOException {
        OkHttpClient client=new OkHttpClient();
        RequestBody formboy=new FormBody.Builder()
                .add("countyName",json)
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(formboy)
                .build();
        Response response=client.newCall(request).execute();
        if(response.isSuccessful()){
            String p = response.body().string();
            String body = p.substring(23,p.length()-12);
            Log.e("-------",body);
            List<Enterprise> list=new Gson().fromJson(body,new TypeToken<List<Enterprise>>(){}.getType());
            //调用函数将其展示在listView中
            show(list);
        }
    }
    public void show(List<Enterprise> list){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

}
