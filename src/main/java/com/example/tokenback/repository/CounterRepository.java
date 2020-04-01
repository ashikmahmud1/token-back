package com.example.tokenback.repository;

import com.example.tokenback.models.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface CounterRepository extends JpaRepository<Counter, Long> {

    Optional<Counter> findByName(String name);

    Boolean existsByName(@NotBlank String name);

    Boolean existsByLetter(@NotBlank String letter);
}
