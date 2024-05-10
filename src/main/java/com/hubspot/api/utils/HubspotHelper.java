package com.hubspot.api.utils;

import com.hubspot.api.model.Event;
import java.util.*;

public class HubspotHelper {
    public static HashMap<String, List<Event>> map = new HashMap<>();
    public static HashMap<String, List<Event>> createVisitorEventMapHelper(List<Event> eventList) {
        for(Event singleEvent: eventList){
            String visitor = singleEvent.getVisitorId();
            List<Event> visitorEventList = map.getOrDefault(visitor, new ArrayList<>());
            visitorEventList.add(singleEvent);
            map.put(visitor, visitorEventList);
        }
        return map;
    }
}
