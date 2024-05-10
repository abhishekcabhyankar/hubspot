package com.hubspot.api.service;

import com.hubspot.api.model.Employee;
import com.hubspot.api.model.EmployeeResponse;
import com.hubspot.api.model.Invitation;
import com.hubspot.api.model.Partner;

import java.util.List;

public interface IHubspotApiService {

    List<Partner> getPartnersAvailability();

    List<Invitation> getInvitationsList(List<Partner> partnersList);

    String sendInvitations(List<Invitation> invitations);

    List<Employee> getAllEmployee();

    String createEmployeeService(EmployeeResponse employee);
}
