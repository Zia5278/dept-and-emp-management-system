package com.example.department_._employee_management_system.repository;

import com.example.department_._employee_management_system.entity.SalaryAdjustmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface SalaryAdjustmentLogRepository extends JpaRepository<SalaryAdjustmentLog, Long> {
    Optional<SalaryAdjustmentLog> findFirstByDepartmentIdAndPerformanceScoreAndAdjustedAtAfter(Long departmentId, Integer performanceScore, LocalDateTime after);

    Optional<SalaryAdjustmentLog> findFirstByDepartmentIdAndAdjustedAtAfter(Long departmentId, LocalDateTime after);
}
