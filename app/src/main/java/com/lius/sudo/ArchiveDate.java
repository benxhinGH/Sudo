package com.lius.sudo;

/**
 * Created by lius on 16-6-2.
 */
public class ArchiveDate {
    private int id;
    private String playerName;
    private String level;
    private String archiveTime;
    private String consumeStrTime;
    private int consumeIntTime;
    private String number;
    private String color;

    public ArchiveDate(int id,String playerName,String level,String archiveTime,String consumeStrTime,
                       int consumeIntTime,String number,String color){
        this.id=id;
        this.playerName=playerName;
        this.level=level;
        this.archiveTime=archiveTime;
        this.consumeStrTime=consumeStrTime;
        this.consumeIntTime=consumeIntTime;
        this.number=number;
        this.color=color;
    }
    public int getId(){return id;}
    public String getPlayerName(){return playerName;}
    public String getLevel(){
        return level;
    }
    public String getArchiveTime(){return archiveTime;}
    public String getConsumeStrTime(){return consumeStrTime;}
    public int getConsumeIntTime(){return consumeIntTime;}
    public String getNumber(){
        return number;
    }
    public String getColor(){
        return color;
    }


}
