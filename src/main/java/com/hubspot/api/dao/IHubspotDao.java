package com.hubspot.api.dao;

import com.hubspot.api.model.Employee;
import com.hubspot.api.model.EmployeeResponse;
import com.hubspot.api.model.Invitation;
import com.hubspot.api.model.Partner;

import java.util.List;

public interface IHubspotDao {

    List<Partner> getPartnersAvailability();

    List<Employee> getAllEmployees();

    String sendInvitations(List<Invitation> invitationList);

    String createEmployee(EmployeeResponse employee);
}
