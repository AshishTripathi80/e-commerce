package com.customerservice.services;
import com.customerservice.enums.Constants;
import com.customerservice.exceptions.CustomerNotFoundException;
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
                .orElseThrow(() -> new CustomerNotFoundException(Constants.ERROR_CUSTOMER_NOT_FOUND.getMessage() + id));
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
     * Check if a customer with the same email already exists.
     * If a customer with the same email exists, throw an exception.
     *
     * @param customer The customer to check.
     */
    public void isCustomerAlreadyPresent(Customer customer) {
        String email = customer.getEmail();
        Customer existingCustomer = customerRepository.findByEmail(email);

        if (existingCustomer != null) {
            throw new UserAlreadyExistsException(Constants.ERROR_USER_ALREADY_EXISTS.getMessage() + email);
        }
    }


    /**
     * Create a new customer after performing necessary validations and checks.
     *
     * @param customer      The customer to be created.
     * @param bindingResult The BindingResult object containing validation errors.
     * @return The created customer.
     */
    public Customer getCreatedCustomer(Customer customer, BindingResult bindingResult) {
        // Check for validation errors
        validationError(bindingResult);

        // Check if user is already present with email
        isCustomerAlreadyPresent(customer);

        // Encrypt the password
        String encryptedPassword = encryptPassword(customer.getPassword());

        // Set the encrypted password in the Customer object
        customer.setPassword(encryptedPassword);

        return createCustomer(customer);
    }


    /**
     * Update an existing customer after performing necessary validations and checks.
     *
     * @param id              The ID of the customer to update.
     * @param updatedCustomer The updated customer data.
     * @param bindingResult   The BindingResult object containing validation errors.
     * @return The updated customer.
     */
    public Customer updateCustomer(Long id, Customer updatedCustomer, BindingResult bindingResult) {
        // Check for validation errors
        validationError(bindingResult);

        Customer existingCustomer = getCustomerById(id);

        if (existingCustomer == null) {
            throw new CustomerNotFoundException(Constants.ERROR_CUSTOMER_NOT_FOUND.getMessage() + id);
        }

        // Copy the updated fields to the existing customer
        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setLastName(updatedCustomer.getLastName());

        // Encrypt the password
        String encryptedPassword = encryptPassword(updatedCustomer.getPassword());

        // Set the encrypted password in the Customer object
        existingCustomer.setPassword(encryptedPassword);
        return existingCustomer;
    }
}
