package com.example.department_._employee_management_system.service;

import com.example.department_._employee_management_system.dto.request.EmployeeRequest;
import com.example.department_._employee_management_system.dto.request.SalaryAdjustmentRequest;
import com.example.department_._employee_management_system.dto.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest req);
    EmployeeResponse getById(Long id);
    EmployeeResponse update(Long id, EmployeeRequest req);
    void delete(Long id);
    void adjustSalary(SalaryAdjustmentRequest req);
    List<EmployeeResponse> getAllByDepartment(Long departmentId);
}
