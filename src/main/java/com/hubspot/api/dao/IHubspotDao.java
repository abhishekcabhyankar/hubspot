package com.hubspot.api.dao;

import com.hubspot.api.model.Employee;
import com.hubspot.api.model.EmployeeResponse;
import com.hubspot.api.model.Event;
import com.hubspot.api.model.Invitation;
import com.hubspot.api.model.Partner;
import com.hubspot.api.model.Session;

import java.util.HashMap;
import java.util.List;

public interface IHubspotDao {

    List<Partner> getPartnersAvailability();

    List<Event> getEventsDao();

    List<Employee> getAllEmployees();

    String sendInvitations(List<Invitation> invitationList);

    String postSessionDoa(HashMap<String, List<Session>> visitorSessionMap);

    String createEmployee(EmployeeResponse employee);
}
