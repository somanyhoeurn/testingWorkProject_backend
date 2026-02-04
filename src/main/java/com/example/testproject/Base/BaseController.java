package com.example.testproject.Base;

import com.example.testproject.dto.response.ApiResponse;
import com.example.testproject.dto.response.StatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class BaseController {
    //    this is for no payload
    protected ResponseEntity<ApiResponse<Object>> response(String code, String message) {
        return ResponseEntity.ok(ApiResponse.builder()
                .status(new StatusResponse(code,message))
                .build());
    }

    //    this is for http ok mostly just use for get
    protected <T> ResponseEntity<ApiResponse<T>> response(String message, String code, T payload) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .status(new StatusResponse(code,message))
                .data(payload)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    //    this is for create
    protected <T> ResponseEntity<ApiResponse<T>> createResponse(String message, String code, T payload) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .status(new StatusResponse(code,message))
                .data(payload)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
