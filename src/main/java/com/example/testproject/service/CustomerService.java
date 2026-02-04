package com.example.testproject.service;

import com.example.testproject.dto.request.CustomerPartialUpdateRequest;
import com.example.testproject.dto.request.CustomerRequest;
import com.example.testproject.dto.request.CustomerUpdateRequest;
import com.example.testproject.dto.response.CustomerResponse;
import com.example.testproject.dto.response.PaginationResponse;
import com.example.testproject.properties.CustomerStatus;
import com.example.testproject.properties.CustomerType;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest customerRequest);

    PaginationResponse<CustomerResponse> getAllCustomer(String search, CustomerType type, CustomerStatus status, int page, int size, Sort.Direction customerSort);

    CustomerResponse getCustomerById(Long customerId);

    CustomerResponse updateCustomerById(Long customerId, CustomerUpdateRequest customerRequest);

    CustomerResponse partialUpdateCustomerById(Long customerId, CustomerPartialUpdateRequest customerRequest);

    void deleteCustomerById(Long customerId);
}
