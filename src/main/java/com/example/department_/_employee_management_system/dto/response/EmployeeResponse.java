package com.example.department_._employee_management_system.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data   // generates getters, setters, toString, equals, hashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String email;
    private BigDecimal salary;
    private LocalDate joiningDate;
    private Long departmentId;
    private String departmentName;
}