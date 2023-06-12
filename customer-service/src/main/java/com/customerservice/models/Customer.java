package com.customerservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true)
    @Email(message = "Please provide a valid email address")
    private String email;

    private String password;

    @Size(min = 2, max = 10, message = "First name must be between 2 and 10 characters")
    private String firstName;

    @Size(min = 2, max = 10, message = "Last name must be between 2 and 10 characters")
    private String lastName;


}
