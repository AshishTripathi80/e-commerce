package com.orderservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @Email(message = "Please provide a valid email address")
    private String customerEmail;

    @Size(min = 2, max = 20, message = "Address  must be between 2 and 20 characters")
    private String customerAddress;

    private List<ProductList> productList;
}
