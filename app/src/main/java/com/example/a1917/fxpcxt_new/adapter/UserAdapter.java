package com.example.a1917.fxpcxt_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a1917.fxpcxt_new.R;
import com.example.a1917.fxpcxt_new.entity.User;

import java.util.LinkedList;

public class UserAdapter extends BaseAdapter {
    private LinkedList<User> mData;
    private Context mContext;
    public UserAdapter(LinkedList<User> mData,Context mContext){
        this.mData=mData;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view=LayoutInflater.from(mContext).inflate(R.layout.item_user_list,viewGroup,false);
        TextView tId=view.findViewById(R.id.item_id);
        TextView tAccount=view.findViewById(R.id.item_account);
        TextView tPassword=view.findViewById(R.id.item_password);
        TextView tName=view.findViewById(R.id.item_name);
        tId.setText(mData.get(position).getId() + "");
        tAccount.setText(mData.get(position).getAccount());
        tPassword.setText(mData.get(position).getPassword());
        tName.setText(mData.get(position).getName());
        return view;
    }
}
