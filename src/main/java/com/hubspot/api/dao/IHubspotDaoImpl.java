package com.hubspot.api.dao;

import com.hubspot.api.model.Event;
import com.hubspot.api.model.EventWrapper;
import com.hubspot.api.model.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;


@Repository
public class IHubspotDaoImpl implements IHubspotDao {
    
    @Value("${hubspot.api.get.events.url}")
    private String eventsUrl;

    @Value("${hubspot.api.post.sessions.url}")
    private String sessionsUrl;

    @SuppressWarnings("null")
    @Override
    public List<Event> getEventsDao() {
        RestTemplate restTemplate = new RestTemplate();
        EventWrapper result = restTemplate.getForObject(eventsUrl, EventWrapper.class);
        //return generateTestData();
        return result.getEvents();
    }

    @Override
    public String postSessionDoa(HashMap<String, List<Session>> visitorSessionMap) {
        String response;
        try {

            String sessionsByUser = "sessionsByUser";
            HashMap<String , HashMap<String, List<Session>>> sessionMap = new HashMap<>();
            sessionMap.put(sessionsByUser, visitorSessionMap);
            HttpEntity<HashMap<String , HashMap<String, List<Session>>>> request = new HttpEntity<>(sessionMap);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> result = restTemplate.postForEntity(sessionsUrl, request,
                    String.class);
            response = result.getStatusCode().toString();

        } catch (HttpClientErrorException ex) {
            System.out.println("Exception status code: " + ex.getStatusCode());
            System.out.println("Exception response body: " + ex.getResponseBodyAsString());
            System.out.println("Exception during send invitations post request: " + ex.getMessage());
            response = ex.getResponseBodyAsString();
        }
        return response;
    }

}
