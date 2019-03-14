package com.versionone.tm.timemanager.Controller;



import com.versionone.tm.timemanager.Entity.His_Event;

import java.util.ArrayList;

import jxl.*;

public class HisEventController {
    public ArrayList<His_Event> HisEventController(Sheet His_sheet, String date){
        ArrayList<His_Event> HisList = new ArrayList<>();
        int start = 0;
        int rows = His_sheet.getRows();
        String MD = date.substring(0,5);
        while(start < rows){
            String Con = His_sheet.getCell(0,start).getContents();
            if(Con.equals(MD)) {
                String dateA = His_sheet.getCell(0,start).getContents();
                String year = His_sheet.getCell(1,start).getContents();
                String content = His_sheet.getCell(2,start).getContents();
                String detail = His_sheet.getCell(3,start).getContents();
                His_Event his_event = new His_Event(year, content, detail,dateA);
                HisList.add(his_event);
            }
            start = start +1;

        }
        return HisList;
    }
}
