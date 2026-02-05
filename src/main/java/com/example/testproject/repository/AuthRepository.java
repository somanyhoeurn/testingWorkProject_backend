package com.example.testproject.repository;

import com.example.testproject.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(@NotBlank(message = "username is required") String username);

    boolean existsByUsername(@NotBlank(message = "Username is required") String username);
}
