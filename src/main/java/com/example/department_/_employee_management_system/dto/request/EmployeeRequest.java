package com.example.department_._employee_management_system.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data   // generates getters, setters, toString, equals, hashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotNull
    @DecimalMin(value = "0.01", message = "Salary must be > 0")
    private BigDecimal salary;

    @NotNull
    private LocalDate joiningDate;

    @NotNull
    private Long departmentId;
}
