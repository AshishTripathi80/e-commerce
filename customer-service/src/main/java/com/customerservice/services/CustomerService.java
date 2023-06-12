package com.customerservice.services;

import com.customerservice.exceptions.InvalidCustomerDataException;
import com.customerservice.models.Customer;
import com.customerservice.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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

    public void validationError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError fieldError : fieldErrors) {
                errorMessage.append(fieldError.getDefaultMessage()).append("; ");
            }
            throw new InvalidCustomerDataException(errorMessage.toString());
        }
    }



    public String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

}
