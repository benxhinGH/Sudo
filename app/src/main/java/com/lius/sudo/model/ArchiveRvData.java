package com.lius.sudo.model;

/**
 * Created by UsielLau on 2017/9/21 0021 20:45.
 */

public class ArchiveRvData {

    private int archiveId;
    private String archiveTime;
    private String level;

    public ArchiveRvData(int archiveId, String archiveTime, String level) {
        this.archiveId = archiveId;
        this.archiveTime = archiveTime;
        this.level = level;
    }

    public int getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(int archiveId) {
        this.archiveId = archiveId;
    }

    public String getArchiveTime() {
        return archiveTime;
    }

    public void setArchiveTime(String archiveTime) {
        this.archiveTime = archiveTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
