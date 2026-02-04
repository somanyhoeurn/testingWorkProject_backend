package com.example.testproject.dto.response;

import lombok.*;
import org.hibernate.annotations.SecondaryRow;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse<T> {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private T user;

}
