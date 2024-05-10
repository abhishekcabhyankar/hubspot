package com.hubspot.api.model;

import java.util.List;

public class Session {

    private long startTime;
    private List<String> pages;
    private long duration;

    public Session() {
    }

    public Session(long startTime, List<String> pages, long duration) {
        this.startTime = startTime;
        this.pages = pages;
        this.duration = duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
