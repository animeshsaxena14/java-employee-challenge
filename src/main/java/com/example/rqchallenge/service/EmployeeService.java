package com.example.rqchallenge.service;

import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.model.EmployeeResponse;
import com.example.rqchallenge.model.EmployeesResponse;
import com.example.rqchallenge.utils.EmployeeServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@PropertySource("classpath:application.properties")
public class EmployeeService implements IEmployeeService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public EmployeeServiceConstants employeeServiceConstants;

    @Value("${employee.service.base.url}")
    public String baseServiceURL;

    @Override
    public List<Employee> getAllEmployees() throws IOException {
        ResponseEntity<EmployeesResponse> employeeResponseEntity = null;
        try {
            employeeResponseEntity = restTemplate.exchange(new URI(baseServiceURL + employeeServiceConstants.GET_EMPLOYEES),
                    HttpMethod.GET, null, EmployeesResponse.class);
        } catch (URISyntaxException e) {
            log.error("Error while fetching employee list: ", e.getMessage());
            e.printStackTrace();
        }
        if (employeeResponseEntity.getStatusCode().is2xxSuccessful()){
            log.debug("getAllEmployee successful response: {}", employeeResponseEntity.getBody().getData());
            return employeeResponseEntity.getBody().getData();
        }

        log.error("Error while fetching employee list, status code is: ", employeeResponseEntity.getStatusCode());
        return new ArrayList<>();
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = null;

        try {
            employees = getAllEmployees();
        } catch (IOException e) {
            log.error("Error fetching all employees: ", e.getMessage());
        }
        return employees.stream()
                .filter(employee -> employee.getEmployee_name().contains(searchString))
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeById(String id) {
        ResponseEntity<EmployeesResponse> employeeResponseEntity = restTemplate.exchange(
                baseServiceURL + employeeServiceConstants.GET_EMPLOYEE_BY_ID,
                HttpMethod.GET,
                null,
                EmployeesResponse.class,
                id);
        if (employeeResponseEntity.getStatusCode().is2xxSuccessful()) {
            log.debug("getEmployeeById response: {} ", employeeResponseEntity.getBody().getData());
            return employeeResponseEntity.getBody().getData().get(0);

        }
        log.debug("No employee found for given ID : {}", id);
        return null;
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        List<Employee> employees = new ArrayList<>();

        try {
            employees = getAllEmployees();
        } catch (IOException e) {
            log.error("Error fetching all employees: ", e.getMessage());
        }
        if (employees.size() == 0) {
            log.debug("No Employees found");
            return -1;
        }
        return employees.stream().max(Comparator.comparing(Employee::getEmployee_salary)).get().getEmployee_salary();
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = new ArrayList<>();

        try {
            employees = getAllEmployees();
        } catch (IOException e) {
            log.error("Error fetching all employees: ", e.getMessage());
        }
        if (employees.size() == 0) {
            log.debug("No Employees found");
            return null;
        }
        return employees.stream().sorted(Comparator.comparing(Employee::getEmployee_salary).reversed())
                .limit(10)
                .map(Employee::getEmployee_name)
                .collect(Collectors.toList());
    }

    @Override
    public Employee createEmployee(Map<String, Object> employeeInput) {
        Employee employee = Employee.builder()
                .employee_name(String.valueOf(employeeInput.get("name")))
                .employee_salary(Integer.valueOf(String.valueOf(employeeInput.get("salary"))))
                .employee_age(Integer.valueOf(String.valueOf(employeeInput.get("age"))))
                .build();

        ResponseEntity<EmployeeResponse> employeeResponseResponseEntity = restTemplate.exchange(
                baseServiceURL + employeeServiceConstants.CREATE_EMPLOYEE_URL,
                HttpMethod.POST,
                new HttpEntity<>(employee),
                EmployeeResponse.class);

        if (employeeResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("Response of Request :{} ", employeeResponseResponseEntity.getBody().getData());
            return employeeResponseResponseEntity.getBody().getData();
        }
        log.error("Employee creation failed with status code: {}", employeeResponseResponseEntity.getStatusCode());
        return null;
    }

    @Override
    public String deleteEmployeeById(String id) {
        ResponseEntity<String> employeeResponseResponseEntity = restTemplate.exchange(
                baseServiceURL + employeeServiceConstants.DELETE_EMPLOYEE_URL,
                HttpMethod.DELETE,
                null,
                String.class,
                id);
        if (employeeResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
            return "Employee Deleted Successfully";
        }

        return "Employee with ID : " + id + " could not be deleted";
    }
}
