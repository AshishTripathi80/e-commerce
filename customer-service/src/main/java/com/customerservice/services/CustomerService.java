package com.customerservice.services;

import com.customerservice.dto.CustomerResponseDTO;
import com.customerservice.enums.Constants;
import com.customerservice.exceptions.InvalidCustomerDataException;
import com.customerservice.exceptions.UserAlreadyExistsException;
import com.customerservice.models.Customer;
import com.customerservice.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    /**
     * Create a new customer.
     *
     * @param customer The customer to be created.
     * @return The created customer.
     */
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Get a customer by their ID.
     *
     * @param id The ID of the customer to retrieve.
     * @return The customer if found, or null if not found.
     */
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElse(null);
    }

    /**
     * Delete a customer.
     *
     * @param customer The customer to be deleted.
     */
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
        ResponseEntity.ok().build();
    }

    /**
     * Get a customer by their email address.
     *
     * @param email The email address of the customer to retrieve.
     * @return The customer if found, or null if not found.
     */
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    /**
     * Get all customers.
     *
     * @return A list of all customers.
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Update a customer.
     *
     * @param customer The customer object to update.
     * @return The updated customer.
     */
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Handle validation errors.
     *
     * @param bindingResult The BindingResult object containing validation errors.
     * @throws InvalidCustomerDataException if validation errors are present.
     */
    public void validationError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> errors = new ArrayList<>();

            // Convert each field error to a string representation
            for (FieldError fieldError : fieldErrors) {
                errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            }

            // Throw an exception with the validation errors
            throw new InvalidCustomerDataException("Validation Failed!", LocalDateTime.now(), errors);
        }
    }

    /**
     * Encrypt the password using BCryptPasswordEncoder.
     *
     * @param password The password to encrypt.
     * @return The encrypted password.
     */
    public String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * Convert a Customer object to a CustomerResponseDTO object.
     *
     * @param customer The customer to convert.
     * @return The converted CustomerResponseDTO.
     */
    public com.customerservice.dto.CustomerResponseDTO convertToResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getEmail(),
                customer.getFirstName(),
                customer.getLastName()
        );
    }


    public void isCustomerAlreadyPresent(Customer customer) {
        String email = customer.getEmail();
        Customer existingCustomer = customerRepository.findByEmail(email);

        if (existingCustomer != null) {
            throw new UserAlreadyExistsException(Constants.ERROR_USER_ALREADY_EXISTS.getMessage() + email);
        }
    }

}
