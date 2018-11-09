package com.versionone.tm.timemanager.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.versionone.tm.timemanager.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends BaseAdapter {
    LayoutInflater mInflator;
    Map<String,String> map;
    List<String> names;
    List<String> times;

    public ItemAdapter(Context c,Map m){
        mInflator = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        map=m;
        names=new ArrayList<String>(map.keySet());
        times=new ArrayList<String>(map.values());
    }


    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=mInflator.inflate(R.layout.item_layout,null);
        TextView nameTextView=(TextView)v.findViewById(R.id.nameTextView);
        TextView timeTextView=(TextView)v.findViewById(R.id.timeTextView);

        nameTextView.setText(names.get(position));
        timeTextView.setText(times.get(position));
        return v;
    }
}
