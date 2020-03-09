package com.example.tokenback.repository;

import com.example.tokenback.models.Display;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisplayRepository extends JpaRepository<Display, Long> {
    Optional<Display> findByNumber(Integer number);
}
