package com.example.a1917.fxpcxt_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a1917.fxpcxt_new.R;
import com.example.a1917.fxpcxt_new.entity.HazardClearRecords;

import java.util.LinkedList;

public class DangerAdapter extends BaseAdapter {
    private LinkedList<HazardClearRecords> mData;
    private Context mContext;
    public DangerAdapter(LinkedList<HazardClearRecords> mData,Context mContext){
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=LayoutInflater.from(mContext).inflate(R.layout.item_danger_list,viewGroup,false);
        TextView dId=view.findViewById(R.id.item_danger_id);
        TextView dEnterpriseName=view.findViewById(R.id.item_danger_enterpriseName);
        TextView dCheckerName=view.findViewById(R.id.item_danger_checkerName);
        TextView dStatus=view.findViewById(R.id.item_danger_status);
        TextView dLevel=view.findViewById(R.id.item_danger_level);
        TextView dChangerName=view.findViewById(R.id.item_danger_changerName);
        dId.setText(mData.get(i).getId().toString());
        dEnterpriseName.setText(mData.get(i).getEnterpriseName());
        dCheckerName.setText(mData.get(i).getCheckerName());
        dStatus.setText(mData.get(i).getStatus().toString());
        dLevel.setText(mData.get(i).getHazardLevel());
        dChangerName.setText(mData.get(i).getChangerName());
        return view;
    }
}
