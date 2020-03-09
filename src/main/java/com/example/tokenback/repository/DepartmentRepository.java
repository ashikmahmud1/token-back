package com.example.tokenback.repository;

import com.example.tokenback.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    //    List<Department> findByDisplay_Id(Long display_id);
}
