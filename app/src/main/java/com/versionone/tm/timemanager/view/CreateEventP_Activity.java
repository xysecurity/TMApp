package com.versionone.tm.timemanager.view;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.Toast;
import com.versionone.tm.timemanager.Controller.EventController;
import com.versionone.tm.timemanager.DBmgr.DBManager;
import com.versionone.tm.timemanager.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;



public class CreateEventP_Activity extends AppCompatActivity{
    private DBManager dbManager;
    private Context context;
    private EditText myEditText;
    private EditText editTitle;
    private MediaPlayer mMediaPlayer;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private ImageView ShowPicture;
    private ImageView TakePhoto;
    private ImageView FromAlbum;
    private Uri imageUri;
    private String music;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createeventp);
        context = this;// 上下文赋值
        dbManager = new DBManager(context);
        myEditText = (EditText) findViewById(R.id.editDate);
        myEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
        editTitle = (EditText)findViewById(R.id.editTitle);
        TakePhoto = (ImageView)findViewById(R.id.TakePhoto);
        ShowPicture = (ImageView)findViewById(R.id.ShowPicture);
        FromAlbum = (ImageView)findViewById(R.id.FromAlbum);
        TakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String filePath = Environment.getExternalStorageDirectory() + "/images/"+System.currentTimeMillis()+".jpg";
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    Log.v("~~~~~~~~~~~~~~~~","result: "+Build.VERSION.SDK_INT);
                    imageUri= FileProvider.getUriForFile(CreateEventP_Activity.this,"com.versionone.tm.timemanager.fileprovider",outputImage);
                }else{
                    imageUri=Uri.fromFile(outputImage);
                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                Log.v("++++++++++++++","imageUri: "+imageUri);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        FromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(CreateEventP_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CreateEventP_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });


        Switch reminder_switch=(Switch)findViewById(R.id.reminder_switch);
        final RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radioGroup);
        reminder_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radiogroup.setVisibility(View.VISIBLE);  //打开listview
                    RadioButton music1 = (RadioButton) findViewById(R.id.music1);
                    RadioButton music2 = (RadioButton) findViewById(R.id.music2);
                    RadioButton music3 = (RadioButton) findViewById(R.id.music3);
                    music1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mMediaPlayer != null) {
                                mMediaPlayer.stop();
                            }
                            startAlarm(1);
                        }
                    });
                    music2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mMediaPlayer != null) {
                                mMediaPlayer.stop();
                            }
                            startAlarm(2);
                        }
                    });
                    music3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mMediaPlayer != null) {
                                mMediaPlayer.stop();
                            }
                            startAlarm(3);
                        }
                    });
                } else {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.stop();
                    }
                    radiogroup.setVisibility(View.GONE);
                }
            }

        });
        Button AddEventBtn=(Button)findViewById(R.id.AddEventBtn);
        AddEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTitle=editTitle.getText().toString();
                String strDate=myEditText.getText().toString();
                String strmode=imageUri.toString();
                String strreminder=music;
                String strusername=null;
                //String strContent=editContent.getText().toString();
                //save Event
                EventController ec = new EventController();
                //ec.CreateEvent(strTitle,strContent,strmode,strreminder,strDate,strusername, dbManager);
                ec.CreateEvent(strTitle,null,strmode,strreminder,strDate,strusername, dbManager);
                Log.v("++++++++++++++","保存的strmode是！！：！"+strmode);
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                Intent startIntent = new Intent(getApplicationContext(),MainEvent_Activity.class);
                startActivity(startIntent);
            }
        });
    }  //end of onCreate



    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }


    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ShowPicture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content//download/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            ShowPicture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }


    protected void showDatePickDlg(){

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventP_Activity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                CreateEventP_Activity.this.myEditText.setText(monthOfYear+"/"+dayOfMonth + "/" +year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    protected void startAlarm(int flag) {
        Log.v("startAlarm","开始flag的值："+flag);
        mMediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri(flag));

        mMediaPlayer.setLooping(false);
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("startAlarm","jieshu");
    }

    private Uri getSystemDefultRingtoneUri(int flag) {
        int num=0;
        switch(flag){
            case 1:
                num = RingtoneManager.TYPE_RINGTONE;
                music="music1";
                break;
            case 2:
                num=RingtoneManager.TYPE_NOTIFICATION;
                music="music2";
                break;
            case 3:
                num=RingtoneManager.TYPE_ALARM;
                music="music3";
                break;
            default:
                break;

        }
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                num);

    }


}//end of CreateActivity
