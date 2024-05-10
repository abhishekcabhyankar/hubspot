package com.hubspot.api.dao;

import com.hubspot.api.model.Event;
import com.hubspot.api.model.Session;

import java.util.HashMap;
import java.util.List;

public interface IHubspotDao {


    List<Event> getEventsDao();
    String postSessionDoa(HashMap<String, List<Session>> visitorSessionMap);
}
