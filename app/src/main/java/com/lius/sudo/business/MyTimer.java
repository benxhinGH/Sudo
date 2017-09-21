package com.lius.sudo.business;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by UsielLau on 2017/9/21 0021 11:27.
 */

public class MyTimer {

    private volatile int time;
    private int second;
    private int minute;
    private int hour;

    private OnTimeChangedListener onTimeChangedListener;

    private Timer timer=new Timer();
    private TimerTask task=new TimerTask() {
        @Override
        public void run() {
            time++;
            if(onTimeChangedListener!=null){
                onTimeChangedListener.onChanged();
            }
        }
    };

    public MyTimer(){

    }

    public MyTimer(int time){
        this.time=time;
    }

    public void start(){
        timer.schedule(task,0,1000);
    }

    public void stop(){
        task.cancel();
    }

    public String getTime(){
        String strTime="";
        second=time%60;
        minute=(time-second)/60;
        if(minute<60){
            strTime=doubleDigitToString(minute)+":"+doubleDigitToString(second);
        }else {
            int temp=minute;
            minute=temp%60;
            hour=(temp-minute)/60;
            strTime=hour+":"+doubleDigitToString(minute)+":"+doubleDigitToString(second);
        }
        return strTime;
    }

    public int getTimeSeconds(){
        return time;
    }

    private String doubleDigitToString(int n){
        String temp=String.valueOf(n);
        if(temp.length()==1){
            temp="0"+temp;
        }
        return temp;
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.onTimeChangedListener = onTimeChangedListener;
    }

    public interface OnTimeChangedListener{
        void onChanged();
    }

}
