package com.orderservice.service;

import com.orderservice.exception.InvalidOrderDataException;
import com.orderservice.exception.OrderNotFoundException;
import com.orderservice.model.*;
import com.orderservice.repo.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductServiceClient productServiceClient;

    /**
     * Save the order.
     *
     * @param orders The order to be saved.
     * @return The saved order.
     */
    public Orders saveOrder(Orders orders) {
        return orderRepository.save(orders);
    }

    /**
     * Get orders by customer email.
     *
     * @param email The customer email.
     * @return The list of orders.
     */
    public List<Orders> getOrdersByEmail(String email) {
        return orderRepository.getByCustomerEmail(email);
    }

    /**
     * Remove orders by ID, product ID, and quantity.
     *
     * @param id        The ID of the orders.
     * @param productId The ID of the product.
     * @param quantity  The quantity of the product.
     */
    public void removeOrders(Long id, Long productId, Long quantity) {
        Orders orders = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        productServiceClient.increaseProductQuantity(productId, quantity);
        orderRepository.delete(orders);
        logger.info("Orders with ID: {} and associated product ID: {} removed successfully.", id, productId);
    }

    /**
     * Create an order from the provided OrderDTO.
     *
     * @param orderDTO      The OrderDTO object containing order details.
     * @param bindingResult The binding result for validation errors.
     * @return The created order.
     */
    public Orders createOrder(OrderDTO orderDTO, BindingResult bindingResult) {
        // Check for validation errors
        validationError(bindingResult);

        Orders orders = new Orders();
        orders.setCustomerEmail(orderDTO.getCustomerEmail());
        orders.setAddress(orderDTO.getCustomerAddress());
        orders.setOrderDate(new Date());

        // Check product availability for each item in the order
        List<ProductList> productList = orderDTO.getProductList();
        List<ProductDTO> availableProducts = new ArrayList<>();
        List<UnsuccessfulProduct> unAvailableProducts = new ArrayList<>();

        productList.forEach(product -> {
            Long productId = product.getId();
            try {
                ProductDTO productDTO = productServiceClient.getProduct(productId);

                if (productDTO != null && productDTO.getAvailableQuantity() >= product.getUnit()) {
                    productDTO.setStatus("Successful");

                    // reducing product quantity after product Successful ordered
                    Long availableQuantity = productServiceClient.reduceProductQuantity(productId, product.getUnit());
                    productDTO.setAvailableQuantity(availableQuantity);
                    availableProducts.add(productDTO);
                } else {
                    UnsuccessfulProduct unsuccessfulProduct = new UnsuccessfulProduct();
                    unsuccessfulProduct.setId(productId);
                    unsuccessfulProduct.setComment("Available Quantity is less than Order Quantity");
                    unAvailableProducts.add(unsuccessfulProduct);
                }
            } catch (Exception e) {
                // Handle the exception and set the status to "Unsuccessful"
                UnsuccessfulProduct unsuccessfulProduct = new UnsuccessfulProduct();
                unsuccessfulProduct.setId(productId);
                unsuccessfulProduct.setComment("Product Not Available By ID: " + productId);
                unAvailableProducts.add(unsuccessfulProduct);
            }
        });

        orders.setListOfProducts(availableProducts);
        orders.setUnsuccessfulProductList(unAvailableProducts);
        return orders;
    }

    /**
     * Handle validation errors.
     *
     * @param bindingResult The BindingResult object containing validation errors.
     * @throws InvalidOrderDataException if validation errors are present.
     */
    public void validationError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> errors = new ArrayList<>();

            // Convert each field error to a string representation
            for (FieldError fieldError : fieldErrors) {
                errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            }

            // Log validation errors
            logger.error("Validation errors occurred: {}", errors);

            // Throw an exception with the validation errors
            throw new InvalidOrderDataException("Validation Failed!", LocalDateTime.now(), errors);
        }
    }
}
