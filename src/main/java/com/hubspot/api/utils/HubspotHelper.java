package com.hubspot.api.utils;

import com.hubspot.api.model.Event;
import com.hubspot.api.model.Session;

import java.util.*;

public class HubspotHelper {

    /*
     * We take a list of events, and create a map with
     * Visitor as key and all events atteneded by the visitor as value
     */
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

    /*
     * Create individual session object
     * if there is only one event in session,
     * the the prevTime and EventStartTime will be the same
     * hence it will automatically resolve to zero
     */
    public Session createSession(long eventStartTime, long prevTimeStamp, List<String> pageList) {
        Session session = new Session();
        session.setStartTime(eventStartTime);
        long eventDuration = prevTimeStamp - eventStartTime;
        session.setDuration(eventDuration);
        session.setPages(pageList);
        return session;
    }

    /**
     * Once we have Visitor -> List of Events attended map
     * we need to perform the following process
     * 1. Start processing each visitor one by one
     * Ordering of the visitors do not matter as noted in the requirement
     * 2. Sort the events based on Start time of the event
     * This is done because we need events attended sorted chronologically as
     * mentioned in requirement
     * 3. Initalize the startTime and prevTime with firstEvent
     * 4. In case there is only one event, then a session is created, added to the
     * sessionList
     * 5. In case there are multiple events, we compare next event, starttime with
     * prevEventStart time
     * if its less then 10 mins we continue to add to the page list
     * If it is more, we "flush" the page list into a new session
     * and start a new pageList from the current event
     * 6. When the events are completed, we create a new session of the pageList
     * 7. Return Visitor -> List<Session> map
     */
    public HashMap<String, List<Session>> createVisitorSessionMap(HashMap<String, List<Event>> visitorEventListMap) {
        CustomComparator cc = new CustomComparator();
        HashMap<String, List<Session>> visitorSessionMap = new HashMap<>();

        for (String visitorKey : visitorEventListMap.keySet()) {

            // each visitor will have its own session List
            List<Session> sessionList = new ArrayList<>();

            // List of events the visitor attended, get it sorted by startTime of the event
            List<Event> visitorEvents = visitorEventListMap.get(visitorKey);
            Collections.sort(visitorEvents, cc);

            Event firstEvent = visitorEvents.get(0);

            // initialize event start, prevtime and pageList with the first event
            List<String> pageList = new ArrayList<>();
            pageList.add(firstEvent.getUrl());
            long eventStartTime = firstEvent.getTimestamp();
            long prevTimeStamp = firstEvent.getTimestamp();

            for (int i = 1; i < visitorEvents.size(); i++) {
                Event currEvent = visitorEvents.get(i);
                long currStartTime = currEvent.getTimestamp();
                long duration = currStartTime - prevTimeStamp;
                if (duration <= HubspotConstants.TEN_MINS) {
                    prevTimeStamp = currStartTime;
                    pageList.add(currEvent.getUrl());
                    continue;
                }

                // this means the current event will start its own session
                // as it does not fall in the range of 10 mins
                Session newSession = createSession(eventStartTime, prevTimeStamp, pageList);
                sessionList.add(newSession);

                // reset event start, prevtime and pageList with the current event
                pageList = new ArrayList<>();
                pageList.add(currEvent.getUrl());
                eventStartTime = currStartTime;
                prevTimeStamp = currStartTime;
            }

            Session newSession = createSession(eventStartTime, prevTimeStamp, pageList);
            sessionList.add(newSession);

            // create visitor -> sessionList entry for this visitor
            visitorSessionMap.put(visitorKey, sessionList);
        }
        
        return visitorSessionMap;
    }
}
