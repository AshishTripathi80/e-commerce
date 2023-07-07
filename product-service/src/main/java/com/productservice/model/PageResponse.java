package com.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;        // The list of products
    private int pageNo;             // Current page number
    private int pageSize;           // Number of items per page
    private int totalPages;         // Total number of pages
    private long totalItems;        // Total number of items in the database
    // ... Getters and setters
}

