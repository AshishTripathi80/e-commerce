package com.orderservice.controller;

import com.orderservice.model.Orders;
import com.orderservice.model.OrderDTO;
import com.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * Create a new order.
     *
     * @param orderDTO      The order details.
     * @param bindingResult The binding result for validation errors.
     * @return The created order.
     */
    @PostMapping
    public ResponseEntity<Orders> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult bindingResult) {
        logger.info("Received request to create order: {}", orderDTO);

        // Create a new Orders entity
        Orders orders = orderService.createOrder(orderDTO, bindingResult);
        Orders savedOrders = orderService.saveOrder(orders);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrders);
    }

    /**
     * Get orders by customer email.
     *
     * @param email The customer email.
     * @return The list of orders.
     */
    @GetMapping("/{email}")
    public ResponseEntity<List<Orders>> getOrdersByEmail(@PathVariable String email) {
        logger.info("Received request to get orders by email: {}", email);

        List<Orders> orders = orderService.getOrdersByEmail(email);

        return ResponseEntity.ok(orders);
    }

    /**
     * Remove orders by ID, product ID, and quantity.
     *
     * @param id        The ID of the orders.
     * @param productId The ID of the product.
     * @param quantity  The quantity of the product.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeOrders(@PathVariable Long id,
                                             @RequestParam Long productId,
                                             @RequestParam Long quantity) {
        logger.info("Received request to remove orders with ID: {}, Product ID: {}, Quantity: {}", id, productId, quantity);

        // Your code to handle the removal of orders with the provided IDs, product ID, and quantity
        orderService.removeOrders(id, productId, quantity);

        return ResponseEntity.noContent().build();
    }
}
