package com.example.tokenback.repository;

import com.example.tokenback.models.Customer;
import com.example.tokenback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<User> findByName(String username);
}
