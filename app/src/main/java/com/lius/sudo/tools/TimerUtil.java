package com.lius.sudo.tools;

/**
 * Created by 刘有泽 on 2016/8/16.
 * 主要提供将用秒表示的格式化时间的方法
 */
public class TimerUtil {
    public static String translateToStrTime(int currentTime){
        int hour;
        int minute;
        int second;
        String strTime;
        hour=currentTime/3600;
        minute=(currentTime-hour*3600)/60;
        second=currentTime-hour*3600-minute*60;

        if(hour<10) strTime="0"+hour+":";
        else strTime=hour+":";

        if(minute<10)strTime=strTime+"0"+minute+":";
        else strTime=strTime+minute+":";

        if(second<10)strTime=strTime+"0"+second;
        else strTime=strTime+second;

        return strTime;
    }
}
