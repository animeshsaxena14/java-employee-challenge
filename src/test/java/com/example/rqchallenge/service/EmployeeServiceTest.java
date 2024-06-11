package com.example.rqchallenge.service;

import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.model.EmployeeResponse;
import com.example.rqchallenge.model.EmployeesResponse;
import com.example.rqchallenge.utils.EmployeeServiceConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class EmployeeServiceTest {

    private List<Employee> employeeList = new ArrayList<>();

    EmployeeServiceConstants employeeServiceConstants = new EmployeeServiceConstants();

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService = new EmployeeService();

    private final String baseServiceURL = "http://dummy.restapiexample.com/api/v1";

    @BeforeEach
    public void setup() {
        employeeList.add(Employee.builder().id(1).employee_name("Tiger Nixon").employee_age(61).employee_salary(320800).build());
        employeeList.add(Employee.builder().id(2).employee_name("Garrett Winters").employee_age(48).employee_salary(122000).build());
        employeeList.add(Employee.builder().id(3).employee_name("Ashton Cox").employee_age(32).employee_salary(123000).build());
        employeeList.add(Employee.builder().id(4).employee_name("Airi Satou").employee_age(34).employee_salary(239000).build());
        employeeList.add(Employee.builder().id(5).employee_name("Brielle Williamson").employee_age(29).employee_salary(222000).build());
        employeeList.add(Employee.builder().id(6).employee_name("Herrod Chandler").employee_age(21).employee_salary(112000).build());
        employeeList.add(Employee.builder().id(7).employee_name("Rhona Davidson").employee_age(59).employee_salary(320000).build());
        employeeList.add(Employee.builder().id(8).employee_name("Colleen Hurst").employee_age(68).employee_salary(65000).build());
        employeeList.add(Employee.builder().id(9).employee_name("Sonya Frost").employee_age(42).employee_salary(80000).build());
        employeeList.add(Employee.builder().id(10).employee_name("Jena Gaines").employee_age(45).employee_salary(90000).build());
        employeeList.add(Employee.builder().id(11).employee_name("Quinn Flynn").employee_age(49).employee_salary(100000).build());
        employeeList.add(Employee.builder().id(12).employee_name("Haley Kennedy").employee_age(23).employee_salary(120000).build());
        employeeList.add(Employee.builder().id(13).employee_name("Tatyana Fitzpatrick").employee_age(27).employee_salary(240000).build());
        employeeList.add(Employee.builder().id(14).employee_name("Michael Silva").employee_age(60).employee_salary(250000).build());
        employeeList.add(Employee.builder().id(15).employee_name("Paul Byrd").employee_age(52).employee_salary(310000).build());

        employeeService.baseServiceURL = baseServiceURL;
        employeeService.employeeServiceConstants = this.employeeServiceConstants;
    }

    private void getAllEmployees() throws URISyntaxException {
        EmployeesResponse employeeList = new EmployeesResponse("", this.employeeList);
        when(restTemplate.exchange(
                new URI(this.baseServiceURL + employeeServiceConstants.GET_EMPLOYEES),
                HttpMethod.GET,
                null,
                EmployeesResponse.class))
                .thenReturn(new ResponseEntity(employeeList, HttpStatus.OK));
    }

    @Test
    public void testGetAllEmployees() throws URISyntaxException, IOException {
        getAllEmployees();
        List<Employee> allEmployeesList = employeeService.getAllEmployees();
        assertEquals(allEmployeesList.size(), employeeList.size());
        assertEquals(allEmployeesList, employeeList);
    }

    @Test
    public void testGetEmployeesByNameSearch() throws URISyntaxException {
        getAllEmployees();
        List<Employee> searchedEmployeeList = employeeService.getEmployeesByNameSearch("rr");
        assertEquals(searchedEmployeeList.get(0).getEmployee_name(), "Garrett Winters");
        assertEquals(searchedEmployeeList.get(1).getEmployee_name(), "Herrod Chandler");
    }

    @Test
    public void testGetEmployeeById() {
        EmployeesResponse employeesResponse = getEmployeeByID();
        Employee employee = employeeService.getEmployeeById("123");
        assertEquals(employeesResponse.getData().get(0), employee);
    }

    private EmployeesResponse getEmployeeByID() {
        String id = "123";
        EmployeesResponse employeesResponse = new EmployeesResponse();
        Employee employee = Employee.builder().id(123).employee_age(59).employee_name("Garett").employee_salary(21333).build();
        employeesResponse.setData(Arrays.asList(employee));

        when(restTemplate.exchange(
                this.baseServiceURL + employeeServiceConstants.GET_EMPLOYEE_BY_ID,
                HttpMethod.GET,
                null,
                EmployeesResponse.class,
                id))
                .thenReturn(new ResponseEntity(employeesResponse, HttpStatus.OK));
        return employeesResponse;
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws URISyntaxException {
        getAllEmployees();
        Integer highestSalaryOfEmployee = employeeService.getHighestSalaryOfEmployees();
        assertEquals(highestSalaryOfEmployee, Integer.valueOf(320800));
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws URISyntaxException {
        getAllEmployees();
        List<String> topTenHighestEarningEmployeeNames = employeeService.getTopTenHighestEarningEmployeeNames();
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Tiger Nixon");
        expectedNames.add("Rhona Davidson");
        expectedNames.add("Paul Byrd");
        expectedNames.add("Michael Silva");
        expectedNames.add("Tatyana Fitzpatrick");
        expectedNames.add("Airi Satou");
        expectedNames.add("Brielle Williamson");
        expectedNames.add("Ashton Cox");
        expectedNames.add("Garrett Winters");
        expectedNames.add("Haley Kennedy");
        assertEquals(topTenHighestEarningEmployeeNames.size(), 10);
        assertEquals(topTenHighestEarningEmployeeNames, expectedNames);
    }

    @Test
    public void testCreateEmployee() {
        Employee employee = Employee.builder()
                .employee_name("Tiger Nixon")
                .employee_salary(123000)
                .employee_age(54)
                .build();

        EmployeeResponse employeeResponse = EmployeeResponse.builder().data(employee).status("Success").build();

        when(restTemplate.exchange(
                this.baseServiceURL + employeeServiceConstants.CREATE_EMPLOYEE_URL,
                HttpMethod.POST,
                new HttpEntity<>(employee),
                EmployeeResponse.class))
                .thenReturn(new ResponseEntity(employeeResponse, HttpStatus.OK));
        Map<String, Object> createEmpInputMap = new HashMap<>();
        createEmpInputMap.put("name", "Tiger Nixon");
        createEmpInputMap.put("salary", 123000);
        createEmpInputMap.put("age", 54);

        Employee createdEmp = employeeService.createEmployee(createEmpInputMap);
        assertEquals(createdEmp, employee);
    }

}
