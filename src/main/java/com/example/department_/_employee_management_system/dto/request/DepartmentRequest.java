package com.example.department_._employee_management_system.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data   // generates getters, setters, toString, equals, hashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String code;
}