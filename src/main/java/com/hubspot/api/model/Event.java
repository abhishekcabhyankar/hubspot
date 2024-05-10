package com.hubspot.api.model;

public class Event {

    private String url;

    private String visitorId;

    private long timestamp;

    public Event(String url, String visitorId, long timestamp) {
        this.url = url;
        this.visitorId = visitorId;
        this.timestamp = timestamp;
    }

    public Event() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
