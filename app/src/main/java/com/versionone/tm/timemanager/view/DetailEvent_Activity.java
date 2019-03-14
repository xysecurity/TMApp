package com.versionone.tm.timemanager.view;
import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ddz.floatingactionbutton.FloatingActionButton;
import com.versionone.tm.timemanager.Controller.EventController;
import com.versionone.tm.timemanager.DBmgr.DBManager;
import com.versionone.tm.timemanager.Entity.event;
import com.versionone.tm.timemanager.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;

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
        Button ReeditBtn=(Button)findViewById(R.id.ReeditBtn);
        FloatingActionButton sharebywxbutton=(FloatingActionButton)findViewById(R.id.wechat);
        FloatingActionButton sharebymomentbutton=(FloatingActionButton)findViewById(R.id.moment);
        FloatingActionButton sharebymailbutton=(FloatingActionButton)findViewById(R.id.mail);
        event e= (event) getIntent().getSerializableExtra("serializable");
        TextView titleText = (TextView)findViewById(R.id.titleText);
        TextView DateText = (TextView)findViewById(R.id.DateText);
        TextView MusicText = (TextView)findViewById(R.id.MusicText);
        EditText ContentText = (EditText)findViewById(R.id.ContentText);
        titleText.setText("Title: "+e.getTitle());
        DateText.setText("Date: "+e.getDate());
        MusicText.setText("Music: "+e.getReminder());
        if(e.getContent()!=null){
            ContentText.setText("Content: "+e.getContent());
        }else{
            //ContentText.setText("Content: "+Uri.parse((String) e.getMode()));
            //byte[] data = GetImage.getImage(e.getMode());
        }

        initPhotoError();

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        ReeditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event e= (event) getIntent().getSerializableExtra("serializable");
                Intent startIntent = new Intent(getApplicationContext(),Reedit_Activity.class);
                startIntent.putExtra("eventID",e.getId());
                startIntent.putExtra("titleText",e.getTitle());
                startIntent.putExtra("DateText",e.getDate());
                startIntent.putExtra("MusicText",e.getReminder());
                startIntent.putExtra("ContentText",e.getContent());
                startActivity(startIntent);
            }
        });
        sharebywxbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharescreentowx(v);
            }
        });
        sharebymomentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharescreentomoment(v);
            }
        });
        sharebymailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event e= (event) getIntent().getSerializableExtra("serializable");
                sharebymail(e.getContent());
            }
        });




    }
    private void initPhotoError(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
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

    private void shareTowx(File file) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra("Kdescription","test123456");
        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
        startActivity(intent);
    }
    private void shareTomoment(File file) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra("Kdescription","test123456");
        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
        startActivity(intent);
    }
    public void sharescreentowx(View v){
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        String sdpath="/sdcard"+File.separator+"screen.png";
        View view=v.getRootView();//getrootview是用来获取当前view层次中最顶层的view
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap=Bitmap.createBitmap(view.getDrawingCache());
        if (bitmap!=null){
            System.out.print("捕捉到图片");
            try{
                FileOutputStream out=new FileOutputStream(sdpath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                System.out.println(sdpath);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }else{
            System.out.print("Bitmap is null");
        }
        File screen =new File(sdpath);
        shareTowx(screen);

    }
    public void sharebymail(String content){
        Intent email=new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL,"test@qq.com");
        email.putExtra(Intent.EXTRA_SUBJECT,"TMItems");
        email.putExtra(Intent.EXTRA_TEXT,content);
        email.setType("text/plain");
        startActivity(email);
    }
    public void sharescreentomoment(View v){
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        String sdpath="/sdcard"+File.separator+"screen.png";
        View view=v.getRootView();//getrootview是用来获取当前view层次中最顶层的view
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap=Bitmap.createBitmap(view.getDrawingCache());
        if (bitmap!=null){
            System.out.print("捕捉到图片");
            try{
                FileOutputStream out=new FileOutputStream(sdpath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                System.out.println(sdpath);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }else{
            System.out.print("Bitmap is null");
        }
        File screen =new File(sdpath);
        shareTomoment(screen);

    }

}
