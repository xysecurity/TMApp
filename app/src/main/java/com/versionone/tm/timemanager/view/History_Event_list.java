package com.versionone.tm.timemanager.view;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;


import com.versionone.tm.timemanager.Controller.HisEventController;
import com.versionone.tm.timemanager.Entity.His_Event;
import com.versionone.tm.timemanager.R;
import com.versionone.tm.timemanager.tools.His_EventAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class History_Event_list extends AppCompatActivity {
    private Context con;
    private His_EventAdapter HisAdapter;
    private TextView dateText;
    HisEventController list  = new HisEventController();
    List<His_Event> contentL = new ArrayList<>();
    Workbook history_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history__event_list);
        {
            try {
                history_book = Workbook.getWorkbook(getAssets().open("history.xls"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }
        }
        dateText = (TextView)findViewById(R.id.Event_Date);
        dateText.setText(getDate());
        setList(getDate());
        Button SelectDate = (Button)findViewById(R.id.Date_S);
        SelectDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SelectPage();
            }
        });
    }
    public void setList(String  dateC){
        Sheet His_sheet = history_book.getSheet(0);
        contentL = list.HisEventController(His_sheet,dateC);
        con = History_Event_list.this;
        ListView event_list = (ListView) findViewById(R.id.event_List);
        HisAdapter = new His_EventAdapter(contentL,con);
        event_list.setAdapter(HisAdapter);
    }
    public static String getDate(){
        SimpleDateFormat Format = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date(System.currentTimeMillis());
        String E_date = Format.format(date);
        System.out.println(E_date);
        return E_date;
    }
    public void SelectPage(){
        DatePickerDialog dateSelect=new DatePickerDialog(History_Event_list.this, new OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String NewDate = (monthOfYear+1)+"/"+dayOfMonth + "/" +year;
                History_Event_list.this.dateText.setText(NewDate);
                History_Event_list.this.setList(NewDate);
            }
         }, 2018, 11, 3);

        dateSelect.show();
    }



}
