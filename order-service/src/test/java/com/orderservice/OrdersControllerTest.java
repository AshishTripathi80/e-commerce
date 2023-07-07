package com.orderservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.controller.OrderController;
import com.orderservice.model.Orders;
import com.orderservice.model.OrderDTO;
import com.orderservice.service.OrderService;
import jakarta.ws.rs.core.MediaType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrdersControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void createOrder_ValidOrderData_ReturnsCreatedStatus() throws Exception {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomerEmail("test@example.com");
        orderDTO.setCustomerAddress("123 Test Street");

        when(orderService.createOrder(any(OrderDTO.class), any(BindingResult.class)))
                .thenReturn(new Orders());

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Assert
        verify(orderService, times(1)).createOrder(any(OrderDTO.class), any(BindingResult.class));
    }




    @Test
    public void removeOrders_ValidData_ReturnsNoContent() throws Exception {
        // Arrange
        Long orderId = 1L;
        Long productId = 1L;
        Long quantity = 10L;

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/{id}", orderId)
                        .param("productId", String.valueOf(productId))
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Assert
        verify(orderService, times(1)).removeOrders(orderId, productId, quantity);
    }


   /* @Test
    public void removeOrders_InvalidOrderId_ReturnsNotFound() throws Exception {
        // Arrange
        Long orderId = 1L;
        Long productId = 1L;
        Long quantity = 10L;

        doThrow(OrderNotFoundException.class)
                .when(orderService).removeOrders(orderId, productId, quantity);

        // Act
        mockMvc.perform(delete("/orders/{orderId}/products/{productId}", orderId, productId)
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isNotFound());

        // Assert
        verify(orderService, times(1)).removeOrders(orderId, productId, quantity);
    }*/

    @Test
    public void getOrdersByEmail_ValidEmail_ReturnsListOfOrders() throws Exception {
        // Arrange
        String email = "example@example.com";
        List<Orders> orders = Arrays.asList(
                new Orders(1L, "example@example.com", new Date(), new ArrayList<>(), new ArrayList<>(), "Address 1", 5),
                new Orders(2L, "example@example.com", new Date(), new ArrayList<>(), new ArrayList<>(), "Address 2", 3)
        );
        when(orderService.getOrdersByEmail(email)).thenReturn(orders);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{email}", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerEmail").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerEmail").value(email));

        // Assert
        verify(orderService, times(1)).getOrdersByEmail(email);
    }


    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
