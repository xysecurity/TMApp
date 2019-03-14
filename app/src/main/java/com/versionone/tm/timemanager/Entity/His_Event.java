package com.versionone.tm.timemanager.Entity;

import java.io.Serializable;

public class His_Event implements Serializable {
    private String E_year;
    private String E_content;
    private String E_detail;
    private String E_date;

    public His_Event(String year, String content, String detail, String date) {
        this.E_year = year;
        this.E_content = content;
        this.E_detail = detail;
        this.E_date = date;
    }

    public String getYear(){ return E_year;}
    public String getContents(){ return E_content;}
    public String getDetail(){ return E_detail;}
    public String getDate(){ return E_date;}
}
