package com.example.a1917.fxpcxt_new;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HazardExamineActivity extends AppCompatActivity {
    private List<HazardClearRecords> mData;
    private Context mContext;
    private DangerAdapter dangerAdapter;
    private ListView changeHazrdList;
    private Spinner spinner,typeSpinner;
    private List<String> spinnerText,typeSpinnerText;
    private LinkedList<Enterprise> enterprises;
    private LinkedList<IndustryAndHazardType> hazardTypes;
    private String selected,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_examine);
//        changeHazrdList = findViewById(R.id.changeHazrdList);
//        mContext = getApplicationContext();
        spinner=findViewById(R.id.spinner2);
        typeSpinner=findViewById(R.id.hazardTypeSpinner2);
        changeHazrdList = findViewById(R.id.changeHazrdList2);
        mContext = getApplicationContext();
        Intent intent=getIntent();
        if(intent != null){
            selectEnterprises();
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //下拉触发之后，根据企业名连接后台接口，找出相应的信息进行展示
                    selected=spinnerText.get(i);
                    Log.e("打印下拉列表选中数据",selected);
                    //根据企业的行业去查找对于的隐患类别
                    selectHazardTypeByEnterprise(selected);
                    //hList=new ArrayList<>();
                    mData=new LinkedList<HazardClearRecords>();
                    showEnterpriseHazardRecords(selected,type);
                    //获取记录信息进行展示
                    changeHazrdList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("hazardClearRecords",(HazardClearRecords)dangerAdapter.getItem(i));
                            Intent intent=new Intent(mContext,ChangeItemActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            return false;
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    changeHazrdList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                    //hList=new ArrayList<>();
                    mData=new LinkedList<HazardClearRecords>();
                    showEnterpriseHazardRecords(selected,type);
                    //获取记录信息进行展示
                    changeHazrdList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("hazardClearRecords",(HazardClearRecords)dangerAdapter.getItem(i));
                            Intent intent=new Intent(mContext,ChangeItemActivity.class);
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
           /* String type = (String) intent.getSerializableExtra("type");
            OkhttpUtil.okHttpPostJson(Routs.HAZARD_EXAMINE, type, new CallBackUtil<CommonResponse<List<HazardClearRecords>>>() {
                @Override
                public CommonResponse<List<HazardClearRecords>> onParseResponse(Call call, Response response) {
                    String p = null;
                    try {
                        p = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    CommonResponse<List<HazardClearRecords>> commonResponse=GsonUtil.getGson().fromJson(p,new TypeToken<CommonResponse<List<HazardClearRecords>>>(){}.getType());
                    return commonResponse;
                }

                @Override
                public void onFailure(Call call, Exception e) {
                    Toast.makeText(HazardExamineActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(CommonResponse<List<HazardClearRecords>> response) {
                    if(response.isSuccess()){
                        List<HazardClearRecords> list = response.getData();
                        if(list != null && list.size() > 0){
                            mData = new LinkedList<HazardClearRecords>();
                            for(HazardClearRecords a:list){
                                mData.add(a);
                            }
                            dangerAdapter=new DangerAdapter((LinkedList<HazardClearRecords>) mData,mContext);
                            changeHazrdList.setAdapter(dangerAdapter);
                        }else {
                            dangerAdapter=new DangerAdapter((LinkedList<HazardClearRecords>) mData,mContext);
                            changeHazrdList.setAdapter(dangerAdapter);
                            Toast.makeText(mContext, "暂无记录", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });*/
            changeHazrdList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("hazardClearRecords",(HazardClearRecords)dangerAdapter.getItem(i));
                    Intent intent=new Intent(mContext,RecheckActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return false;
                }
            });
        }
    }
    public void selectEnterprises() {
        OkhttpUtil.okHttpPost(Routs.GET_ALL_ENTERPRISE, new CallBackUtil<CommonResponse<List<Enterprise>>>() {
            @Override
            public CommonResponse<List<Enterprise>> onParseResponse(Call call, Response response) {
                String p = null;
                try {
                    p = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CommonResponse<List<Enterprise>> commonResponse = GsonUtil.getGson().fromJson(p, new TypeToken<CommonResponse<List<Enterprise>>>() {
                }.getType());
                return commonResponse;
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(CommonResponse<List<Enterprise>> response) {
                if (response.isSuccess()) {
                    int temp = response.getData().size();
                    // spinnerText.add("所有企业");
                    for (int i = 0; i < temp; i++) {
                        spinnerText.add(enterprises.get(i).getName());
                        Log.e("展示所有企业", spinnerText.get(i).toString());
                    }
                    //qiyexinxi.setText("企业信息");
                    ArrayAdapter<String> show = new ArrayAdapter<String>(HazardExamineActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerText);
                    //获取后台数据进行展示
                    show.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(show);
                }
            }
        });
    }
    public void selectHazardTypeByEnterprise(String enterpriseName){
        OkhttpUtil.okHttpPost(Routs.SELECT_HAZARD_TYPE, new CallBackUtil<CommonResponse<List<IndustryAndHazardType>>>() {
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
                    // spinnerText.add("所有企业");
                    for(int i=0;i<temp;i++){
                        typeSpinnerText.add(hazardTypes.get(i).getHazardType());
                    }
                    //qiyexinxi.setText("企业信息");
                    ArrayAdapter<String> show = new ArrayAdapter<String>(HazardExamineActivity.this,android.R.layout.simple_spinner_dropdown_item,typeSpinnerText);
                    //获取后台数据进行展示
                    show.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    typeSpinner.setAdapter(show);
                }
            }
        });
    }
    //根据企业名连接后台
    public void showEnterpriseHazardRecords(String enterpriseName,String hazardType) {
        Map<String, String> params = new HashMap<>();
        if (enterpriseName != null && enterpriseName.length() > 0) {
            params.put("enterpriseName", enterpriseName);
        }
        if (hazardType != null && hazardType.length() > 0) {
            params.put("hazardType", hazardType);
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
                CommonResponse<List<HazardClearRecords>> commonResponse = GsonUtil.getGson().fromJson(p, new TypeToken<CommonResponse<List<HazardClearRecords>>>() {
                }.getType());
                return commonResponse;
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(CommonResponse<List<HazardClearRecords>> response) {
                if (response.isSuccess()) {
                    List<HazardClearRecords> list = response.getData();
                    if (list != null && list.size() > 0) {
                        mData = new LinkedList<HazardClearRecords>();
                        for (HazardClearRecords h : list)
                            mData.add(h);
                        Log.e("打印获取数据", mData.get(0).toString());
                        dangerAdapter = new DangerAdapter((LinkedList<HazardClearRecords>) mData, mContext);
                        changeHazrdList.setAdapter(dangerAdapter);
                    } else {
                        dangerAdapter = new DangerAdapter((LinkedList<HazardClearRecords>) new LinkedList<HazardClearRecords>(), mContext);
                        changeHazrdList.setAdapter(dangerAdapter);
                        Toast.makeText(mContext, "该企业暂无隐患记录", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
