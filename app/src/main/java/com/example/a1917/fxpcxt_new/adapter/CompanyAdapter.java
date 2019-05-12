package com.example.a1917.fxpcxt_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a1917.fxpcxt_new.R;
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
        view=LayoutInflater.from(mContext).inflate(R.layout.item_company_list,viewGroup,false);
        TextView item_company_id=view.findViewById(R.id.item_company_id);
        TextView item_company_name=view.findViewById(R.id.item_company_name);
        TextView item_company_linkman=view.findViewById(R.id.item_company_linkman);
        TextView item_company_address=view.findViewById(R.id.item_company_address);
        item_company_id.setText(mData.get(i).getId().toString());
        item_company_name.setText(mData.get(i).getName());
        item_company_linkman.setText(mData.get(i).getLinkman());
        item_company_address.setText(mData.get(i).getAddress());
        return view;
    }
}
