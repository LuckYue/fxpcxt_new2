package com.example.a1917.fxpcxt_new.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import com.bumptech.glide.load.engine.Resource;
import com.example.a1917.fxpcxt_new.AddCompanyActivity;
import com.example.a1917.fxpcxt_new.CompanyItemActivity;
import com.example.a1917.fxpcxt_new.R;
import com.example.a1917.fxpcxt_new.adapter.CompanyAdapter;
import com.example.a1917.fxpcxt_new.entity.Enterprise;
import com.example.a1917.fxpcxt_new.entity.Province;
import com.example.a1917.fxpcxt_new.entity.Role;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
    /*protected Activity mActivity;*/
    //private static final AlertDialog.Builder Observable = ;
    private Spinner spProvince;
    private Spinner spCity;
    private Spinner spArea;

    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> areaAdapter;

    private List<Province> provinces = new ArrayList<>();     //所有省份的list

    private List<String> provinceList = new ArrayList<>(); // 所有省份的list
    private List<String> cityList = new ArrayList<>();     // 当前选中省份的城市
    private List<String> areaList = new ArrayList<>();     // 当前选中城市的城区

    private String provinceName;    // 省的名字
    private String cityName;        // 市的名字
    private String areaName;        // 区的名字
    private String address;

    private int provincePosition = 0; // 当前选的省份的位置
    private int cityPosition = 0;     // 当前城市在List中的位置
    private int areaPosition = 0;     // 当前城区在list的位置

    private Button btn_add,btn_ok;
    private LinkedList<Enterprise> mData;
    private Context mContext;
    private CompanyAdapter companyAdapter;
    private ListView enterpriseList;
    public CompanyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_company,container,false);
        mContext=getContext();
        //mActivity = (Activity) getContext();

        spProvince=view.findViewById(R.id.sp_province);
        spCity=view.findViewById(R.id.sp_city);
        spArea=view.findViewById(R.id.sp_area);
        btn_ok=view.findViewById(R.id.btn_ok);
        enterpriseList=view.findViewById(R.id.companyListView);
        initJsonData();
        //Resources resources=getResources();
        //String [] province=resources.getStringArray(R.array.);
        //a创建省市县三级联动

       /* //得到县名
        String countyName=address;
        //根据县名链接后台显示企业信息
        showEnterpriseInfo(countyName);*/
        //长按触发跳转到companyItemActivity
        enterpriseList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("enterprise",(Enterprise)companyAdapter.getItem(i));
                Intent intent=new Intent(mContext,CompanyItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                companyAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return view;
    }
    /**
     * 展示数据
     */
    private void showData() {
        for (Province province : provinces) {
            provinceList.add(province.getName());
        }
        // 显示省份数据
        spProvince.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, provinceList));
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                provincePosition = position;
                provinceName = provinceList.get(position);
                // 获取当前省份对应的城市list
                cityList.clear();
                List<Province.CityBean> cityBeans = provinces.get(position).getCity();
                for (Province.CityBean city : cityBeans) {
                    cityList.add(city.getName());
                }

                // 刷新城市列表
                spCity.setSelection(0);
                cityName = cityList.get(0);
                cityAdapter.notifyDataSetChanged();

                // 刷新城区列表
                updateArea(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 显示城市数据
        spCity.setAdapter(cityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cityList));
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityPosition = position;
                cityName = cityList.get(position);
                // 刷新城区列表
                updateArea(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 显示城区数据
        spArea.setAdapter(areaAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, areaList));
        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaPosition=position;
                areaName = areaList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 更新城区数据
     *
     * @param position
     */
    private void updateArea(int position) {
        areaList.clear();
        Province.CityBean cityBean = provinces.get(provincePosition).getCity().get(position);
        areaList.addAll(cityBean.getArea());
        spArea.setSelection(0);
        areaName = areaList.get(0);
        areaAdapter.notifyDataSetChanged();
    }
    /**
     * 从assert文件夹中获取json数据
     */
    private void initJsonData() {

        Observable.create(new ObservableOnSubscribe<List<Province>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Province>> emitter) throws Exception {
                List<Province> provinces = new ArrayList<>();
                try {
                    StringBuffer sb = new StringBuffer();
                    InputStream is =getResources().getAssets().open("citylist.json");//打开json数据
                    byte[] by = new byte[is.available()];//转字节
                    int len = -1;
                    while ((len = is.read(by)) != -1) {
                        sb.append(new String(by, 0, len, "utf8"));//根据字节长度设置编码
                    }
                    is.close();// 关闭流
                    //String jsonData=new GetJsonDataUtil().getJson(this,"itylist.json");
                    // 通过Gson将字符串转成对象list
                    Gson gson = new Gson();
                    provinces = gson.fromJson(sb.toString(), new TypeToken<List<Province>>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    emitter.onNext(provinces);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Province>>() {
                    @Override
                    public void accept(List<Province> provinces) throws Exception {
                        if (provinces != null && provinces.size() > 0) {
                            CompanyFragment.this.provinces = provinces;
                            // 显示数据
                            showData();
                        }
                    }
                });
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
                .add("address",json)
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
                mData=new LinkedList<Enterprise>();
                for(Enterprise a:list)
                    mData.add(a);
                companyAdapter=new CompanyAdapter(mData,mContext);
                enterpriseList.setAdapter(companyAdapter);
            }
        });
    }

    //add的触发事件

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_add=getActivity().findViewById(R.id.addCompany);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,AddCompanyActivity.class);
                startActivity(intent);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address=provinceName + cityName + areaName;
                Log.e("地址",address);
                //得到地址
                String countyName=address;
                //根据县名链接后台显示企业信息
                showEnterpriseInfo(countyName);
            }
        });

    }

}
