package com.hubspot.api.dao;

import com.hubspot.api.model.Employee;
import com.hubspot.api.model.EmployeeResponse;
import com.hubspot.api.model.EmployeeWrapper;
import com.hubspot.api.model.Event;
import com.hubspot.api.model.EventWrapper;
import com.hubspot.api.model.Invitation;
import com.hubspot.api.model.Partner;
import com.hubspot.api.model.PartnerWrapper;
import com.hubspot.api.model.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hubspot.api.utils.HubspotConstants.COUNTRIES;

@Repository
public class IHubspotDaoImpl implements IHubspotDao {

    @Value("${hubspot.api.get.partners.url}")
    private String getPartnersUrl;

    @Value("${hubspot.api.post.invitation.url}")
    private String postInvitationUrl;

    @Value("${hubspot.api.get.dummy.url}")
    private String dummyUrl;

    @Value("${hubspot.api.get.create.url}")
    private String createUrl;

    @Value("${hubspot.api.get.events.url}")
    private String eventsUrl;

    @Value("${hubspot.api.post.sessions.url}")
    private String sessionsUrl;

    

    @SuppressWarnings("null")
    @Override
    public List<Partner> getPartnersAvailability() {
        RestTemplate restTemplate = new RestTemplate();
        PartnerWrapper result = restTemplate.getForObject(getPartnersUrl, PartnerWrapper.class);
        return result.getPartners();
    }


    public List<Event> generateTestData(){
        Event one = new Event("/pages/a-big-river", "d1177368-2310-11e8-9e2a-9b860a0d9039", 1512754583000l);
        Event two = new Event("/pages/a-small-dog", "d1177368-2310-11e8-9e2a-9b860a0d9039", 1512754631000l);
        Event tree = new Event("/pages/a-big-talk", "f877b96c-9969-4abc-bbe2-54b17d030f8b", 1512709065294l);

        Event four = new Event("/pages/a-sad-story", "f877b96c-9969-4abc-bbe2-54b17d030f8b", 1512711000000l);
        Event five = new Event("/pages/a-big-river", "d1177368-2310-11e8-9e2a-9b860a0d9039", 1512754436000l);
        Event six = new Event( "/pages/a-sad-story", "f877b96c-9969-4abc-bbe2-54b17d030f8b", 1512709024000l);
        List<Event> list = new ArrayList<>();
        list.add(one);
        list.add(two);
        list.add(tree);
        list.add(four);
        list.add(five);
        list.add(six);
        return list;
    }

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

    public String sendInvitations(List<Invitation> invitationList) {
        String response;
        try {
            Map<String, List<Invitation>> list = new HashMap<>();
            list.put(COUNTRIES, invitationList);

            HttpEntity<Map<String, List<Invitation>>> request = new HttpEntity<>(list);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Invitation> result = restTemplate.postForEntity(postInvitationUrl, request,
                    Invitation.class);
            response = result.getStatusCode().toString();

        } catch (HttpClientErrorException ex) {
            System.out.println("Exception status code: " + ex.getStatusCode());
            System.out.println("Exception response body: " + ex.getResponseBodyAsString());
            System.out.println("Exception during send invitations post request: " + ex.getMessage());
            response = ex.getResponseBodyAsString();
        }
        return response;
    }

    @SuppressWarnings("null")
    @Override
    public List<Employee> getAllEmployees() {
        RestTemplate restTemplate = new RestTemplate();
        EmployeeWrapper result = restTemplate.getForObject(dummyUrl, EmployeeWrapper.class);
        return result.getData();
    }

    @Override
    public String createEmployee(EmployeeResponse employee) {
        String response;
        try {

            HttpEntity<EmployeeResponse> request = new HttpEntity<>(employee);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> result = restTemplate.postForEntity(createUrl, request,
                    String.class);
            response = result.toString();

        } catch (HttpClientErrorException ex) {
            System.out.println("Exception status code: " + ex.getStatusCode());
            System.out.println("Exception response body: " + ex.getResponseBodyAsString());
            System.out.println("Exception during send invitations post request: " + ex.getMessage());
            response = ex.getResponseBodyAsString();
        }
        return response;
    }


}
