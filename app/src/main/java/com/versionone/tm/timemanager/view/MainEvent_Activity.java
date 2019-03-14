package com.versionone.tm.timemanager.view;
import android.Manifest;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.versionone.tm.timemanager.Controller.EventController;
import com.versionone.tm.timemanager.DBmgr.DBManager;
import com.versionone.tm.timemanager.Entity.event;
import com.versionone.tm.timemanager.R;
import com.versionone.tm.timemanager.Sybchronization.Synchronization;
import com.versionone.tm.timemanager.tools.ItemAdapter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MainEvent_Activity  extends AppCompatActivity {
    private DBManager dbManager;
    private Context context;
    private LinearLayout linearLayout;
    private int i;
    private List<event> dataset;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private String accountName;
    static final int GET_ACCOUNT_NAME_REQUEST = 1;  // 自訂的要求代碼

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
        myListView=(ListView)findViewById(R.id.listView);

        GetData retrieveData=new GetData();
        retrieveData.onPostExecute(id);

        ImageView CreateEventBtn=(ImageView)findViewById(R.id.CreateEventBtn);
        CreateEventBtn.bringToFront();
        CreateEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("createeventBtn:",":::::::");
                showDialog();

            }
        });

        //Navigation Drawer
        mDrawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                int id = menuItem.getItemId();
                if(id == R.id.Historical_event)
                {
                    Intent intent = new Intent();
                    intent.setClass(MainEvent_Activity.this,History_Event_list.class);
                    startActivity(intent);
                    return true;
                }
                else if(id == R.id.Synchronize)
                {
                    get_account_name();

                    return true;
                }
                else if(id == R.id.Date_Caculator){
                    Intent startIntent = new Intent(getApplicationContext(),Calculator_Activity.class);
                    startActivity(startIntent);
                    return true;
                }
                return false;
            }
        });
    }//End of Navigation Drawer

    private void showDialog(){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainEvent_Activity.this);
        normalDialog.setIcon(R.drawable.ic_launcher_background);
        normalDialog.setTitle("Select Mode");
        normalDialog.setPositiveButton("Photo Mode",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startIntent = new Intent(getApplicationContext(),CreateEventP_Activity.class);
                        startActivity(startIntent);
                    }
                });
        normalDialog.setNegativeButton("Text Mode",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startIntent = new Intent(getApplicationContext(),CreateEvent_Activity.class);
                        startActivity(startIntent);
                    }
                });
        // 显示
        normalDialog.show();
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

        }
    }

    //Navigation drawer
    public void get_account_name()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR}, 1);

        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, false, null, null, null, null);
        startActivityForResult(intent, GET_ACCOUNT_NAME_REQUEST );

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GET_ACCOUNT_NAME_REQUEST && resultCode == RESULT_OK) {
            Context m = getApplicationContext();
            accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME); // 使用者所選的帳戶名稱
            Synchronization syn = new Synchronization(m, dbManager, accountName);
            syn.setSyn();
            this.recreate();
            Toast toast = Toast.makeText(this, "Finish Synchronization", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //End of Navigation drawer
}
