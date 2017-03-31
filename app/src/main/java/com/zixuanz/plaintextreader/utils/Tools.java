package com.zixuanz.plaintextreader.utils;

import com.zixuanz.plaintextreader.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zixuan Zhao on 3/19/17.
 */

public class Tools {

    private static final DateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");

    public static String transDateToStr(Date date){
        return df.format(date);
    }

    public static Date transStrToDate(String str){
        Date date;
        try{
            date = df.parse(str);
        }catch (ParseException e){
            date = new Date();
        }

        return date;
    }

    public static int setIcon(String path){
        int icon = 0;
        if(path.endsWith(".txt")){
            icon = R.mipmap.ic_launcher;
        }
        return icon;
    }
}
