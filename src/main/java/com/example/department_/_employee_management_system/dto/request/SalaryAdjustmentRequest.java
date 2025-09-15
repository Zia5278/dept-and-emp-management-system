package com.example.department_._employee_management_system.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data   // generates getters, setters, toString, equals, hashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaryAdjustmentRequest {
    @NotNull
    private Long departmentId;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer performanceScore;
}
