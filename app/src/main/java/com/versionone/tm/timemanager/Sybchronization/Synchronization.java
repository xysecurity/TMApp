package com.versionone.tm.timemanager.Sybchronization;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.versionone.tm.timemanager.Controller.EventController;
import com.versionone.tm.timemanager.DBmgr.DBManager;
import com.versionone.tm.timemanager.view.MainEvent_Activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Synchronization extends MainEvent_Activity
{
    private DBManager mdbManager;
    private String mAccountName;
    private Context mcontext;

    public Synchronization(Context mcontext, DBManager mdbManager, String mAccountName)
    {
        this.mdbManager = mdbManager;
        this.mAccountName = mAccountName;
        this.mcontext = mcontext;
    }

    public void setSyn()
    {
        // 設定要返回的資料
        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,                             // 0 日歷ID
                CalendarContract.Calendars.ACCOUNT_NAME,                // 1 日歷所屬的帳戶名稱
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,       // 2 日歷名稱
                CalendarContract.Calendars.OWNER_ACCOUNT,                  // 3 日歷擁有者
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,       // 4 對此日歷所擁有的權限
        };
        // 根據上面的設定，定義各資料的索引，提高代碼的可讀性
        int PROJECTION_ID_INDEX = 0;
        int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        int PROJECTION_DISPLAY_NAME_INDEX = 2;
        int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
        int PROJECTION_CALENDAR_ACCESS_LEVEL = 4;

        long calendarId = 0;

        // 查詢日歷
        Cursor cur;
        ContentResolver cr = mcontext.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        // 定義查詢條件，找出屬於上面Google帳戶及可以完全控制的日歷
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL + " = ?))";
        String[] selectionArgs = new String[]{mAccountName,
                "com.google",
                Integer.toString(CalendarContract.Calendars.CAL_ACCESS_OWNER)};
        // 因為targetSDK=25，所以要在Apps運行時檢查權限
        int permissionCheck = ContextCompat.checkSelfPermission(mcontext,
                Manifest.permission.READ_CALENDAR);
        // 建立List來暫存查詢的結果
        final List<String> accountNameList = new ArrayList<>();
        final List<Integer> calendarIdList = new ArrayList<>();
        // 如果使用者給了權限便開始查詢日歷
        if (permissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
            if (cur != null) {
                while (cur.moveToNext()) {
                    String accountName = null;
                    String displayName = null;
                    String ownerAccount = null;
                    int accessLevel = 0;
                    // 取得所需的資料
                    calendarId = cur.getLong(PROJECTION_ID_INDEX);
                    accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                    displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                    ownerAccount = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
                    accessLevel = cur.getInt(PROJECTION_CALENDAR_ACCESS_LEVEL);
                    Log.i("query_calendar", String.format("calendarId=%s", calendarId));
                    Log.i("query_calendar", String.format("accountName=%s", accountName));
                    Log.i("query_calendar", String.format("displayName=%s", displayName));
                    Log.i("query_calendar", String.format("ownerAccount=%s", ownerAccount));
                    Log.i("query_calendar", String.format("accessLevel=%s", accessLevel));

                    // 暫存資料讓使用者選擇
                    accountNameList.add(displayName);
                    calendarIdList.add((int) calendarId);

                }
                cur.close();
                query_event(calendarId);
            }

        }
        else {
            Toast toast = Toast.makeText(mcontext, "Sign in your Google account first", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public void query_event(long calendarId)
    {
        // 設定要返回的資料
        String[] INSTANCE_PROJECTION = new String[]{
                CalendarContract.Instances.EVENT_ID,      // 0 活動ID
                CalendarContract.Instances.BEGIN,         // 1 活動開始日期時間
                CalendarContract.Instances.TITLE,          // 2 活動標題
                CalendarContract.Instances.DESCRIPTION,
        };
        // 根據上面的設定，定義各資料的索引，提高代碼的可讀性
        int PROJECTION_ID_INDEX = 0;
        int PROJECTION_BEGIN_INDEX = 1;
        int PROJECTION_TITLE_INDEX = 2;
        int PROJECTION_DESCRIPTION_INDEX = 3;

        // 取得在EditText的日歷ID
        String targetCalendar = Long.toString(calendarId);
        // 指定一個時間段，查詢以下時間內的所有活動
        // 月份是從0開始，0-11
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2018, 11, 3, 8, 0);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2018, 11, 30, 8, 0);
        long endMillis = endTime.getTimeInMillis();
        // 查詢活動
        Cursor cur = null;
        ContentResolver cr = mcontext.getContentResolver();
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        // 定義查詢條件，找出上面日歷中指定時間段的所有活動
        String selection = CalendarContract.Events.CALENDAR_ID + " = ?";
        String[] selectionArgs = new String[]{targetCalendar};
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);
        // 因為targetSDK=25，所以要在Apps運行時檢查權限
        int permissionCheck = ContextCompat.checkSelfPermission(mcontext,
                Manifest.permission.READ_CALENDAR);

        // 建立List來暫存查詢的結果
        final List<Integer> eventIdList = new ArrayList<>();
        final List<Long> beginList = new ArrayList<>();
        final List<String> titleList = new ArrayList<>();
        final List<String> descriptionList = new ArrayList<>();

        // 如果使用者給了權限便開始查詢日歷
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            cur = cr.query(builder.build(),
                    INSTANCE_PROJECTION,
                    selection,
                    selectionArgs,
                    null);
            if (cur != null) {
                while (cur.moveToNext()) {
                    long eventID = 0;
                    long beginVal = 0;
                    String title = null;
                    String description = null;
                    // 取得所需的資料
                    eventID = cur.getLong(PROJECTION_ID_INDEX);
                    beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
                    title = cur.getString(PROJECTION_TITLE_INDEX);
                    description = cur.getString(PROJECTION_DESCRIPTION_INDEX);
                    Log.i("query_event", String.format("eventID=%s", eventID));
                    Log.i("query_event", String.format("beginVal=%s", beginVal));
                    Log.i("query_event", String.format("title=%s", title));
                    Log.i("query_event", String.format("description=%s", description));
                    // 暫存資料讓使用者選擇
                    eventIdList.add((int) eventID);
                    beginList.add((long) beginVal);
                    titleList.add(title);
                    descriptionList.add(description);

                    Calendar test = Calendar.getInstance();
                    test.setTimeInMillis(beginVal);

                    Date plandate = new Date(beginVal);
                    SimpleDateFormat formatechange = new SimpleDateFormat("yyyy/MM/dd kk:mm ");
                    String startDateTime = formatechange.format(plandate);

                    Log.d("Title: ",title);
                    Log.d("Description: ",description);
                    Log.d("StartTime: ",startDateTime);
                    EventController ec= new EventController();
                    ec.CreateEvent(title, description, null,  null,  startDateTime ,null,  mdbManager);
                }
                cur.close();
            }
        }
        else {
            Toast toast = Toast.makeText(this, "No authority", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
