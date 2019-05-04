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
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.AddUserActivity;
import com.example.a1917.fxpcxt_new.MainActivity;
import com.example.a1917.fxpcxt_new.R;
import com.example.a1917.fxpcxt_new.UserItemActivity;
import com.example.a1917.fxpcxt_new.UserMenuActivity;
import com.example.a1917.fxpcxt_new.adapter.UserAdapter;
import com.example.a1917.fxpcxt_new.entity.Enterprise;
import com.example.a1917.fxpcxt_new.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    private List<User> mData=null;
    private Context mContext;
    private UserAdapter userAdapter=null;
    private ListView userList;
    private Button btn_add;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        //User user = new User(1l,"1","sa","sa",1l,"安监局",1l,true,"18870954590",1L);
        View view=inflater.inflate(R.layout.fragment_user,container,false);
        //连接后台，接收数据
        //List<User> userInfoList=selectAll();
        selectAll();
//        while (list == null) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        userList=view.findViewById(R.id.userList);
//        mData=new LinkedList<User>();
////        for(User a:list)
////            mData.add(a);
//        userAdapter=new UserAdapter((LinkedList<User>)mData,mContext);
//        userList.setAdapter(userAdapter);
        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("user",(User)userAdapter.getItem(i));
                Intent intent=new Intent(mContext,UserItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }
        });
        return view;
    }
    static List<User> list=null;
    public  List<User> selectAll(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.200:7001/userInfo/selectAll";
                try {
                    list=select(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return list;
    }
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public List<User> select(String url) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .build();
        Response response=client.newCall(request).execute();

        String p = response.body().string();
        String body = p.substring(23,p.length()-12);
        Log.e("-------",body);
        List<User> list=new Gson().fromJson(body,new TypeToken<List<User>>(){}.getType());
        Log.e("000-------------",list.get(0).getId()+list.get(0).getOrgId().toString());
        showUser(list);
        return list;
    }

    private void showUser(List<User> users){
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                mData=new LinkedList<User>();
                for(User a:list)
                    mData.add(a);
                userAdapter=new UserAdapter((LinkedList<User>)mData,mContext);
                userList.setAdapter(userAdapter);
            }
        });
    }

//增加用户的信息
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_add=getActivity().findViewById(R.id.addUser);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,AddUserActivity.class);
                startActivity(intent);
            }
        });
    }
}
