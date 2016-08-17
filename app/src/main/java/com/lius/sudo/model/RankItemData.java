package com.lius.sudo.model;

/**
 * Created by 刘有泽 on 2016/8/17.
 */
public class RankItemData {
    private int rank;
    private String playerName;
    private String consumeTime;
    public RankItemData(int rank,String playerName,String consumeTime){
        this.rank=rank;
        this.playerName=playerName;
        this.consumeTime=consumeTime;
    }
    public int getRank(){return rank;}
    public String getPlayerName(){return playerName;}
    public String getConsumeTime(){return consumeTime;}
}
