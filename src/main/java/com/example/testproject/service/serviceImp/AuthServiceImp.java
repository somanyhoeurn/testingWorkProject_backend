package com.example.testproject.service.serviceImp;

import com.example.testproject.dto.request.AuthRequest;
import com.example.testproject.dto.request.RegisterRequest;
import com.example.testproject.dto.response.AuthResponse;
import com.example.testproject.dto.response.RegisterResponse;
import com.example.testproject.dto.response.StatusResponse;
import com.example.testproject.dto.response.UserResponse;
import com.example.testproject.exceptions.ApiExceptions;
import com.example.testproject.model.User;
import com.example.testproject.properties.CustomerStatus;
import com.example.testproject.properties.Roles;
import com.example.testproject.repository.AuthRepository;
import com.example.testproject.security.JwtService;
import com.example.testproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${token.ms}")
    private Long jwtExpiration;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        Set<Roles> uniqueRoles = new HashSet<>(registerRequest.getRoles());

        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            throw new ApiExceptions(
                    new StatusResponse("ROLE_REQUIRED", "At least one role must be provided"),
                    HttpStatus.BAD_REQUEST,null
            );
        }

        if (uniqueRoles.size() != registerRequest.getRoles().size()) {
            throw new ApiExceptions(
                    new StatusResponse("DUPLICATE_ROLE", "Duplicate roles are not allowed"),
                    HttpStatus.BAD_REQUEST,null
            );
        }

        if (authRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ApiExceptions(
                    new StatusResponse("USERNAME_EXISTS", "Username already exists"),
                    HttpStatus.CONFLICT,
                    null
            );
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(registerRequest.getRoles())
                .build();
        User savedUser = authRepository.save(user);
        return savedUser.toRegisterResponse();
    }

    @Transactional(noRollbackFor = ApiExceptions.class)
    @Override
    public AuthResponse<UserResponse> login(AuthRequest authRequest) {

        User user = authRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new ApiExceptions(
                        new StatusResponse("INVALID_CREDENTIALS", "Invalid username or password"),
                        HttpStatus.UNAUTHORIZED, null
                ));

        if (user.getLockUntil() != null && user.getLockUntil().isAfter(Instant.now())) {
            throw new ApiExceptions(
                    new StatusResponse("ACCOUNT_LOCKED", "Account is locked due to multiple failed attempts"),
                    HttpStatus.FORBIDDEN,
                    Map.of("lockedUntil", user.getLockUntil().toString())
            );
        }

        if (user.getStatus() == CustomerStatus.INACTIVE){
            throw new ApiExceptions(
                    new StatusResponse("ACCOUNT_INACTIVE", "Account is inactive. Please contact support."),
                    HttpStatus.FORBIDDEN,null
            );
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            user.setFailedAttempts(0);
            user.setLockUntil(null);
            authRepository.save(user);

            String accessToken = jwtService.generateToken(userDetails);

            return AuthResponse.<UserResponse>builder()
                    .accessToken(accessToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtExpiration)
                    .user(user.toResponse())
                    .build();

        } catch (AuthenticationException ex) {

            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if (attempts >= 5) {
                Instant lockedUntil = Instant.now().plus(Duration.ofMinutes(30));
                user.setLockUntil(lockedUntil);
                authRepository.save(user);

                throw new ApiExceptions(
                        new StatusResponse("ACCOUNT_LOCKED", "Account is locked due to multiple failed attempts"),
                        HttpStatus.FORBIDDEN,
                        Map.of("lockedUntil", lockedUntil.toString())
                );
            }

            authRepository.save(user);

            throw new ApiExceptions(
                    new StatusResponse("INVALID_CREDENTIALS", "Invalid username or password"),
                    HttpStatus.UNAUTHORIZED,
                    null
            );
        }
    }



}
