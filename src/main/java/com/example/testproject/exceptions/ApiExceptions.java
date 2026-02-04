package com.example.testproject.exceptions;

import com.example.testproject.dto.response.StatusResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ApiExceptions extends RuntimeException {

    private final StatusResponse code;
    private final HttpStatus httpStatus;
    private final Map<String, Object> data;

}
