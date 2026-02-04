package com.example.testproject.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private StatusResponse status;
    private T data;

}
