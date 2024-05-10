package com.hubspot.api.utils;

import com.hubspot.api.model.Event;
import com.hubspot.api.model.Session;

import java.util.*;

public class HubspotHelper {

    public HashMap<String, List<Event>> createVisitorEventMapHelper(List<Event> eventList) {
        HashMap<String, List<Event>> visitorEventMap = new HashMap<>();
        for (Event singleEvent : eventList) {
            String visitor = singleEvent.getVisitorId();
            List<Event> visitorEventList = visitorEventMap.getOrDefault(visitor, new ArrayList<>());
            visitorEventList.add(singleEvent);
            visitorEventMap.put(visitor, visitorEventList);
        }
        return visitorEventMap;
    }

    public void createSession(long eventStartTime, long prevTimeStamp, List<String> pageList, List<Session> sessionList) {
        Session session = new Session();
        session.setStartTime(eventStartTime);
        long eventDuration = prevTimeStamp - eventStartTime;
        session.setDuration(eventDuration);
        session.setPages(pageList);
        sessionList.add(session);
    }

    public HashMap<String, List<Session>> createVisitorSessionMap(HashMap<String, List<Event>> visitorEventListMap){
        CustomComparator cc = new CustomComparator();
        HashMap<String, List<Session>> visitorSessionMap = new HashMap<>();

        for (String visitorKey : visitorEventListMap.keySet()) {
            List<Event> visitorEvents = visitorEventListMap.get(visitorKey);
            Collections.sort(visitorEvents, cc);
            List<Session> sessionList = new ArrayList<>();
            List<String> pageList = new ArrayList<>();
            
            Event firstEvent = visitorEvents.get(0);
            pageList.add(firstEvent.getUrl());
            long eventStartTime = firstEvent.getTimestamp();
            long prevTimeStamp = firstEvent.getTimestamp();

            for (int i = 1; i < visitorEvents.size(); i++) {
                Event curEvent = visitorEvents.get(i);
                long currStartTime = curEvent.getTimestamp();
                long duration = currStartTime - prevTimeStamp;
                if (duration <= HubspotConstants.TEN_MINS) {
                    prevTimeStamp = currStartTime;
                    pageList.add(curEvent.getUrl());
                    continue;
                }
                createSession(eventStartTime, prevTimeStamp, pageList, sessionList);
                
                pageList = new ArrayList<>();
                pageList.add(curEvent.getUrl());
                eventStartTime = currStartTime;
                prevTimeStamp = currStartTime;
            }

            createSession(eventStartTime, prevTimeStamp, pageList, sessionList);
            visitorSessionMap.put(visitorKey, sessionList);
        }
        return visitorSessionMap;
    }
}
