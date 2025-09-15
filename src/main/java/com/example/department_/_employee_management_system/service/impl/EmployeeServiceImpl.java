package com.example.department_._employee_management_system.service.impl;

import com.example.department_._employee_management_system.dto.request.EmployeeRequest;
import com.example.department_._employee_management_system.dto.request.SalaryAdjustmentRequest;
import com.example.department_._employee_management_system.dto.response.EmployeeResponse;
import com.example.department_._employee_management_system.entity.Department;
import com.example.department_._employee_management_system.entity.Employee;
import com.example.department_._employee_management_system.entity.SalaryAdjustmentLog;
import com.example.department_._employee_management_system.exception.BadRequestException;
import com.example.department_._employee_management_system.exception.ResourceNotFoundException;
import com.example.department_._employee_management_system.repository.DepartmentRepository;
import com.example.department_._employee_management_system.repository.EmployeeRepository;
import com.example.department_._employee_management_system.repository.SalaryAdjustmentLogRepository;
import com.example.department_._employee_management_system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository empRepo;
    private final DepartmentRepository deptRepo;
    private final SalaryAdjustmentLogRepository logRepo;

    @Value("${app.salary.cap:200000}")
    private BigDecimal salaryCap;

    public EmployeeServiceImpl(EmployeeRepository empRepo, DepartmentRepository deptRepo, SalaryAdjustmentLogRepository logRepo) {
        this.empRepo = empRepo;
        this.deptRepo = deptRepo;
        this.logRepo = logRepo;
    }

    @Override
    public EmployeeResponse create(EmployeeRequest req) {
        if (empRepo.findByEmail(req.getEmail()).isPresent()) throw new BadRequestException("Email already used");
        Department d = deptRepo.findById(req.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Employee e = Employee.builder()
                .name(req.getName())
                .email(req.getEmail())
                .salary(req.getSalary())
                .joiningDate(req.getJoiningDate())
                .department(d)
                .build();
        Employee saved = empRepo.save(e);
        return map(saved);
    }

    @Override
    public EmployeeResponse getById(Long id) {
        Employee e = empRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return map(e);
    }

    @Override
    public EmployeeResponse update(Long id, EmployeeRequest req) {
        Employee e = empRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        if (!e.getEmail().equals(req.getEmail()) && empRepo.findByEmail(req.getEmail()).isPresent()) throw new BadRequestException("Email already used");
        Department d = deptRepo.findById(req.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        e.setName(req.getName());
        e.setEmail(req.getEmail());
        e.setSalary(req.getSalary());
        e.setJoiningDate(req.getJoiningDate());
        e.setDepartment(d);
        Employee saved = empRepo.save(e);
        return map(saved);
    }

    @Override
    public void delete(Long id) {
        if (!empRepo.existsById(id)) throw new ResourceNotFoundException("Employee not found");
        empRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void adjustSalary(SalaryAdjustmentRequest req) {
        Long deptId = req.getDepartmentId();
        Integer score = req.getPerformanceScore();
        // idempotency: if a log exists for this department within last 30 minutes -> reject
        LocalDateTime windowStart = LocalDateTime.now().minusMinutes(30);
        var recent = logRepo.findFirstByDepartmentIdAndAdjustedAtAfter(deptId, windowStart);
        if (recent.isPresent()) {
            throw new BadRequestException("Salary adjustment for this department was performed within last 30 minutes");
        }

        Department d = deptRepo.findById(deptId).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        List<Employee> employees = empRepo.findAll().stream()
                .filter(e -> e.getDepartment() != null && e.getDepartment().getId().equals(deptId))
                .collect(Collectors.toList());

        if (employees.isEmpty()) return; // nothing to do

        for (Employee e : employees) {
            BigDecimal original = e.getSalary();
            BigDecimal adjusted = original;

            // performance-based
            if (score >= 90) {
                adjusted = adjusted.add(original.multiply(BigDecimal.valueOf(0.15)));
            } else if (score >= 70) {
                adjusted = adjusted.add(original.multiply(BigDecimal.valueOf(0.10)));
            } else {
                // log warning - here we throw no error but continue
                System.out.println("Warning: performance < 70 for employee id=" + e.getId());
            }

            // tenure bonus >5 years
            LocalDate fiveYearsAgo = LocalDate.now().minusYears(5);
            if (e.getJoiningDate().isBefore(fiveYearsAgo)) {
                adjusted = adjusted.add(original.multiply(BigDecimal.valueOf(0.05)));
            }

            // cap
            if (adjusted.compareTo(salaryCap) > 0) adjusted = salaryCap;

            e.setSalary(adjusted);
            empRepo.save(e);
        }

        SalaryAdjustmentLog log = SalaryAdjustmentLog.builder()
                .departmentId(deptId)
                .performanceScore(score)
                .adjustedAt(LocalDateTime.now())
                .build();
        logRepo.save(log);
    }

    @Override
    public List<EmployeeResponse> getAllByDepartment(Long departmentId) {
        return empRepo.findAll().stream()
                .filter(e -> e.getDepartment() != null && e.getDepartment().getId().equals(departmentId))
                .map(this::map)
                .collect(Collectors.toList());
    }

    private EmployeeResponse map(Employee e) {
        var dr = e.getDepartment();
        Long deptId = dr != null ? dr.getId() : null;
        String deptName = dr != null ? dr.getName() : null;
        return new EmployeeResponse(e.getId(), e.getName(), e.getEmail(), e.getSalary(), e.getJoiningDate(), deptId, deptName);
    }
}