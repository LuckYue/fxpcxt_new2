package com.example.a1917.fxpcxt_new.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a1917.fxpcxt_new.R;
import com.example.a1917.fxpcxt_new.entity.Role;

import java.util.LinkedList;

public class RoleAdapter extends BaseAdapter {
    private LinkedList<Role> mData;
    private Context mContext;
    public RoleAdapter(LinkedList<Role> mData,Context mContext){
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
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view=LayoutInflater.from(mContext).inflate(R.layout.item_role_list,viewGroup,false);
        TextView rId=view.findViewById(R.id.item_role_id);
        TextView rName=view.findViewById(R.id.item_role_name);
        TextView rStatus=view.findViewById(R.id.item_role_status);
        rId.setText(mData.get(position).getId().toString());
        rName.setText(mData.get(position).getName());
        //Log.e("eeeeeeeeeeeee",mData.get(position).getStatus().toString());
        rStatus.setText(mData.get(position).getStatus()==null?"":mData.get(position).getStatus().toString());
        return view;
    }
}
