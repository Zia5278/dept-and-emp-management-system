package com.example.department_._employee_management_system.controller;

import com.example.department_._employee_management_system.dto.request.EmployeeRequest;
import com.example.department_._employee_management_system.dto.request.SalaryAdjustmentRequest;
import com.example.department_._employee_management_system.dto.response.EmployeeResponse;
import com.example.department_._employee_management_system.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/adjust-salary")
    public ResponseEntity<?> adjustSalary(@Valid @RequestBody SalaryAdjustmentRequest req) {
        service.adjustSalary(req);
        return ResponseEntity.ok().body("Salary adjustment completed");
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<List<EmployeeResponse>> getByDepartment(@PathVariable Long deptId) {
        return ResponseEntity.ok(service.getAllByDepartment(deptId));
    }
}
