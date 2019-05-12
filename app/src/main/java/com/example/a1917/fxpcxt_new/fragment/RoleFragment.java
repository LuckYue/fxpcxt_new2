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
import android.widget.Button;
import android.widget.ListView;

import com.example.a1917.fxpcxt_new.AddRoleActivity;
import com.example.a1917.fxpcxt_new.R;
import com.example.a1917.fxpcxt_new.RoleItemActivity;
import com.example.a1917.fxpcxt_new.UserItemActivity;
import com.example.a1917.fxpcxt_new.adapter.RoleAdapter;
import com.example.a1917.fxpcxt_new.entity.Role;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoleFragment extends Fragment {
    private LinkedList<Role> mData;
    private Context mContext;
    private RoleAdapter roleAdapter;
    private ListView roleList;
    private Button btn_add;
    public RoleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext=getContext();
        View view=inflater.inflate(R.layout.fragment_role,container,false);
        //请求后台数据
        selectAll();

        while (list == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        roleList=view.findViewById(R.id.roleList);
        mData=new LinkedList<Role>();
        for(Role a:list)
            mData.add(a);
        roleAdapter=new RoleAdapter((LinkedList<Role>)mData,mContext);
        roleList.setAdapter(roleAdapter);
        roleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle=new Bundle();
               //bundle.putSerializable("role",(Role)roleAdapter.getItem(i));
                //把listview中的对象传到另一个Activity中
                bundle.putSerializable("role",(Role)roleAdapter.getItem(i));
                Intent intent=new Intent(mContext,RoleItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }
        });
        return view;
    }
    static List<Role> list=null;
    public List<Role> selectAll(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/role/list";
                try {
                    list=select(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return  list;
    }

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public List<Role> select(String url) throws IOException {
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
        Log.e("-------",body);
        List<Role> list=new Gson().fromJson(body,new TypeToken<List<Role>>(){}.getType());
        Log.e("000-------------",list.get(0).getId()+list.get(0).getStatus().toString());
        return list;
    }
//增加角色
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_add=getActivity().findViewById(R.id.addRole);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,AddRoleActivity.class);
                startActivity(intent);
            }
        });
    }
}
