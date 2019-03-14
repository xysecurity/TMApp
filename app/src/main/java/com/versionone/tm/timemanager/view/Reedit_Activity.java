package com.versionone.tm.timemanager.view;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
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
import com.versionone.tm.timemanager.tools.Alarm_Notification;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
public class Reedit_Activity extends AppCompatActivity{
    private DBManager dbManager;
    private Context context;
    private EditText myEditText;
    private EditText editTitle;
    private EditText editContent;
    private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reedit_activity);
        context = this;// 上下文赋值
        dbManager = new DBManager(context);
        Intent intent = getIntent();
        final int id = intent.getIntExtra("eventID",0);
        String DateText = intent.getStringExtra("DateText");
        String titleText = intent.getStringExtra("titleText");
        String ContentText = intent.getStringExtra("ContentText");
        myEditText = (EditText) findViewById(R.id.editDate);
        myEditText.setText(DateText);
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
        editContent = (EditText)findViewById(R.id.editContent);
        editTitle.setText(titleText);
        editContent.setText(ContentText);
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
                String strmode=null;
                String strreminder=null;
                String strusername=null;
                String strContent=editContent.getText().toString();
                //save Event
                EventController ec = new EventController();
                ec.reeditEvent(id,strTitle,strContent,strDate,dbManager);
                Log.v("++++++++++++++","更新的时间是！！：！"+strDate);
                Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                Intent startIntent = new Intent(getApplicationContext(),MainEvent_Activity.class);
                startActivity(startIntent);
                //notification set!  in 7 second for representation
                AlarmManager alarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                Intent intent = new Intent(Reedit_Activity.this,Alarm_Notification.class);
                intent.putExtra("title",strTitle);
                PendingIntent pi = PendingIntent.getService(Reedit_Activity.this,0,intent,0);
                alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 7000, pi);
            }
        });
    }  //end of onCreate
    protected void showDatePickDlg(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Reedit_Activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                Reedit_Activity.this.myEditText.setText(monthOfYear+"/"+dayOfMonth + "/" +year);
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
                break;
            case 2:
                num=RingtoneManager.TYPE_NOTIFICATION;
                break;
            case 3:
                num=RingtoneManager.TYPE_ALARM;
                break;
            default:
                break;
        }
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                num);
    }
}//end of Activity
