package com.versionone.tm.timemanager.tools;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeIntervalUtils {

    public static String getTimeInterval(int selectDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// HH:mm:ss
        Date CurrentDate = new Date(System.currentTimeMillis());
        int currentDate=Integer.parseInt(simpleDateFormat.format(CurrentDate));
        int diff = currentDate - selectDate;
        if(diff>=0){
            int year=diff/10000;
            int month=diff%10000/100;
            int day = diff%10000%100;
            return year+" years "+month+" months "+day+" days has passed.";
        }
        diff=0-diff;
        int year=diff/10000;
        int month=diff%10000/100;
        int day = diff%10000%100;
        return year+" years "+month+" months "+day+" days are left.";
    }


}
