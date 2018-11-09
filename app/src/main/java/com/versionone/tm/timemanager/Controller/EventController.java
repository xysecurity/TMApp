package com.versionone.tm.timemanager.Controller;
import android.content.Entity;
import android.database.Cursor;
import android.util.Log;


import com.versionone.tm.timemanager.DBmgr.DBManager;
import com.versionone.tm.timemanager.Entity.event;

import java.util.ArrayList;
import java.util.List;

public class EventController {
    //添加事件
    public void CreateEvent(String title,String content, String mode,  String reminder,  String date,String Username, DBManager dbe){
        event newevent = new event(title,content,mode,reminder,Username,date);
        Log.v("++++++++++++++","实例中的时间是"+newevent.getDate());
        dbe.open();

        dbe.save(newevent);
        dbe.close();
    }
    /*//检查单个事件
    public ArrayList<String> checkout(int id , DBManager db){
        ArrayList<String> SingleEvent =  new ArrayList<String>();
        event event1 ;
        db.open();
        event1= db.getEvent(id);
        SingleEvent.add((event1.getTitle()));
        SingleEvent.add(event1.getContent());
        SingleEvent.add(event1.getMode());
        SingleEvent.add(event1.getPictureSrc());
        SingleEvent.add(event1.getReminder());
        SingleEvent.add(event1.getSelf_commitment());
        SingleEvent.add(event1.getUsername());
        SingleEvent.add(event1.getDate());
        db.close();
        return SingleEvent;
    }*/
    public ArrayList<event> getlist(DBManager db){
        db.open();
        ArrayList<event> eventlist =new ArrayList<event>();
        Cursor c = db.getEvent();
        while (c.moveToNext()) {
            int idtmp = c.getInt(0);
            String titletmp = c.getString(1);
            String contenttmp = c.getString(2);
            String modetmp = c.getString(3);
            String PictureSrctmp = c.getString(4);
            String remindertmp = c.getString(5);
            String self_commitmenttmp = c.getString(6);
            String Usernametmp = c.getString(7);
            String datetmp = c.getString(8);
            event event1 = new event(idtmp,titletmp, contenttmp, modetmp,  remindertmp,  Usernametmp, datetmp);
            eventlist.add(event1);
        }
        db.close();
        return eventlist;
    }


    //删除事件
    public void deleteEvent(int id, DBManager db){
        db.open();
        db.delete(id);
        db.close();
    }

    //修改事件
    public void reeditEvent(int id){

    }

    //同步事件
    public void synchronize(){

    }
}
