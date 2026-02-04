package com.example.testproject.repository;

import com.example.testproject.dto.response.CustomerResponse;
import com.example.testproject.dto.response.PaginationResponse;
import com.example.testproject.model.Customer;
import com.example.testproject.properties.CustomerStatus;
import com.example.testproject.properties.CustomerType;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(String name, String email, String phone, Pageable pageable);

    Page<Customer> findByTypeAndStatus(CustomerType type, CustomerStatus status, Pageable pageable);

    Page<Customer> findByTypeAndStatusAndNameContainingIgnoreCaseOrTypeAndStatusAndEmailContainingIgnoreCaseOrTypeAndStatusAndPhoneContaining(CustomerType type, CustomerStatus status, String search, CustomerType type1, CustomerStatus status1, String search1, CustomerType type2, CustomerStatus status2, String search2, Pageable pageable);

    boolean existsByEmail(@Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email must be valid"
    ) String email);

    Page<Customer> findByType(CustomerType type, Pageable pageable);

    Page<Customer> findByStatus(CustomerStatus status, Pageable pageable);

    Page<Customer> findByTypeAndNameContainingIgnoreCaseOrTypeAndEmailContainingIgnoreCaseOrTypeAndPhoneContaining(CustomerType type, String search, CustomerType type1, String search1, CustomerType type2, String search2, Pageable pageable);
}
