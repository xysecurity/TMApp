package com.versionone.tm.timemanager.Entity;


import java.io.Serializable;

public class event implements Serializable {


    private int id;
    private String title;
    private String content;
    private String mode;
    private String PictureSrc;
    private String reminder;
    private String self_commitment;
    private String Username;
    private String date;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }
    public String getMode() {
        return mode;
    }
    public String getPictureSrc() {
        return PictureSrc;
    }
    public String getReminder() {
        return reminder;
    }
    public String getSelf_commitment() {
        return self_commitment;
    }
    public String getUsername() {
        return Username;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public void setPictureSrc(String pictureSrc) {
        PictureSrc = pictureSrc;
    }
    public void setReminder(String reminder) {
        this.reminder = reminder;
    }
    public void setSelf_commitment(String self_commitment) {
        this.self_commitment = self_commitment;
    }
    public void setUsername(String username) {
        Username = username;
    }

    /* public event(int id,String title, String content,String mode,String PictureSrc,String reminder,String self_commitment,String Username,String date){
         this.id = id;
         this.content=content;
         this.mode=mode;
         this.PictureSrc=PictureSrc;
         this.reminder=reminder;
         this.self_commitment=self_commitment;
         this.Username=Username;
         this.date=date;
     }*/
    public event(int id,String title, String content,String mode,String reminder,String Username,String date){
        this.id=id;
        this.title=title;
        this.content=content;
        this.mode=mode;
        this.reminder=reminder;
        this.Username=Username;
        this.date=date;
    }
    public event(String title, String content,String mode,String reminder,String Username,String date){
        this.title=title;
        this.content=content;
        this.mode=mode;
        this.reminder=reminder;
        this.Username=Username;
        this.date=date;
    }

}

