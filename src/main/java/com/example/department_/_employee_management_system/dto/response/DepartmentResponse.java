package com.example.department_._employee_management_system.dto.response;

import lombok.*;

@Data   // generates getters, setters, toString, equals, hashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String name;
    private String code;
}