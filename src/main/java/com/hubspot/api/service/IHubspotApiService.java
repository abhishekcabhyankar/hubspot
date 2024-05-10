package com.hubspot.api.service;

import com.hubspot.api.model.Event;
import com.hubspot.api.model.Session;

import java.util.HashMap;
import java.util.List;

public interface IHubspotApiService {


    List<Event> getEventService();

    String postSessionService(HashMap<String, List<Session>> visitorSessionMap);

    HashMap<String, List<Event>> createVisitorEventsMap(List<Event> eventList);

    Session createSession(long eventStartTime, long prevTimeStamp, List<String> pageList);
}
