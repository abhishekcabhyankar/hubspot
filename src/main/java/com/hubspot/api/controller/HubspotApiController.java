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
    CustomComparator cc = new CustomComparator();
    HashMap<String, List<Session>> visitorSessionMap;
    HashMap<String, List<Event>> visitorEventListMap;
    List<String> pageList;

    @ApiOperation(value = "method to get list of events from hubspot")
    @GetMapping("/" + "events")
    @ResponseStatus(HttpStatus.OK)
    public String getEventsFromHubSpot() {

        eventList = hubspotApiService.getEventService();
        visitorSessionMap = new HashMap<>();

        if (CollectionUtils.isEmpty(eventList)) {
            System.out.println("Unable to get events");
            return "Unable to get events list.";
        }

     
        visitorEventListMap = hubspotApiService.createVisitorEventsMap(eventList);

        for (String visitorKey : visitorEventListMap.keySet()) {
            List<Event> visitorEvents = visitorEventListMap.get(visitorKey);
            Collections.sort(visitorEvents, cc);
            List<Session> sessionList = new ArrayList<>();
            Event firstEvent = visitorEvents.get(0);

            pageList = new ArrayList<>();
            pageList.add(firstEvent.getUrl());

            long eventStartTime = firstEvent.getTimestamp();
            long prevTimeStamp = firstEvent.getTimestamp();

            for (int i = 1; i < visitorEvents.size(); i++) {
                Event temp = visitorEvents.get(i);
                long currStartTime = temp.getTimestamp();
                long duration = currStartTime - prevTimeStamp;
                if (duration <= 600000) {
                    prevTimeStamp = currStartTime;
                    pageList.add(temp.getUrl());
                    continue;
                }
                Session newSession = hubspotApiService.createSession(eventStartTime, prevTimeStamp, pageList);
                sessionList.add(newSession);
                pageList = new ArrayList<>();
                pageList.add(temp.getUrl());
                eventStartTime = currStartTime;
                prevTimeStamp = currStartTime;
            }

            // handle the last item
            Session newSession = hubspotApiService.createSession(eventStartTime, prevTimeStamp, pageList);
            sessionList.add(newSession);
            visitorSessionMap.put(visitorKey, sessionList);
        }
        return hubspotApiService.postSessionService(visitorSessionMap);
    }
}
