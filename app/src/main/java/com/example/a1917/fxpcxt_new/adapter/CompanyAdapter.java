package com.example.a1917.fxpcxt_new.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.a1917.fxpcxt_new.entity.Enterprise;

import java.util.LinkedList;

public class CompanyAdapter extends BaseAdapter {
    private LinkedList<Enterprise> mData;
    private Context mContext;

    public CompanyAdapter(LinkedList<Enterprise> mData,Context mContext){
        this.mData=mData;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
