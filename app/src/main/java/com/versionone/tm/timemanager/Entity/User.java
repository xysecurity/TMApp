package com.versionone.tm.timemanager.Entity;


public class User {
    private String UserName;
    private String Email;
    private String password;
    public String getUserName(){
        return UserName;
    }
    public String getEmail(){
        return Email;
    }
    public String getPassword(){
        return password;
    }
    public void setUserName(String UserName){
        this.UserName = UserName;
    }
    public void setEmail(String Email){
        this.Email = Email;
    }
    public void setPassword(String Password){
        this.password = Password;
    }
    public User(String Email, String UserName, String password){
        this.UserName = UserName;
        this.Email=Email;
        this.password=password;
    }
}
