package com.example.testproject.dto.response;

import com.example.testproject.properties.CustomerStatus;
import com.example.testproject.properties.CustomerType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerResponse {
    private Long id;
    private String name;
    private CustomerType type;
    private String email;
    private String phone;
    private CustomerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
