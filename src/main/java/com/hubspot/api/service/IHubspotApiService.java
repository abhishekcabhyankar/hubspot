package com.hubspot.api.service;

import com.hubspot.api.model.Event;
import com.hubspot.api.model.Session;

import java.util.HashMap;
import java.util.List;

public interface IHubspotApiService {

    List<Event> getEventService();

    String postSessionService(HashMap<String, List<Session>> visitorSessionMap);

    HashMap<String, List<Event>> createVisitorEventsMap(List<Event> eventList);

    HashMap<String, List<Session>> createVisitorSessionMap(HashMap<String, List<Event>> visitorEventListMap);
}
