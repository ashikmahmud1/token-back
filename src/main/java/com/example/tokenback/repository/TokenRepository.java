package com.example.tokenback.repository;

import com.example.tokenback.models.*;
import com.example.tokenback.schema.DepartmentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    // Avoid null parameter
    // :fromDate is null or a.createdAt >= :fromDate
    // If fromDate is null then the above condition is true. so the condition will not have
    // any effect to the entire query
    @Query("select count (a.id) as total_served, a.department.id as department_id, a.department.name as department_name from Token a where " +
            "(:status is null or a.status = :status)" +
            "and (:fromDate is null or a.createdAt >= :fromDate)" +
            "and (:toDate is null or a.createdAt <= :toDate) group by a.department")
    List<DepartmentReport> findDepartmentReport(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("status") ETokenStatus status);

    List<Token> findByCreatedAtAndStatusOrStatus(Date date, ETokenStatus status, ETokenStatus status2);

    List<Token> findByStatus(ETokenStatus status);

    List<Token> findByCounter(Counter counter);

    List<Token> findByDepartment(Department department);

    List<Token> findByUser(User user);

    List<Token> findByCustomer(Customer customer);
}
