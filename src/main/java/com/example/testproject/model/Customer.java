package com.example.testproject.model;

import com.example.testproject.dto.response.CustomerResponse;
import com.example.testproject.properties.CustomerStatus;
import com.example.testproject.properties.CustomerType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "customers")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CustomerType type;

    private String email;

    private String phone;

    @Column(nullable = false,columnDefinition = "varchar(20)")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private CustomerStatus status = CustomerStatus.ACTIVE;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public CustomerResponse toResponse(){
        return CustomerResponse.builder()
                .id(getId())
                .name(getName())
                .type(getType())
                .email(getEmail())
                .phone(getPhone())
                .status(getStatus())
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }

}
