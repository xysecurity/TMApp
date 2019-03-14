package com.versionone.tm.timemanager.Controller;

import android.database.Cursor;
import android.util.Log;

import com.versionone.tm.timemanager.DBmgr.DBManager;
import com.versionone.tm.timemanager.Entity.User;

public class UserController {
    public void Registered(DBManager dbMgr, String Email, String UserName, String password){
        User user = new User(Email,UserName,password);
        dbMgr.open();
        dbMgr.save(user);
        dbMgr.close();
    }
    public boolean verifes(String Username,String password, DBManager db){
        db.open();
        Cursor c = db.query(Username);
        String passwordtmp;
        while (c.moveToNext()) {

            passwordtmp=c.getString(c.getColumnIndex("password"));
            if(password.equals(passwordtmp)){
                return true;
            }else {
                return false;
            }
        }
        db.close();
        return false;
    }

    public Boolean ChangePassword(String email,String username,String newpassword,DBManager dbMgr){
        User user;
        Boolean msg=Boolean.TRUE;
        dbMgr.open();
        user=dbMgr.getUser(username);
        if(user.getEmail().equals(email)){

            user.setPassword(newpassword);
            dbMgr.updateUser(user);
            Log.i("***********"," 调用db得update（）方法成功");
            return msg;
        }
        else {
            msg=false;
        }
        dbMgr.close();
        return msg;
    }
}
