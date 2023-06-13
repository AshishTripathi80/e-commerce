package com.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {

    private Long id;

    @Email(message = "Please provide a valid email address")
    private String email;

    @Size(min = 2, max = 10, message = "First name must be between 2 and 10 characters")
    private String firstName;

    @Size(min = 2, max = 10, message = "Last name must be between 2 and 10 characters")
    private String lastName;

}
