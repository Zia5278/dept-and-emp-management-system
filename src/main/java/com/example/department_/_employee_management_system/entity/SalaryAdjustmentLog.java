package com.example.department_._employee_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data   // generates getters, setters, toString, equals, hashCode
@Table(name = "salary_adjustment_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryAdjustmentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long departmentId;
    private Integer performanceScore;
    private LocalDateTime adjustedAt;
}