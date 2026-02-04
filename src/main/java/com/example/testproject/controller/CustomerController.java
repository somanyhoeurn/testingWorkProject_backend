package com.example.testproject.controller;

import com.example.testproject.Base.BaseController;
import com.example.testproject.dto.request.CustomerPartialUpdateRequest;
import com.example.testproject.dto.request.CustomerRequest;
import com.example.testproject.dto.request.CustomerUpdateRequest;
import com.example.testproject.dto.response.ApiResponse;
import com.example.testproject.dto.response.CustomerResponse;
import com.example.testproject.dto.response.PaginationResponse;
import com.example.testproject.properties.CustomerStatus;
import com.example.testproject.properties.CustomerType;
import com.example.testproject.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("isAuthenticated()")
public class CustomerController extends BaseController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest customerRequest){
        return createResponse("Customer has been created successfully.", "SUCCESS", customerService.createCustomer(customerRequest));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<CustomerResponse>>> getAllCustomer(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) CustomerType type,
            @RequestParam(required = false) CustomerStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction customerSort

    ){
        return response("Customer List retrieved successfully.","SUCCESS",customerService.getAllCustomer(search,type,status,page,size,customerSort));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@Positive @PathVariable @Valid Long customerId){
        return response("Customer Retrieved successfully","SUCCESS",customerService.getCustomerById(customerId));
    }

    @PutMapping("{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomerById(@Positive @PathVariable Long customerId, @Valid @RequestBody CustomerUpdateRequest customerRequest){
        return response("Customer updated successfully","SUCCESS",customerService.updateCustomerById(customerId, customerRequest));
    }

    @PatchMapping("{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponse>> partialUpdateCustomerById(@Positive @PathVariable Long customerId,@Valid @RequestBody CustomerPartialUpdateRequest customerRequest){
        return response("Customer updated successfully","SUCCESS",customerService.partialUpdateCustomerById(customerId, customerRequest));
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<ApiResponse<Object>> deleteCustomerById(@Positive @PathVariable Long customerId){
        customerService.deleteCustomerById(customerId);
        return response("Customer Deleted successfully.","SUCCESS",null);
    }


}
