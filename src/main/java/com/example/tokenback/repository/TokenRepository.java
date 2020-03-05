package com.example.tokenback.repository;

import com.example.tokenback.models.Token;
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
    @Query("select count (a.id) as total_served from Token a where " +
            "(:fromDate is null or a.createdAt >= :fromDate)  " +
            "and (:toDate is null or a.createdAt <= :toDate)")
    List<Object[]> findDepartmentReport(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}
