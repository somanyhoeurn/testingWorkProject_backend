package com.example.testproject.service.serviceImp;

import com.example.testproject.dto.request.CustomerPartialUpdateRequest;
import com.example.testproject.dto.request.CustomerRequest;
import com.example.testproject.dto.request.CustomerUpdateRequest;
import com.example.testproject.dto.response.CustomerResponse;
import com.example.testproject.dto.response.PaginationResponse;
import com.example.testproject.dto.response.StatusResponse;
import com.example.testproject.exceptions.ApiExceptions;
import com.example.testproject.exceptions.CustomerHasActiveException;
import com.example.testproject.exceptions.DuplicateEmailException;
import com.example.testproject.exceptions.NotFoundException;
import com.example.testproject.model.Customer;
import com.example.testproject.properties.CustomerStatus;
import com.example.testproject.properties.CustomerType;
import com.example.testproject.repository.CustomerRepository;
import com.example.testproject.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        if(customerRepository.existsByEmail(customerRequest.getEmail())){
            throw new DuplicateEmailException("A customer with this email already exist.");
        }
        Customer customer = customerRequest.toEntity();
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer.toResponse();
    }

    @Override
    public PaginationResponse<CustomerResponse> getAllCustomer(String search, CustomerType type, CustomerStatus status, int page, int size, Sort.Direction customerSort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(customerSort,"createdAt"));

        boolean hasSearch = search != null && !search.isBlank();
        boolean hasType = type != null;
        boolean hasStatus = status != null;

        Page<Customer> customerPage;
        if (hasSearch && hasType && hasStatus) {

            customerPage = customerRepository
                    .findByTypeAndStatusAndNameContainingIgnoreCaseOrTypeAndStatusAndEmailContainingIgnoreCaseOrTypeAndStatusAndPhoneContaining(
                            type, status, search,
                            type, status, search,
                            type, status, search,
                            pageable
                    );

        } else if (hasSearch && hasType) {

            customerPage = customerRepository
                    .findByTypeAndNameContainingIgnoreCaseOrTypeAndEmailContainingIgnoreCaseOrTypeAndPhoneContaining(
                            type, search,
                            type, search,
                            type, search,
                            pageable
                    );

        } else if (hasType && hasStatus) {

            customerPage = customerRepository.findByTypeAndStatus(type, status, pageable);

        } else if (hasSearch) {

            customerPage = customerRepository
                    .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(
                            search, search, search, pageable
                    );

        } else if (hasType) {

            customerPage = customerRepository.findByType(type, pageable);

        } else if (hasStatus) {

            customerPage = customerRepository.findByStatus(status, pageable);

        } else {

            customerPage = customerRepository.findAll(pageable);
        }


        Page<CustomerResponse> customerResponses = customerPage.map(Customer::toResponse);

        return new PaginationResponse<>(
                customerResponses
        );
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new NotFoundException("Customer not found"));
        return customer.toResponse();
    }

    @Override
    public CustomerResponse updateCustomerById(Long customerId, CustomerUpdateRequest customerRequest) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new NotFoundException("Customer not found"));
        customer.setName(customerRequest.getName());
        customer.setType(customerRequest.getType());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhone(customerRequest.getPhone());
        customer.setStatus(customerRequest.getStatus());
        return customerRepository.save(customer).toResponse();
    }

    @Override
    public CustomerResponse partialUpdateCustomerById(Long customerId, CustomerPartialUpdateRequest customerRequest) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new NotFoundException("Customer not found"));
        customer.setPhone(customerRequest.getPhone());
        customer.setStatus(customerRequest.getStatus());
        return customerRepository.save(customer).toResponse();
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new NotFoundException("Customer not found"));
        if (customer.getStatus() == CustomerStatus.ACTIVE){
            throw new CustomerHasActiveException("Customer cannot be delete.");
        }
        customerRepository.deleteById(customerId);
    }

}
