package com.versionone.tm.timemanager.DBmgr;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.versionone.tm.timemanager.Entity.User;
import com.versionone.tm.timemanager.Entity.event;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.ID;

public class DBManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "TM.db";
    //private static final String DB_NAME = "user.db";
    //定义表名
    private static final String TBL_NAME = "user";
    private static final String TBL_NAME_event = "event";
    //建立数据库表SQL语句
    private static final String CREATE_TBL = "create table user " +
            "(id integer primary key autoincrement,name varchar(20) unique,email varchar(20),password varchar(20))";
    private static final String CREATE_TBL_event = "create table event " +
            "(id integer primary key autoincrement,title vachar(20) unique,content varchar(200),mode varchar(20),PictureSrc varchar(20),reminder varchar(20),self_commitment varchar(20), Username varchar(20), date varchar(20))";
    //SQLiteDatabase
    private SQLiteDatabase db;

    public DBManager(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory,
                     int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL_event);
        db.execSQL(CREATE_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //db open
    public void open() {
        db = getWritableDatabase();
    }
    //close db
    public void close(){
        if (db != null){
            db.close();

        }
    }
    /*
     ************************   ************************   ************************   ************************
     ************************   *********USer***********   **********USer********   ************************
     ************************   ************************   ************************   ************************
     ************************   ************************   ************************   ************************

     */
    //@User
    public void save(User user){
        db.execSQL("insert into user values(null,?,?,?)",new String []{user.getUserName(),user.getEmail(),user.getPassword()});
        Log.i("=======","email" + user.getEmail()+" name "+user.getUserName()+" psd "+user.getPassword());
    }
    public User getUser(String UserName){
        Cursor c = db.rawQuery("select * from user where name='"+UserName+"'", null);
        String username,email,password;
        User user;
        while (c.moveToNext()) {
            String id = c.getString(0);
            email= c.getString(2);
            username = c.getString(1);
            password = c.getString(3);
            user = new User(email,username,password);
            return user;
        }
        return null;
    }
    public void updateUser(User user){
        String user_name= user.getUserName();
        String new_password=user.getPassword();
        ContentValues values =new ContentValues();
        values.put("password",new_password);
        db.update(TBL_NAME, values, "name=?", new String[]{""+user_name});
        Log.i("***********"," 修改数据库成功");

    }
    public Cursor query(String name){
        Cursor c = db.rawQuery("select * from user where name='"+name+"'", null);
        //Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
        return c;
    }



    /*
            ************************   ************************   ************************   ************************
            ************************   *********Event***********   *********Event*********   ************************
            ************************   ************************   ************************   ************************
            ************************   ************************   ************************   ************************

    */
    //@UEvent
    public void save(event event){
        ContentValues values = new ContentValues();
        values.put("title",event.getTitle());
        values.put("content",event.getContent());
        values.put("mode",event.getMode());
        //values.put("PictureSrc",event.getPictureSrc());
        values.put("reminder",event.getReminder());
        values.put("Username",event.getUsername());
        values.put("date",event.getDate());
        Log.v("+++++++++++****","数据库插入的时间是"+event.getDate());
        //values.put("self_commitment", event.getSelf_commitment());
        db.insert(TBL_NAME_event, null, values);
        //test insert event
        Log.i("=========","save event!!!!!");
    }
    /*public ArrayList<event> getEvent(){
        ArrayList<event> eventlist =new ArrayList<event>();
        Cursor c =  db.query(TBL_NAME_event, null, null, null, null, null, null);
        while (c.moveToNext()) {
            int idtmp = c.getInt(0);
            String titletmp = c.getString(1);
            String contenttmp = c.getString(2);
            String modetmp = c.getString(3);
            // String PictureSrctmp = c.getString(4);
            String remindertmp = c.getString(5);
            // String self_commitmenttmp = c.getString(6);
            String Usernametmp = c.getString(7);
            String datetmp = c.getString(8);
            event event1 = new event(titletmp, contenttmp, modetmp,  remindertmp,  Usernametmp, datetmp);
            eventlist.add( event1);
        }
        return eventlist;
    }*/
    public Cursor getEvent(){
        Cursor c =  db.query(TBL_NAME_event, null, null, null, null, null, null);
        return c;
    }
    /*public List<Entity> findAll() {// 查询所有
        List<Entity> data = null;
        open();
        Cursor cursor = db.query(TBL_NAME_event, null, null, null, null, null, null);
        if (cursor.getColumnCount() > 0) {// 当有数据时
            data = new ArrayList<Entity>();
            while (cursor.moveToNext()) {
                Entity entity = new Entity();
                entity.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                entity.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                entity.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                entity.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                data.add(entity);
            }
        }
        db.close();
        Log.i("DBManager", "findAll查询所有");
        return data;
    }*/

   /* public int delete(String name) {// 根据title删除
        int deleteCount = db.delete(TBL_NAME, "name=?", new String[] { name + "" });// 1.表名2.根据条件参数3.条件值
        return deleteCount;
    }*/
    public int delete( String name) {// 根据title删除
        open();
        int deleteCount = db.delete(TBL_NAME_event,  "name=?", new String[] { name + "" });// 1.表名2.根据条件参数3.条件值
        db.close();
        Log.i("DBManager", "delete删除了" + deleteCount);
        close();
        return deleteCount;

    }
    public int delete( int id) {// 根据id删除
        open();
        Log.v("DBManager","id:"+id);
        int deleteCount = db.delete(TBL_NAME_event,  "id=?", new String[] { id + "" });// 1.表名2.根据条件参数3.条件值
        close();
        //Log.i("DBManager", "delete删除了" + deleteCount);
        return deleteCount;
    }





}