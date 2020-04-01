package com.example.tokenback.repository;

import com.example.tokenback.models.Display;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface DisplayRepository extends JpaRepository<Display, Long> {

    Optional<Display> findByName(String name);

    Boolean existsByName(@NotBlank String name);
}
