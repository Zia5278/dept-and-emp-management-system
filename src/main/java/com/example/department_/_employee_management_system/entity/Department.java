package com.example.department_._employee_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data   // generates getters, setters, toString, equals, hashCode
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;
}