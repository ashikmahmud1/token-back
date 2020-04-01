package com.example.tokenback.repository;

import com.example.tokenback.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Boolean existsByName(@NotBlank String name);

    Boolean existsByLetter(@NotBlank String letter);
}
