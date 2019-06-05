package com.example.a1917.fxpcxt_new.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.a1917.fxpcxt_new.common.CommonResponse;
import com.example.a1917.fxpcxt_new.entity.Enterprise;
import com.example.a1917.fxpcxt_new.entity.User;
import com.example.a1917.fxpcxt_new.route.Routs;
import com.example.a1917.fxpcxt_new.util.CallBackUtil;
import com.example.a1917.fxpcxt_new.util.GsonUtil;
import com.example.a1917.fxpcxt_new.util.OkhttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

import static com.example.a1917.fxpcxt_new.AddDangerActivity.result;

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
        userList=view.findViewById(R.id.userList);
        Map<String,String> headers = new HashMap<String,String>();
        SharedPreferences preferences = getActivity().getSharedPreferences("Token",
                Activity.MODE_PRIVATE);
        String token = preferences.getString("Token", "");
        headers.put(Routs.AUTHORIZATION_HEADER_NAME,Routs.AUTHORIZATION_HEADER_NAME_PREFIX+token);
        //连接后台，接收数据
        OkhttpUtil.okHttpPost(Routs.GET_ALL_USER_INFO,new CallBackUtil<CommonResponse<List<User>>>() {
            @Override
            public CommonResponse<List<User>> onParseResponse(Call call, Response response) {
                String p = null;
                try {
                    p = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CommonResponse<List<User>> commonResponse = GsonUtil.getGson().fromJson(p,new TypeToken<CommonResponse<List<User>>>(){}.getType());
                return commonResponse;
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(CommonResponse<List<User>> response) {
                if(response.isSuccess()){
                    List<User> list=response.getData();
                    mData=new LinkedList<User>();
                    for(User a:list)
                        mData.add(a);
                    userAdapter=new UserAdapter((LinkedList<User>)mData,mContext);
                    userList.setAdapter(userAdapter);
                    //内容刷新
                    userAdapter.notifyDataSetChanged();
                }
            }
        });
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
