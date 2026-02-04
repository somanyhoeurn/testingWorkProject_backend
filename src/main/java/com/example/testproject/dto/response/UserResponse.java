package com.example.testproject.dto.response;

import com.example.testproject.properties.CustomerStatus;
import com.example.testproject.properties.Roles;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private List<Roles> roles;
    private CustomerStatus status;

}
