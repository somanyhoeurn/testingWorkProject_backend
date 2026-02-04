package com.example.testproject.dto.request;

import com.example.testproject.model.Customer;
import com.example.testproject.properties.CustomerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPartialUpdateRequest {
    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Phone must be a valid number"
    )
    private String phone;

    @NotNull(message = "Status is required")
    private CustomerStatus status;

    public Customer toEntity(){
        return Customer.builder()
                .phone(getPhone())
                .status(getStatus())
                .build();
    }

}
