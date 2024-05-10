package com.hubspot.api.controller;

import com.hubspot.api.model.Event;
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
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/hubspot/api/")
public class HubspotApiController {

    @Autowired
    private IHubspotApiService hubspotApiService;

    @ApiOperation(value = "method to get list of events list from hubspot")
    @GetMapping("/" + "events")
    @ResponseStatus(HttpStatus.OK)
    public String getEventsFromHubSpot() {
        
        // call the hubspot API and get the list of events
        List<Event> eventList = hubspotApiService.getEventService();
        if (CollectionUtils.isEmpty(eventList)) {
            System.out.println("Unable to get events");
            return "Unable to get events list.";
        }
        
        // create Visitor -> List<Events> map
        HashMap<String, List<Event>> visitorEventListMap = hubspotApiService.createVisitorEventsMap(eventList);
        
        // create Visitor -> List<Session> map
        HashMap<String, List<Session>> visitorSessionMap = hubspotApiService
                .createVisitorSessionMap(visitorEventListMap);
                
        String response = hubspotApiService.postSessionService(visitorSessionMap);
        return response;
    }
}
