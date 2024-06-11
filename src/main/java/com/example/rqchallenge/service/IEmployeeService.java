package com.example.rqchallenge.service;

import com.example.rqchallenge.model.Employee;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IEmployeeService {

    List<Employee> getAllEmployees() throws IOException;

    List<Employee> getEmployeesByNameSearch(String searchString);

    Employee getEmployeeById(String id);

    Integer getHighestSalaryOfEmployees();

    List<String> getTopTenHighestEarningEmployeeNames();

    Employee createEmployee(Map<String, Object> employeeInput);

    String deleteEmployeeById(String id);
}
