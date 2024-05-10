package com.hubspot.api.service;

import com.hubspot.api.model.Employee;
import com.hubspot.api.model.EmployeeResponse;
import com.hubspot.api.model.Event;
import com.hubspot.api.model.Invitation;
import com.hubspot.api.model.Partner;
import com.hubspot.api.model.Session;

import java.util.HashMap;
import java.util.List;

public interface IHubspotApiService {

    List<Partner> getPartnersAvailability();

    List<Event> getEventService();

    String postSessionService(HashMap<String, List<Session>> visitorSessionMap);


    List<Invitation> getInvitationsList(List<Partner> partnersList);

    HashMap<String, List<Event>> createVisitorEventsMap(List<Event> eventList);


    String sendInvitations(List<Invitation> invitations);

    List<Employee> getAllEmployee();

    String createEmployeeService(EmployeeResponse employee);
}
