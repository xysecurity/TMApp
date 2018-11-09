package com.versionone.tm.timemanager.view;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.versionone.tm.timemanager.Controller.EventController;
import com.versionone.tm.timemanager.DBmgr.DBManager;
import com.versionone.tm.timemanager.Entity.event;
import com.versionone.tm.timemanager.R;

import org.w3c.dom.Text;

public class DetailEvent_Activity extends AppCompatActivity {
    private DBManager dbManager;
    private Context context;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        dbManager=new DBManager(context);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailevent_activity);
        Button DeleteBtn=(Button)findViewById(R.id.DeleteBtn);
        event e= (event) getIntent().getSerializableExtra("serializable");
        TextView titleText = (TextView)findViewById(R.id.titleText);
        TextView DateText = (TextView)findViewById(R.id.DateText);
        EditText ContentText = (EditText)findViewById(R.id.ContentText);
        titleText.setText("Title: "+e.getTitle());
        DateText.setText("Date: "+e.getDate());
        ContentText.setText("Content: "+e.getContent());

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }
    private void showDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(DetailEvent_Activity.this);
        normalDialog.setIcon(R.drawable.ic_launcher_background);
        normalDialog.setTitle("Confirm Delete?");
        normalDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //删除数据库的操作！！！
//                        String name = titleTest.getText().toString();
//                        if (!name.equals("")) {
//                            dbManager.delete(context, name);// 根据title删除数据库
//                        }
//                        Toast.makeText(context, "Title"+name, Toast.LENGTH_SHORT).show();



//                        Intent intent = getIntent();
//                        int id = intent.getIntExtra("_id",0);
//                        //Log.v("测试","id结果："+id);
//                        dbManager.delete(id);
                        EventController ec = new EventController();
                        event e= (event) getIntent().getSerializableExtra("serializable");
                        ec.deleteEvent(e.getId(),dbManager);
                        dbManager.delete(e.getId());


                        Toast.makeText(context, "ID"+e.getId(), Toast.LENGTH_SHORT).show();
                        Intent startIntent = new Intent(getApplicationContext(),MainEvent_Activity.class);
                        startActivity(startIntent);
                    }
                });
        normalDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startIntent = new Intent(getApplicationContext(),DetailEvent_Activity.class);
                        startActivity(startIntent);
                    }
                });
        // 显示
        normalDialog.show();
    }
}
