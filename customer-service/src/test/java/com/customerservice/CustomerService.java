package com.customerservice;

import com.customerservice.exceptions.InvalidCustomerDataException;
import com.customerservice.exceptions.UserAlreadyExistsException;
import com.customerservice.models.Customer;
import com.customerservice.repo.CustomerRepository;
import com.customerservice.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

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
        Customer customer = new Customer(null, "test@gmail.com", "test123", "test123", "test1123");
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer createdCustomer = customerService.createCustomer(customer);

        // Assert
        assertNotNull(createdCustomer);
        assertEquals(customer, createdCustomer);
        assertEquals(customer.getFirstName(), createdCustomer.getFirstName());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void getCustomerById_ExistingId_ShouldReturnCustomer() {
        // Arrange
        Long id = 1L;
        Customer customer = new Customer(id, "test@gmail.com", "test123", "test123", "test1123");
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Act
        Customer retrievedCustomer = customerService.getCustomerById(id);

        // Assert
        assertNotNull(retrievedCustomer);
        assertEquals(customer, retrievedCustomer);
        assertEquals(customer.getFirstName(), retrievedCustomer.getFirstName());
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
        Customer customer = new Customer(null, email, "test1233", "test1233", "test1233");
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
        customers.add(new Customer(1L, "test1@gmail.com", "test01", "test01", "test01"));
        customers.add(new Customer(2L, "test2@gmail.com", "test02", "test02", "test02"));
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
        Customer updatedCustomer = customerService.createCustomer(customer);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals(customer, updatedCustomer);
        verify(customerRepository, times(1)).save(customer);
    }


    @Test
    void validationError_WithErrors_ShouldThrowException() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act and Assert
        assertThrows(InvalidCustomerDataException.class, () -> customerService.validationError(bindingResult));
    }


    @Test
    void encryptPassword_ShouldReturnEncryptedPassword() {
        // Arrange
        String password = "password123";
        String encryptedPassword = "$2a$10$pCMaeeheQlm7QVuclWaQq.WTv8xrYILIP8wTozEntSjEVuUOO0wNm";

        // Act
        String result = customerService.encryptPassword(password);

        // Assert
        assertNotNull(result);
        assertNotEquals(password, result);
        //assertEquals(encryptedPassword, result);
        assertTrue(result.matches("\\$2a\\$\\d{2}\\$.+")); // Check if the result matches the BCrypt pattern
    }


    @Test
    void isCustomerAlreadyPresent_CustomerExists_ShouldThrowUserAlreadyExistsException() {
        // Arrange
        String email = "existing@example.com";
        Customer existingCustomer = new Customer();
        existingCustomer.setEmail(email);

        Customer newCustomer = new Customer();
        newCustomer.setEmail(email);

        when(customerRepository.findByEmail(email)).thenReturn(existingCustomer);

        // Act and Assert
        assertThrows(UserAlreadyExistsException.class, () -> {
            customerService.isCustomerAlreadyPresent(newCustomer);
        });

        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void isCustomerAlreadyPresent_CustomerDoesNotExist_ShouldNotThrowException() {
        // Arrange
        String email = "new@example.com";
        Customer newCustomer = new Customer();
        newCustomer.setEmail(email);

        when(customerRepository.findByEmail(email)).thenReturn(null);

        // Act and Assert
        assertDoesNotThrow(() -> {
            customerService.isCustomerAlreadyPresent(newCustomer);
        });

        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void convertToResponseDTO_ValidCustomer_ShouldReturnCorrectResponseDTO() {
        // Arrange
        Long customerId = 1L;
        String email = "john.doe@example.com";
        String firstName = "John";
        String lastName = "Doe";
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setEmail(email);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        CustomerResponseDTO expectedDTO = new CustomerResponseDTO(customerId, email, firstName, lastName);

        // Act
        CustomerResponseDTO resultDTO = customerService.convertToResponseDTO(customer);

        // Assert
        assertEquals(expectedDTO.getId(), resultDTO.getId());
        assertEquals(expectedDTO.getEmail(), resultDTO.getEmail());
        assertEquals(expectedDTO.getFirstName(), resultDTO.getFirstName());
        assertEquals(expectedDTO.getLastName(), resultDTO.getLastName());
    }


    @Test
    void convertToResponseDTO_EmptyList_ShouldReturnEmptyDTOList() {
        // Arrange
        List<Customer> customers = new ArrayList<>();

        // Act
        List<CustomerResponseDTO> resultDTOList = customerService.convertToResponseDTO(customers);

        // Assert
        assertEquals(0, resultDTOList.size());
    }

    @Test
    void convertToResponseDTO_NonEmptyList_ShouldReturnCorrectDTOList() {
        // Arrange
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setEmail("john.doe@example.com");
        customer1.setFirstName("John");
        customer1.setLastName("Doe");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setEmail("jane.doe@example.com");
        customer2.setFirstName("Jane");
        customer2.setLastName("Doe");

        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);

        List<CustomerResponseDTO> expectedDTOList = new ArrayList<>();
        expectedDTOList.add(new CustomerResponseDTO(1L, "john.doe@example.com", "John", "Doe"));
        expectedDTOList.add(new CustomerResponseDTO(2L, "jane.doe@example.com", "Jane", "Doe"));

        // Act
        List<CustomerResponseDTO> resultDTOList = customerService.convertToResponseDTO(customers);

        // Assert
        assertEquals(expectedDTOList.size(), resultDTOList.size());
        for (int i = 0; i < expectedDTOList.size(); i++) {
            CustomerResponseDTO expectedDTO = expectedDTOList.get(i);
            CustomerResponseDTO resultDTO = resultDTOList.get(i);
            assertEquals(expectedDTO.getId(), resultDTO.getId());
            assertEquals(expectedDTO.getEmail(), resultDTO.getEmail());
            assertEquals(expectedDTO.getFirstName(), resultDTO.getFirstName());
            assertEquals(expectedDTO.getLastName(), resultDTO.getLastName());
        }
    }


}
