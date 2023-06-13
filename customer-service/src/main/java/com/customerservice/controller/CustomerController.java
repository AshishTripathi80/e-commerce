package com.customerservice.controller;

import com.customerservice.dto.CustomerResponseDTO;
import com.customerservice.enums.Constants;
import com.customerservice.exceptions.CustomerNotFoundException;
import com.customerservice.exceptions.UserAlreadyExistsException;
import com.customerservice.models.Customer;
import com.customerservice.services.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    // Create a new customer
    @PostMapping()
    public CustomerResponseDTO createCustomer(@Valid @RequestBody Customer customer, BindingResult bindingResult) {
        logger.info("Received request to create a new customer");

        // Check for validation errors
        customerService.validationError(bindingResult);

        //Check if user is already present with email
        customerService.isCustomerAlreadyPresent(customer);

        // Encrypt the password
        String encryptedPassword = customerService.encryptPassword(customer.getPassword());

        // Set the encrypted password in the Customer object
        customer.setPassword(encryptedPassword);

        Customer createdCustomer = customerService.createCustomer(customer);

        return customerService.convertToResponseDTO(createdCustomer);
    }



    //Get all customer
    @GetMapping()
    public List<Customer> getAllCustomers() {
        logger.info("Received request to get all customers");
        return customerService.getAllCustomers();
    }

    // Get a customer by ID
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        logger.info("Received request to get customer by ID: {}", id);
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            throw new CustomerNotFoundException(Constants.ERROR_CUSTOMER_NOT_FOUND.getMessage() + id);
        }
        return customer;
    }

    // Get a customer by email
    @GetMapping("/email/{email}")
    public Customer getCustomerByEmail(@PathVariable String email) {
        logger.info("Received request to get customer by email: {}", email);
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            throw new CustomerNotFoundException(Constants.ERROR_CUSTOMER_NOT_FOUND.getMessage() + email);
        }
        return customer;
    }

    // Update a customer by ID
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer updatedCustomer, BindingResult bindingResult) {
        logger.info("Received request to update customer with ID: {}", id);

        // Check for validation errors
        customerService.validationError(bindingResult);

        Customer existingCustomer = customerService.getCustomerById(id);

        if (existingCustomer == null) {
            throw new CustomerNotFoundException(Constants.ERROR_CUSTOMER_NOT_FOUND.getMessage() + id);
        }

        // Copy the updated fields to the existing customer
        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setLastName(updatedCustomer.getLastName());

        // Encrypt the password
        String encryptedPassword = customerService.encryptPassword(updatedCustomer.getPassword());

        // Set the encrypted password in the Customer object
        existingCustomer.setPassword(encryptedPassword);


        return customerService.updateCustomer(existingCustomer);
    }

    // Delete a customer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        logger.info("Received request to delete customer by ID: {}", id);
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            customerService.deleteCustomer(customer);
            return ResponseEntity.ok().body("Customer deleted successfully");
        } else {
            throw new CustomerNotFoundException(Constants.ERROR_CUSTOMER_NOT_FOUND.getMessage() + id);
        }
    }
}
