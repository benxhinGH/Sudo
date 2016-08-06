package com.lius.sudo;

/**
 * Created by 刘有泽 on 2016/8/6.
 */

import android.util.Log;

/**
 * 一个自定义计时器，需要通过外部信号来计时，提供了获取00：00：00格式时间的方法
 */
public class MyTimer {
    private int currentTime=0;
    public void timePlusOne(){
        currentTime++;
    }
    public String getStringTime(){
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
