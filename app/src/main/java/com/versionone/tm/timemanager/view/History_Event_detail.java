package com.versionone.tm.timemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.versionone.tm.timemanager.R;

public class History_Event_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history__event_detail);
        Intent intent =getIntent();
        String E_date = intent.getStringExtra("Event_date") + "/" + intent.getStringExtra("Event_year");

        TextView date_V = (TextView)findViewById(R.id.dateView);
        date_V.setText(E_date);
        String E_detai = intent.getStringExtra("Event_detail");
        TextView detail_V = (TextView)findViewById(R.id.detailView);
        detail_V.setText(E_detai);
    }
}
