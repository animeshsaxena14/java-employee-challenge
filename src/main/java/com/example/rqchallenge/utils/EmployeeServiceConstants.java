package com.example.rqchallenge.utils;

import org.springframework.stereotype.Component;

@Component
public class EmployeeServiceConstants {

    public final String GET_EMPLOYEES = "/employees";
    public final String GET_EMPLOYEE_BY_ID = GET_EMPLOYEES + "/{id}";
    public final String CREATE_EMPLOYEE_URL = "/create";
    public final String DELETE_EMPLOYEE_URL = "/delete/{id}";

}
