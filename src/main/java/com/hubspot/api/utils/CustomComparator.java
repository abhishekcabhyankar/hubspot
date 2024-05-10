package com.hubspot.api.utils;
import java.util.Comparator;
import com.hubspot.api.model.Event;

public class CustomComparator implements Comparator<Event> {
    @Override
    public int compare(Event a, Event b) {
        if (a.getTimestamp() == b.getTimestamp()) {
            return 0;
        }
        if (a.getTimestamp() > b.getTimestamp()) {
            return 1;
        }
        return -1;
    }
}