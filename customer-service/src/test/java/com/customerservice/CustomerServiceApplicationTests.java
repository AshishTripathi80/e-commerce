package com.customerservice;

import com.customerservice.exceptions.UserAlreadyExistsException;
import com.customerservice.models.Customer;
import com.customerservice.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomerServiceApplicationTests {

    @Autowired
    private CustomerService customerService;


    @Test
    void getCustomerByEmail_shouldReturnCustomer() {

        String email = "ankur@gmail.com";
        Customer existingCustomer = customerService.getCustomerByEmail(email);

        if (existingCustomer == null) {
            Customer newCustomer = new Customer(null, email, "tiwari", "ankur", "tiwari");
            // Save the new customer in the customerService
            customerService.createCustomer(newCustomer);
        }
        Customer customer1=customerService.getCustomerByEmail(email);
        assertEquals("ankur", customer1.getFirstName());
    }





}
