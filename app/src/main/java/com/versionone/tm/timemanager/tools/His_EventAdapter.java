package com.versionone.tm.timemanager.tools;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.versionone.tm.timemanager.Entity.His_Event;
import com.versionone.tm.timemanager.R;
import com.versionone.tm.timemanager.view.History_Event_detail;

import java.util.List;

public class His_EventAdapter extends BaseAdapter {

    private List<His_Event> His_Event_list;
    private Context con;

    public His_EventAdapter(List<His_Event> History_event, Context context){
        this.His_Event_list = History_event;
        this.con = context;
    }
    @Override
    public int getCount() {
        return His_Event_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final His_Event EventShow = His_Event_list.get(position);
        convertView = LayoutInflater.from(con).inflate(R.layout.history_item,parent,false);
        TextView txt_year = (TextView) convertView.findViewById(R.id.yearView);
        TextView txt_content = (TextView) convertView.findViewById(R.id.contentView);
        txt_year.setText(His_Event_list.get(position).getYear());
        txt_content.setText(His_Event_list.get(position).getContents());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(con, History_Event_detail.class);

                intent.putExtra("Event_detail", EventShow.getDetail());
                intent.putExtra("Event_date", EventShow.getDate());
                intent.putExtra("Event_year", EventShow.getYear());
                con.startActivity(intent);
            }
        });

        return convertView;
    }
}