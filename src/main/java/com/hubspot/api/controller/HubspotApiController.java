package com.hubspot.api.controller;

import com.hubspot.api.model.Employee;
import com.hubspot.api.model.EmployeeResponse;
import com.hubspot.api.model.Event;
import com.hubspot.api.model.Invitation;
import com.hubspot.api.model.Partner;
import com.hubspot.api.model.Session;
import com.hubspot.api.service.IHubspotApiService;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/hubspot/api/")
public class HubspotApiController {

    @Autowired
    private IHubspotApiService hubspotApiService;

    private List<Partner> partnersList;

    private List<Event> eventList;

    private List<Invitation> invitationsList;

    @ApiOperation(value = "method to get list of partners with availability, transform the data and send post request for invitations.")
    @GetMapping("/" + "partners")
    @ResponseStatus(HttpStatus.OK)
    public String getPartnersAndSendInvitations() {
        partnersList = hubspotApiService.getPartnersAvailability();

        if (CollectionUtils.isEmpty(partnersList)) {
            System.out.println("Unable to get partners list information");
            return "Unable to get partners list.";
        }

        invitationsList = hubspotApiService.getInvitationsList(partnersList);
        return hubspotApiService.sendInvitations(invitationsList);
    }

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

        for (String visitor : visitorSessionMap.keySet()) {
            System.out.println(visitor + " is the visitor");
            List<Session> sessions = visitorSessionMap.get(visitor);
            int i = 1;
            for (Session session : sessions) {
                System.out.println("session" + i + " started");
                System.out.println(session.getStartTime() + " " + session.getDuration());
                for (String page : session.getPages()) {
                    System.out.println(" " + page);
                }
                i++;
            }
            System.out.println();
            System.out.println();
        }

        return hubspotApiService.postSessionService(visitorSessionMap);
    }

    @ApiOperation(value = "method to get list of partners with availability, transform the data and send post request for invitations.")
    @GetMapping("/" + "employee")
    @ResponseStatus(HttpStatus.OK)
    public String getEmployeeString() {
        List<Employee> employees = hubspotApiService.getAllEmployee();

        if (CollectionUtils.isEmpty(employees)) {
            System.out.println("Unable to get employee list information");
            return null;
        }

        EmployeeResponse employeeResponse = new EmployeeResponse("Abhishek", "1000", "19");

        return hubspotApiService.createEmployeeService(employeeResponse);
    }

    public class CustomComparator implements Comparator<Event> {
        @Override
        public int compare(Event a, Event b) {
            if (a.getTimestamp() == b.getTimestamp()) {
                return 0;
            }
            if (a.getTimestamp() > b.getTimestamp()) {
                return 1;
            }
            return -1;
        }
    }
}
