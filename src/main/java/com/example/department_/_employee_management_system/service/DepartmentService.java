package com.example.department_._employee_management_system.service;

import com.example.department_._employee_management_system.dto.request.DepartmentRequest;
import com.example.department_._employee_management_system.dto.response.DepartmentResponse;

public interface DepartmentService {
    DepartmentResponse create(DepartmentRequest req);
    DepartmentResponse getById(Long id);
    DepartmentResponse update(Long id, DepartmentRequest req);
    void delete(Long id);
}