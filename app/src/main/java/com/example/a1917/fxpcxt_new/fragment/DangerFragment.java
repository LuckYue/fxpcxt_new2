package com.example.a1917.fxpcxt_new.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a1917.fxpcxt_new.AddDangerActivity;
import com.example.a1917.fxpcxt_new.DangerItemActivity;
import com.example.a1917.fxpcxt_new.R;
import com.example.a1917.fxpcxt_new.adapter.DangerAdapter;
import com.example.a1917.fxpcxt_new.common.CommonResponse;
import com.example.a1917.fxpcxt_new.entity.Enterprise;
import com.example.a1917.fxpcxt_new.entity.HazardClearRecords;
import com.example.a1917.fxpcxt_new.entity.Role;
import com.example.a1917.fxpcxt_new.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DangerFragment extends Fragment {
    private LinkedList<HazardClearRecords> mData;
    private LinkedList<Enterprise> enterprises;
    private Context mContext;
    private DangerAdapter dangerAdapter=null;
    private ListView dangerList;
    private TextView qiyexinxi;
    private Button btn_addDangerRecord;
    private Spinner spinner;
    private List<String> spinnerText;
    private List<HazardClearRecords> hListAll;
    private List<HazardClearRecords> hList;
    public DangerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext=getContext();
        View view=inflater.inflate(R.layout.fragment_danger,container,false);
        dangerList=view.findViewById(R.id.dangerList_fragment);
        //下拉列表
        spinner=view.findViewById(R.id.spinner);
        spinnerText=new ArrayList<String>();
        qiyexinxi=view.findViewById(R.id.spinnreText);
        spinnerText.add("卢傻逼");
        spinnerText.add("1");
        spinnerText.add("2");
        spinnerText.add("3");
//        selectEnterprise();
        //ArrayAdapter<String> show=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerText);
        ArrayAdapter<String> show=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinnerText);
        //获取后台数据进行展示,
        show.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(show);

        return view;
    }

    //连接后台获取企业信息名
    static List<Enterprise> tempList=null;
    public void selectEnterprise(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/enterprise/selectAllEnterprise";
                try {
                    select(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public List<Enterprise> select(String url) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .build();
        Response response=client.newCall(request).execute();
        String p = response.body().string();
        Log.i("获取企业后台返回数据",p);
        CommonResponse<List<Enterprise>> result=new Gson().fromJson(p,new TypeToken<CommonResponse<List<Enterprise>>>(){}.getType());
        if(result.isSuccess()){
            showEnterprise(result.getData());
            return result.getData();
        }else{
            throw new RuntimeException("获取企业列表失败："+result.getMessage());
        }
    }
    private void showEnterprise(List<Enterprise> enterprises){
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                int temp=enterprises.size();
                // spinnerText.add("所有企业");
                for(int i=0;i<temp;i++){
                    spinnerText.add(enterprises.get(i).getName());
                    Log.e("展示所有企业",spinnerText.get(i).toString());
                }
                qiyexinxi.setText("企业信息");
                ArrayAdapter<String> show=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,spinnerText);
                //获取后台数据进行展示
                show.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(show);
            }
        });
    }
    //触发事件
    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
        //下拉列表的触发事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //下拉触发之后，根据企业名连接后台接口，找出相应的信息进行展示
                String selected=spinnerText.get(i);
                hList=new ArrayList<>();
                mData=new LinkedList<HazardClearRecords>();
                showEnterpriseHazardRecords(selected);
                //获取记录信息进行展示

                //dangerList=view.findViewById(R.id.dangerList_fragment);

                dangerAdapter=new DangerAdapter((LinkedList<HazardClearRecords>) mData,mContext);
                //Log.e("打印获取数据",mData.get(0).toString());
//                Log.e("打印dangerList",new Gson().toJson(dangerList));
                dangerList.setAdapter(dangerAdapter);
                dangerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("hazardClearRecords",(HazardClearRecords)dangerAdapter.getItem(i));
                        Intent intent=new Intent(mContext,DangerItemActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return false;
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                hListAll = new ArrayList<>();
                mData=new LinkedList<HazardClearRecords>();
                //下拉列表无选中时，展示所有的隐患排查信息
                showAllRecords();
                //获取记录信息进行展示

                mData=new LinkedList<HazardClearRecords>();
                //dangerList=view.findViewById(R.id.dangerList_fragment);
                dangerAdapter=new DangerAdapter((LinkedList<HazardClearRecords>) mData,mContext);
                dangerList.setAdapter(dangerAdapter);
                dangerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("hazardClearRecords",(HazardClearRecords)dangerAdapter.getItem(i));
                        Intent intent=new Intent(mContext,DangerItemActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return false;
                    }
                });

            }
        });
        //增加按钮的触发事件
        btn_addDangerRecord=view.findViewById(R.id.addDangerRecord);
        btn_addDangerRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,AddDangerActivity.class);
                startActivity(intent);

            }
        });
    }*/
    //根据企业名连接后台
    public void showEnterpriseHazardRecords(String enterpriseName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/hazardclearancerecords/selectRecordsByEnterpriseId";
                try {
                    showRecordsByEnterpriseName(url,enterpriseName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public List<HazardClearRecords> showRecordsByEnterpriseName(String url,String enterpriseName)throws IOException{
        OkHttpClient client=new OkHttpClient();
        RequestBody formBody=new FormBody.Builder()
                .add("enterpriseName",enterpriseName)
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response=client.newCall(request).execute();
        if(response.isSuccessful()){
            String p = response.body().string();
            CommonResponse<List<HazardClearRecords>> result=new Gson().fromJson(p,new TypeToken<CommonResponse<List<HazardClearRecords>>>(){}.getType());
            hList=result.getData();
            showSome(hList);
            return hList;
        }else {
            return null;
        }

    }
    public void showSome(List<HazardClearRecords> list){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(list != null && list.size() > 0){
                    for(HazardClearRecords h:list)
                        mData.add(h);
                    Log.e("打印获取数据",mData.get(0).toString());
                }

            }
        });
    }
    //连接后台获取所有记录信息
    public void showAllRecords(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/hazardclearancerecords/selectAllRecords";
                try {
                    showAll(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public  List<HazardClearRecords> showAll(String url)throws IOException{
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .build();
        Response response=client.newCall(request).execute();
        if(response.isSuccessful()){
            String p = response.body().string();
            Log.e("打印P",p);
            CommonResponse<List<HazardClearRecords>> list=new Gson().fromJson(p,new TypeToken<CommonResponse<List<HazardClearRecords>>>(){}.getType());
            hListAll=list.getData();
            showAll(hListAll);
            return hListAll;
        }else {
            return null;
        }

    }
    public void showAll(List<HazardClearRecords> list){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(HazardClearRecords h:list)
                    mData.add(h);
            }
        });
    }
}
