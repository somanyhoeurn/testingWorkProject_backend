package com.example.testproject.dto.response;

import com.example.testproject.properties.Roles;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegisterResponse {

    private Long id;
    private String username;
    private List<Roles> roles;

}
