package com.example.rqchallenge.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Employee {

    private Integer id;

    private String employee_name;

    private Integer employee_salary;

    private Integer employee_age;

    private String profile_image;

}
