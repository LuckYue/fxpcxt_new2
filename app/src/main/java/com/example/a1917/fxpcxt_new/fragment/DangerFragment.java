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
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.DangerItemActivity;
import com.example.a1917.fxpcxt_new.R;
import com.example.a1917.fxpcxt_new.adapter.DangerAdapter;
import com.example.a1917.fxpcxt_new.common.CommonResponse;
import com.example.a1917.fxpcxt_new.entity.Enterprise;
import com.example.a1917.fxpcxt_new.entity.HazardClearRecords;
import com.example.a1917.fxpcxt_new.entity.IndustryAndHazardType;
import com.example.a1917.fxpcxt_new.route.Routs;
import com.example.a1917.fxpcxt_new.util.CallBackUtil;
import com.example.a1917.fxpcxt_new.util.GsonUtil;
import com.example.a1917.fxpcxt_new.util.OkhttpUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DangerFragment extends Fragment {
    private LinkedList<HazardClearRecords> mData;
    private List<Enterprise> enterprises;
    private List<IndustryAndHazardType> hazardTypes;
    private Context mContext;
    private DangerAdapter dangerAdapter=null;
    private ListView dangerList;
    private TextView qiyexinxi,tType;
    private Button btn_addDangerRecord;
    private Spinner spinner,typeSpinner;
    private List<String> spinnerText,typeSpinnerText;
    //private List<HazardClearRecords> hListAll;
    private List<HazardClearRecords> hList;
    private String selected,type;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(180,TimeUnit.SECONDS)
            .readTimeout(180,TimeUnit.SECONDS)
            .build();
    public DangerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_danger,container,false);
        dangerList = view.findViewById(R.id.dangerList_fragment);
        //下拉列表
        spinner=view.findViewById(R.id.spinner);
        spinnerText=new ArrayList<String>();
        qiyexinxi=view.findViewById(R.id.spinnreText);

        tType=view.findViewById(R.id.Text);
        typeSpinner=view.findViewById(R.id.hazardTypeSpinner);
        typeSpinnerText=new ArrayList<String>();
        /*typeSpinnerText=new ArrayList<String>();
        typeSpinnerText.add("职业健康");
        typeSpinnerText.add("安全管理");
        typeSpinnerText.add("特种设备");
        typeSpinnerText.add("电气安全");
        typeSpinnerText.add("消防");
        ArrayAdapter<String> typeText=new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,typeSpinnerText);
        //获取后台数据进行展示,
        typeText.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeText);*/
        selectEnterprise();
        ArrayAdapter<String> show=new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,spinnerText);
        //获取后台数据进行展示,
        show.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(show);

        return view;
    }

    //连接后台获取企业信息名
    //static List<Enterprise> tempList=null;
    public void selectEnterprise(){
        OkhttpUtil.okHttpPost(Routs.GET_ALL_ENTERPRISE, new CallBackUtil<CommonResponse<List<Enterprise>>>() {
            @Override
            public CommonResponse<List<Enterprise>> onParseResponse(Call call, Response response) {
                String p = null;
                try {
                    p=response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CommonResponse<List<Enterprise>> commonResponse = GsonUtil.getGson().fromJson(p,new TypeToken<CommonResponse<List<Enterprise>>>(){}.getType());
                return commonResponse;
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(CommonResponse<List<Enterprise>> response) {
                if(response.isSuccess()){
                    int temp = response.getData().size();
                    enterprises = response.getData();
                    // spinnerText.add("所有企业");
                    spinnerText=new ArrayList<String>();
                    for(int i=0;i<temp;i++){
                        spinnerText.add(enterprises.get(i).getName());
                        Log.e("展示所有企业",spinnerText.get(i).toString());
                    }
                    //qiyexinxi.setText("企业信息");
                    ArrayAdapter<String> show = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,spinnerText);
                    //获取后台数据进行展示
                    show.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(show);
                }
            }
        });
        /*new Thread(new Runnable() {
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
//        OkHttpClient client=new OkHttpClient();
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
                //qiyexinxi.setText("企业信息");
                ArrayAdapter<String> show=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,spinnerText);
                //获取后台数据进行展示
                show.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(show);
            }
        });*/
    }
    //触发事件
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
        //下拉列表的触发事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //下拉触发之后，根据企业名连接后台接口，找出相应的信息进行展示
                selected=spinnerText.get(i);
                Log.e("打印下拉列表选中数据",selected);
                //根据企业的行业去查找对于的隐患类别
                selectHazardTypeByEnterprise(selected);
                hList=new ArrayList<>();
                mData=new LinkedList<HazardClearRecords>();
                showEnterpriseHazardRecords(selected,type);
                //获取记录信息进行展示
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
                dangerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        return false;
                    }
                });

            }

        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = typeSpinnerText.get(position);
                hList=new ArrayList<>();
                mData=new LinkedList<HazardClearRecords>();
                showEnterpriseHazardRecords(selected,type);
                //获取记录信息进行展示
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
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //增加按钮的触发事件
       /* btn_addDangerRecord=view.findViewById(R.id.addDangerRecord);
        btn_addDangerRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,AddDangerActivity.class);
                startActivity(intent);

            }
        });*/
    }
    //根据企业名连接后台
    public void showEnterpriseHazardRecords(String enterpriseName,String hazardType){
        Map<String, String> params = new HashMap<>();
        if(enterpriseName != null && enterpriseName.length() >0){
            params.put("enterpriseName",enterpriseName);
        }
        if(hazardType != null && hazardType.length() >0){
            params.put("hazardType",hazardType);
        }
        OkhttpUtil.okHttpPost(Routs.SELECT_HAZARD_BY_ENTERPRISEANDTYPE, params, new CallBackUtil<CommonResponse<List<HazardClearRecords>>>() {
            @Override
            public CommonResponse<List<HazardClearRecords>> onParseResponse(Call call, Response response) {
                String p = null;
                try {
                    p = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CommonResponse<List<HazardClearRecords>> commonResponse = GsonUtil.getGson().fromJson(p,new TypeToken<CommonResponse<List<HazardClearRecords>>>(){}.getType());
                return commonResponse;
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(CommonResponse<List<HazardClearRecords>> response) {
                if(response.isSuccess()){
                    List<HazardClearRecords> list = response.getData();
                    if(list != null && list.size() > 0){
                        mData = new LinkedList<HazardClearRecords>();
                        for(HazardClearRecords h:list)
                            mData.add(h);
                        Log.e("打印获取数据",mData.get(0).toString());
                        dangerAdapter=new DangerAdapter((LinkedList<HazardClearRecords>) mData,mContext);
                        dangerList.setAdapter(dangerAdapter);
                    }else{
                        dangerAdapter=new DangerAdapter((LinkedList<HazardClearRecords>) new LinkedList<HazardClearRecords>() ,mContext);
                        dangerList.setAdapter(dangerAdapter);
                        Toast.makeText(mContext, "该企业暂无隐患记录", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/hazardclearancerecords/selectRecordsByEnterpriseId";
                try {
                    showRecordsByEnterpriseName(url,enterpriseName,hazardType);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public List<HazardClearRecords> showRecordsByEnterpriseName(String url,String enterpriseName,String hazardType)throws IOException{
        if(enterpriseName ==null && hazardType==null){
            return null;
        }
//        OkHttpClient client=new OkHttpClient();
        RequestBody formBody = null;
        if(enterpriseName !=null && hazardType!=null){
             formBody=new FormBody.Builder()
                    .add("enterpriseName",enterpriseName)
                    .add("hazardType",hazardType)
                    .build();
        }else if(hazardType==null){
             formBody=new FormBody.Builder()
                    .add("enterpriseName",enterpriseName)
                    .build();
        }else{
             formBody=new FormBody.Builder()
                    .add("hazardType",hazardType)
                    .build();
        }


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
                    mData = new LinkedList<HazardClearRecords>();
                    for(HazardClearRecords h:list)
                        mData.add(h);
                    Log.e("打印获取数据",mData.get(0).toString());
                    dangerAdapter=new DangerAdapter((LinkedList<HazardClearRecords>) mData,mContext);
                    dangerList.setAdapter(dangerAdapter);
                }else{
                    dangerAdapter=new DangerAdapter((LinkedList<HazardClearRecords>) new LinkedList<HazardClearRecords>() ,mContext);
                    dangerList.setAdapter(dangerAdapter);
                    Toast.makeText(mContext, "该企业暂无隐患记录", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
    /*//连接后台获取所有记录信息
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
    }*/
    /*public  List<HazardClearRecords> showAll(String url)throws IOException{
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

    }*/
   /* public void showAll(List<HazardClearRecords> list){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(HazardClearRecords h:list)
                    mData.add(h);
                dangerAdapter=new DangerAdapter((LinkedList<HazardClearRecords>) mData,mContext);
                dangerList.setAdapter(dangerAdapter);
            }
        });
    }*/

   public void selectHazardTypeByEnterprise(String enterpriseName){
       HashMap<String,String> hashMap = new HashMap<>();
       hashMap.put("enterpriseName",enterpriseName);
       OkhttpUtil.okHttpPost(Routs.SELECT_HAZARD_TYPE, hashMap,new CallBackUtil<CommonResponse<List<IndustryAndHazardType>>>() {
           @Override
           public CommonResponse<List<IndustryAndHazardType>> onParseResponse(Call call, Response response) {
               String p = null;
               try {
                   p=response.body().string();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               CommonResponse<List<IndustryAndHazardType>> commonResponse = GsonUtil.getGson().fromJson(p,new TypeToken<CommonResponse<List<IndustryAndHazardType>>>(){}.getType());
               return commonResponse;
           }

           @Override
           public void onFailure(Call call, Exception e) {
               Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onResponse(CommonResponse<List<IndustryAndHazardType>> response) {
                if(response.isSuccess()){
                    int temp=response.getData().size();
                    hazardTypes = response.getData();
                    // spinnerText.add("所有企业");
                    typeSpinnerText=new ArrayList<String>();
                    for(int i=0;i<temp;i++){
                        typeSpinnerText.add(hazardTypes.get(i).getHazardType());
                    }
                    //qiyexinxi.setText("企业信息");
                    ArrayAdapter<String> show = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,typeSpinnerText);
                    //获取后台数据进行展示
                    show.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    typeSpinner.setAdapter(show);
                }
           }
       });
   }
}
