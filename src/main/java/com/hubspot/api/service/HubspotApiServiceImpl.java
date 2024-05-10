package com.hubspot.api.service;

import com.hubspot.api.dao.IHubspotDao;
import com.hubspot.api.model.Event;
import com.hubspot.api.model.Session;
import com.hubspot.api.utils.HubspotHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class HubspotApiServiceImpl implements IHubspotApiService {

    @Autowired
    private IHubspotDao hubspotDao;

    @Override
    public List<Event> getEventService() {
        List<Event> eventList = hubspotDao.getEventsDao();
        return eventList;
    }

    @Override
    public HashMap<String, List<Event>> createVisitorEventsMap(List<Event> eventList) {
        return HubspotHelper.createVisitorEventMapHelper(eventList);
    }

    @Override
    public String postSessionService(HashMap<String, List<Session>> visitorSessionMap) {
        return hubspotDao.postSessionDoa(visitorSessionMap);
    }

    @Override
    public Session createSession(long eventStartTime, long prevTimeStamp, List<String> pageList) {
        return HubspotHelper.createSession(eventStartTime, prevTimeStamp, pageList);
    }

}
