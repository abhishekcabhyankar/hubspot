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


    HubspotHelper hubspotHelper = new HubspotHelper();


    @Override
    public List<Event> getEventService() {
        List<Event> eventList = hubspotDao.getEventsDao();
        return eventList;
    }

    @Override
    public HashMap<String, List<Event>> createVisitorEventsMap(List<Event> eventList) {
        return hubspotHelper.createVisitorEventMapHelper(eventList);
    }

    @Override
    public String postSessionService(HashMap<String, List<Session>> visitorSessionMap) {
        return hubspotDao.postSessionDoa(visitorSessionMap);
    }

    @Override
    public HashMap<String, List<Session>> createVisitorSessionMap(HashMap<String, List<Event>> visitorEventListMap) {
        return hubspotHelper.createVisitorSessionMap(visitorEventListMap);
    }

}
