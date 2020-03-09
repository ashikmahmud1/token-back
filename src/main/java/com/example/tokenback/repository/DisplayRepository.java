package com.example.tokenback.repository;

import com.example.tokenback.models.Display;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisplayRepository extends JpaRepository<Display, Long> {
    Display findByNumber(Integer number);
}
