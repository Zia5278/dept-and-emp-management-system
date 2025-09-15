package com.example.department_._employee_management_system.service.impl;

import com.example.department_._employee_management_system.dto.request.DepartmentRequest;
import com.example.department_._employee_management_system.dto.response.DepartmentResponse;
import com.example.department_._employee_management_system.entity.Department;
import com.example.department_._employee_management_system.exception.BadRequestException;
import com.example.department_._employee_management_system.exception.ResourceNotFoundException;
import com.example.department_._employee_management_system.repository.DepartmentRepository;
import com.example.department_._employee_management_system.service.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repo;

    public DepartmentServiceImpl(DepartmentRepository repo) { this.repo = repo; }

    @Override
    public DepartmentResponse create(DepartmentRequest req) {
        if (repo.findByCode(req.getCode()).isPresent()) {
            throw new BadRequestException("Department code already exists");
        }
        Department d = Department.builder().name(req.getName()).code(req.getCode()).build();
        Department saved = repo.save(d);
        return new DepartmentResponse(saved.getId(), saved.getName(), saved.getCode());
    }

    @Override
    public DepartmentResponse getById(Long id) {
        Department d = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        return new DepartmentResponse(d.getId(), d.getName(), d.getCode());
    }

    @Override
    public DepartmentResponse update(Long id, DepartmentRequest req) {
        Department d = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        d.setName(req.getName());
        d.setCode(req.getCode());
        Department saved = repo.save(d);
        return new DepartmentResponse(saved.getId(), saved.getName(), saved.getCode());
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Department not found");
        repo.deleteById(id);
    }
}