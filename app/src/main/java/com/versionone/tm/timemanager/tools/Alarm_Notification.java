package com.versionone.tm.timemanager.tools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.versionone.tm.timemanager.R;

public class Alarm_Notification extends Service {
        //oncommand 方法多次调用

        int i = 1 ;
    public void onCreate() {

        super.onCreate();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent, int flags, int startId){
        CharSequence cs1= intent.getStringExtra("title");
        sendChatmsg(cs1);
        return super.onStartCommand(intent, flags, startId);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendChatmsg( CharSequence cs1){
        String channelId = "chat";
        String channelName = "chatmsg";


        //通知显示 HGIGH
        int importance = NotificationManager.IMPORTANCE_HIGH;
        createNotificationChannel(channelId, channelName, importance);
        //CharSequence cs = cs1;
        sendChatMsg(cs1);
        i++;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
    public void sendChatMsg(CharSequence strTitle) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle(" Event Today!")
                .setContentText(strTitle)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.notificated)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notificated))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
