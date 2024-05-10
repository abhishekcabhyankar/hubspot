package com.hubspot.api.controller;

import com.hubspot.api.model.Event;
import com.hubspot.api.model.Session;
import com.hubspot.api.service.IHubspotApiService;
import com.hubspot.api.utils.CustomComparator;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/hubspot/api/")
public class HubspotApiController {

    @Autowired
    private IHubspotApiService hubspotApiService;

    private List<Event> eventList;


    @ApiOperation(value = "method to get list of events from hubspot")
    @GetMapping("/" + "events")
    @ResponseStatus(HttpStatus.OK)
    public String getEventsFromHubSpot() {
        eventList = hubspotApiService.getEventService();

        if (CollectionUtils.isEmpty(eventList)) {
            System.out.println("Unable to get events");
            return "Unable to get events list.";
        }
        HashMap<String, List<Session>> visitorSessionMap = new HashMap<>();
        HashMap<String, List<Event>> map = hubspotApiService.createVisitorEventsMap(eventList);
        CustomComparator cc = new CustomComparator();
        
        for (String visitorKey : map.keySet()) {
            List<Event> visitorEvents = map.get(visitorKey);
            System.out.println(visitorKey + " is the visitor");
            for (Event event : visitorEvents) {
                System.out.println(event.getUrl());
            }
            System.out.println();
        }
        for (String visitorKey : map.keySet()) {
            List<Event> visitorEvents = map.get(visitorKey);
            Collections.sort(visitorEvents, cc);
            long eventStartTime = 0;
            long eventDuration = 0;
            long prevTimeStamp = 0;
            List<String> pageList;
            List<Session> sessionList = new ArrayList<>();

            if (visitorEvents.size() == 1) {
                Session session = new Session();
                pageList = new ArrayList<>();
                pageList.add(visitorEvents.get(0).getUrl());
                session.setPages(pageList);
                session.setDuration(0);
                session.setStartTime(visitorEvents.get(0).getTimestamp());
                sessionList.add(session);
                visitorSessionMap.put(visitorKey, sessionList);
                continue;
            }

            Event firstEvent = visitorEvents.get(0);
            pageList = new ArrayList<>();
            pageList.add(firstEvent.getUrl());
            eventStartTime = firstEvent.getTimestamp();
            prevTimeStamp = eventStartTime;
            for (int i = 1; i < visitorEvents.size(); i++) {
                Event temp = visitorEvents.get(i);
                long currStartTime = temp.getTimestamp();
                long duration = currStartTime - prevTimeStamp;
                if (duration <= 600000) {
                    prevTimeStamp = currStartTime;
                    pageList.add(temp.getUrl());
                    continue;
                }

                Session session = new Session();
                session.setStartTime(eventStartTime);
                eventDuration = prevTimeStamp - eventStartTime;
                session.setDuration(eventDuration);
                session.setPages(pageList);

                sessionList.add(session);

                pageList = new ArrayList<>();
                pageList.add(temp.getUrl());
                eventStartTime = currStartTime;
                prevTimeStamp = currStartTime;
            }

            // handle the last item
            Session session = new Session();
            session.setStartTime(eventStartTime);
            eventDuration = prevTimeStamp - eventStartTime;
            session.setDuration(eventDuration);
            session.setPages(pageList);
            sessionList.add(session);
            visitorSessionMap.put(visitorKey, sessionList);
        }
        return hubspotApiService.postSessionService(visitorSessionMap);
    }
}
