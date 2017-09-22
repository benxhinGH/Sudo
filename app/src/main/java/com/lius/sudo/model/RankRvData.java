package com.lius.sudo.model;

/**
 * Created by UsielLau on 2017/9/22 0022 14:24.
 */

public class RankRvData {

    private int rank;
    private String playerName;
    private String gameTime;

    public RankRvData(int rank, String playerName, String gameTime) {
        this.rank = rank;
        this.playerName = playerName;
        this.gameTime = gameTime;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }
}
