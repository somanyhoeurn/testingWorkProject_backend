package com.example.testproject.exceptions;
import com.example.testproject.dto.response.ApiResponse;
import com.example.testproject.dto.response.StatusResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StatusResponse> handleCustomerNotFound(NotFoundException ex) {
        StatusResponse errorResponse = new StatusResponse("CUSTOMER_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HandlerMethodValidationException.class})
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(Exception ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        if (ex instanceof MethodArgumentNotValidException mav) {
            mav.getBindingResult().getFieldErrors()
                    .forEach(fe -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));
        } else if (ex instanceof HandlerMethodValidationException hmv) {
            hmv.getAllErrors().forEach(error -> {
                String paramName = "parameter";
                if (error instanceof org.springframework.validation.FieldError fe) {
                    paramName = fe.getField();
                }
                fieldErrors.put(paramName, error.getDefaultMessage());
            });
        }

        StatusResponse status = new StatusResponse("REQUIRED_FIELD_MISSING", "Invalid request data.");

        ApiResponse<Map<String, String>> body = ApiResponse.<Map<String, String>>builder()
                .status(status)
                .data(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleDuplicateEmail(DuplicateEmailException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("email", "Email already exists");

        StatusResponse status = new StatusResponse("DUPLICATE_FIELD", "Validation failed");

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .status(status)
                .data(errors)
                .build();

        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String code = "INVALID_REQUEST_BODY";
        String message = "Request body is missing, malformed, or contains invalid values. Please provide valid JSON.";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            // Check if the target type is an Enum
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                // Build the field path
                String fieldPath = ife.getPath().stream()
                        .map(ref -> ref.getFieldName())
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining("."));
                if (fieldPath.isEmpty()) fieldPath = "unknown field";

                // Invalid value
                String invalidValue = ife.getValue() != null ? ife.getValue().toString() : "null";

                // Allowed enum values
                String allowed = Arrays.stream(ife.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                code = "INVALID_ENUM_VALUE";
                message = String.format(
                        "Invalid value '%s' for field '%s'. Allowed values: %s",
                        invalidValue, fieldPath, allowed
                );
            }
        }

        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(new StatusResponse(code, message))
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }


    @ExceptionHandler(CustomerHasActiveException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomerHasActive(CustomerHasActiveException ex) {

        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(new StatusResponse(
                        "CUSTOMER_HAS_ACTIVE",
                        "Customer cannot be delete."
                ))
                .data(null)
                .build();

        return ResponseEntity.status(409).body(body);
    }
    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredential(InvalidCredentialException ex) {

        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(new StatusResponse(
                        "INVALID_CREDENTIAL",
                        "Invalid username or password."
                ))
                .data(null)
                .build();

        return ResponseEntity.status(401).body(body);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccountLocked(AccountLockedException ex) {

        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(new StatusResponse(
                        "ACCOUNT_LOCKED",
                        "Account is locked due to multiple fails."
                ))
                .data(null)
                .build();

        return ResponseEntity.status(403).body(body);
    }

//    @ExceptionHandler(ApiExceptions.class)
//    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiExceptions ex) {
//
//        ApiResponse<Void> body = ApiResponse.<Void>builder()
//                .status(new StatusResponse(ex.getCode().toString(), ex.getMessage()))
//                .data(null)
//                .build();
//
//        return ResponseEntity.status(ex.getHttpStatus()).body(body);
//    }


    @ExceptionHandler(ApiExceptions.class)
    public ResponseEntity<ApiResponse<Object>> handleApiExceptions(ApiExceptions ex) {

        StatusResponse sr = ex.getCode();

        ApiResponse<Object> body = ApiResponse.<Object>builder()
                .data(null)
                .status(StatusResponse.builder()
                        .code(sr.getCode())
                        .message(sr.getMessage())
                        .build())
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }



}
