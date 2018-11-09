package com.versionone.tm.timemanager.view;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.versionone.tm.timemanager.Controller.EventController;
import com.versionone.tm.timemanager.DBmgr.DBManager;
import com.versionone.tm.timemanager.Entity.event;
import com.versionone.tm.timemanager.R;
import com.versionone.tm.timemanager.tools.ItemAdapter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MainEvent_Activity  extends AppCompatActivity {
    private DBManager dbManager;
    private Context context;
    private int i;
    private List<event> dataset;
    //ListView
    ItemAdapter itemAdapter;
    ListView myListView;
    Map<String,String> eventsMap = new LinkedHashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainevent_activity);
        int id=0;
        context=this;
        dbManager=new DBManager(context);
        EventController ec= new EventController();
        //获取数据库
        dataset =  ec.getlist(dbManager);
//        for (event event : dataset) {
//            String name = event.getTitle();
//            String time = event.getDate();
//            eventsMap.put(name,time);
//            Log.v("+++++++++++","time is : "+time);
//            id=event.getId();
//        }
        myListView=(ListView)findViewById(R.id.listView);
        GetData retrieveData=new GetData();
        retrieveData.onPostExecute(id);

        ImageView CreateEventBtn=(ImageView)findViewById(R.id.CreateEventBtn);
        CreateEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(),CreateEvent_Activity.class);
                startActivity(startIntent);
            }
        });
    }
    private class GetData extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        protected void onPostExecute(int _id){
            for(i=0;i<dataset.size();i++){
                eventsMap.put(dataset.get(i).getTitle(),dataset.get(i).getDate());
                Log.v("GetData","onPostExecute!!!!"+dataset.get(i).getTitle());
                itemAdapter = new ItemAdapter(context,eventsMap);
                myListView.setAdapter(itemAdapter);
            }
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    event e = dataset.get(position);
                    Intent showIntent = new Intent(getApplicationContext(),DetailEvent_Activity.class);
                    showIntent.putExtra("_id",e.getId());
                    showIntent.putExtra("serializable",e);
                    startActivity(showIntent);
                }
            });


//            final int delete_id = _id;
//            if(eventsMap.size()>0){
//                itemAdapter = new ItemAdapter(context,eventsMap);
//                myListView.setAdapter(itemAdapter);
//
//                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Intent showIntent = new Intent(getApplicationContext(),DetailEvent_Activity.class);
//                        showIntent.putExtra("_id",delete_id);
//                        Log.v("position","value!!!!!:"+delete_id);
//                        startActivity(showIntent);
//                    }
//                });
//            }
        }
    }



} //end of ShowActivity
