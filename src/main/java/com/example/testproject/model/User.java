package com.example.testproject.model;

import aQute.bnd.annotation.baseline.BaselineIgnore;
import com.example.testproject.dto.response.RegisterResponse;
import com.example.testproject.dto.response.UserResponse;
import com.example.testproject.properties.CustomerStatus;
import com.example.testproject.properties.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<Roles> roles = new ArrayList<>();

    @Column(name = "failed_attempts")
    private int failedAttempts;

    @Column(name = "lock_until")
    private Instant lockUntil;

    @Column(nullable = false)
    @Builder.Default
    private CustomerStatus status = CustomerStatus.ACTIVE;

    public UserResponse toResponse(){
        return UserResponse.builder()
                .id(id)
                .username(getUsername())
                .roles(getRoles())
                .build();
    }

    public RegisterResponse toRegisterResponse(){
        return RegisterResponse.builder()
                .id(id)
                .username(getUsername())
                .roles(getRoles())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public boolean isLocked() {
        return lockUntil != null && lockUntil.isAfter(Instant.now());
    }


}
