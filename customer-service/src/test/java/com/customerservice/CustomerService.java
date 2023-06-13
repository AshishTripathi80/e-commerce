package com.customerservice;

import com.customerservice.models.Customer;
import com.customerservice.repo.CustomerRepository;
import com.customerservice.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() {
        // Arrange
        Customer customer = new Customer(null,"test@gmail.com","test123","test123","test1123");
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer createdCustomer = customerService.createCustomer(customer);

        // Assert
        assertNotNull(createdCustomer);
        assertEquals(customer, createdCustomer);
        assertEquals(customer.getFirstName(),createdCustomer.getFirstName());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void getCustomerById_ExistingId_ShouldReturnCustomer() {
        // Arrange
        Long id = 1L;
        Customer customer = new Customer(id,"test@gmail.com","test123","test123","test1123");
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Act
        Customer retrievedCustomer = customerService.getCustomerById(id);

        // Assert
        assertNotNull(retrievedCustomer);
        assertEquals(customer, retrievedCustomer);
        assertEquals(customer.getFirstName(),retrievedCustomer.getFirstName());
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void getCustomerById_NonExistingId_ShouldReturnNull() {
        // Arrange
        Long id = 1L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Customer retrievedCustomer = customerService.getCustomerById(id);

        // Assert
        assertNull(retrievedCustomer);
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer() {
        // Arrange
        Customer customer = new Customer();

        // Act
        customerService.deleteCustomer(customer);

        // Assert
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void getCustomerByEmail_ExistingEmail_ShouldReturnCustomer() {
        // Arrange
        String email = "test@example.com";
        Customer customer = new Customer();
        when(customerRepository.findByEmail(email)).thenReturn(customer);

        // Act
        Customer retrievedCustomer = customerService.getCustomerByEmail(email);

        // Assert
        assertNotNull(retrievedCustomer);
        assertEquals(customer, retrievedCustomer);
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void getCustomerByEmail_NonExistingEmail_ShouldReturnNull() {
        // Arrange
        String email = "test@example.com";
        when(customerRepository.findByEmail(email)).thenReturn(null);

        // Act
        Customer retrievedCustomer = customerService.getCustomerByEmail(email);

        // Assert
        assertNull(retrievedCustomer);
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void getAllCustomers_ShouldReturnListOfCustomers() {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());
        when(customerRepository.findAll()).thenReturn(customers);

        // Act
        List<Customer> retrievedCustomers = customerService.getAllCustomers();

        // Assert
        assertNotNull(retrievedCustomers);
        assertEquals(customers, retrievedCustomers);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer() {
        // Arrange
        Customer customer = new Customer();
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer updatedCustomer = customerService.updateCustomer(customer);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals(customer, updatedCustomer);
        verify(customerRepository, times(1)).save(customer);
    }
}
