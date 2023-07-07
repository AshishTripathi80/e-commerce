package com.orderservice;

import com.orderservice.exception.InvalidOrderDataException;
import com.orderservice.exception.OrderNotFoundException;
import com.orderservice.model.Orders;
import com.orderservice.model.OrderDTO;
import com.orderservice.repo.OrderRepository;
import com.orderservice.service.OrderService;
import com.orderservice.service.ProductServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrdersServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductServiceClient productServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveOrder_ShouldReturnSavedOrder() {
        // Arrange
        Orders orders = new Orders();
        when(orderRepository.save(orders)).thenReturn(orders);

        // Act
        Orders savedOrders = orderService.saveOrder(orders);

        // Assert
        assertEquals(orders, savedOrders);
        verify(orderRepository, times(1)).save(orders);
    }

    @Test
    void getOrdersByEmail_ShouldReturnListOfOrders() {
        // Arrange
        String email = "test@example.com";
        List<Orders> ordersList = new ArrayList<>();
        when(orderRepository.getByCustomerEmail(email)).thenReturn(ordersList);

        // Act
        List<Orders> retrievedOrders = orderService.getOrdersByEmail(email);

        // Assert
        assertEquals(ordersList, retrievedOrders);
        verify(orderRepository, times(1)).getByCustomerEmail(email);
    }

    @Test
    void removeOrders_WithValidIdAndProductDetails_ShouldRemoveOrders() {
        // Arrange
        Long orderId = 1L;
        Long productId = 2L;
        Long quantity = 10L;
        Orders orders = new Orders();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orders));

        // Act
        orderService.removeOrders(orderId, productId, quantity);

        // Assert
        verify(productServiceClient, times(1)).increaseProductQuantity(productId, quantity);
        verify(orderRepository, times(1)).delete(orders);
    }

    @Test
    void removeOrders_WithInvalidId_ShouldThrowOrderNotFoundException() {
        // Arrange
        Long orderId = 1L;
        Long productId = 2L;
        Long quantity = 10L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(OrderNotFoundException.class,
                () -> orderService.removeOrders(orderId, productId, quantity));
        verify(orderRepository, never()).delete(any());
    }



    @Test
    void createOrder_WithInvalidOrderDTO_ShouldThrowInvalidOrderDataException() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomerEmail("invalid-email");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(InvalidOrderDataException.class,
                () -> orderService.createOrder(orderDTO, bindingResult));
        verify(bindingResult, times(1)).hasErrors();
        verify(bindingResult, times(1)).getFieldErrors();
        verifyNoInteractions(productServiceClient);
    }





    @Test
    void removeOrders_WithValidData_ShouldRemoveOrdersAndIncreaseProductQuantity() {
        // Arrange
        Long orderId = 1L;
        Long productId = 1L;
        Long quantity = 10L;

        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setCustomerEmail("test@example.com");
        orders.setAddress("123 Test Street");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orders));

        // Act
        orderService.removeOrders(orderId, productId, quantity);

        // Assert
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).delete(orders);
        verify(productServiceClient, times(1)).increaseProductQuantity(productId, quantity);
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(productServiceClient);
    }

    @Test
    void removeOrders_WithInvalidOrderId_ShouldThrowOrderNotFoundException() {
        // Arrange
        Long orderId = 1L;
        Long productId = 1L;
        Long quantity = 10L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(OrderNotFoundException.class,
                () -> orderService.removeOrders(orderId, productId, quantity));
        verify(orderRepository, times(1)).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(productServiceClient);
    }
}