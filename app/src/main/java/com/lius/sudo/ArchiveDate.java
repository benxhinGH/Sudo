package com.lius.sudo;

/**
 * Created by lius on 16-6-2.
 */
public class ArchiveDate {
    private String level;
    private String archTime;
    public ArchiveDate(){

    }
    public ArchiveDate(String time,String level){
        this.level=level;
        this.archTime=time;
    }
    public void setLevel(String level){
        this.level=level;
    }
    public String getLevel(){
        return level;
    }
    public void setArchTime(String archTime){
        this.archTime=archTime;
    }
    public String getArchTime(){
        return archTime;
    }
    public String getPrefName(){
        return archTime+level;
    }
}
