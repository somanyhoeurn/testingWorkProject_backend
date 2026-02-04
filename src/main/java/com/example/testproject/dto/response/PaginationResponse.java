package com.example.testproject.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
//@Builder
public class PaginationResponse<T> {
    private List<T> items;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;

    public PaginationResponse(Page<T> pageData) {
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalItems = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
        this.items = pageData.getContent();
    }
}
