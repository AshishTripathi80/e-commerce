package com.productservice.controller;

import com.productservice.exceptions.ErrorMessages;
import com.productservice.models.Product;
import com.productservice.repo.ProductRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger LOGGER = Logger.getLogger(ProductController.class.getName());

    @Autowired
    private ProductRepository productRepository;

    // Endpoint to get all products
    @GetMapping
    public List<Product> getAllProducts() {
        LOGGER.log(Level.INFO, "GET request received for all products");
        return productRepository.findAll();
    }

    // Endpoint to create a new product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        LOGGER.log(Level.INFO, "POST request received to create a new product: {0}", product.getName());
        return productRepository.save(product);
    }

    // Endpoint to get a specific product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        LOGGER.log(Level.INFO, "GET request received for product with ID: {0}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, ErrorMessages.ErrorMessage.PRODUCT_NOT_FOUND.getMessage(), id);
                    return new NotFoundException(ErrorMessages.ErrorMessage.PRODUCT_NOT_FOUND.getMessage() + id);
                });
    }

    // Endpoint to update an existing product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productData) {
        LOGGER.log(Level.INFO, "PUT request received to update product with ID: {0}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "Product not found with ID: {0}", id);
                    return new NotFoundException(ErrorMessages.ErrorMessage.PRODUCT_NOT_FOUND.getMessage() + id);
                });

        product.setName(productData.getName());
        product.setSortDescription(productData.getSortDescription());
        product.setFullDescription(productData.getFullDescription());

        return productRepository.save(product);
    }

    // Endpoint to delete a product by ID
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        LOGGER.log(Level.INFO, "DELETE request received to delete product with ID: {0}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "Product not found with ID: {0}", id);
                    return new NotFoundException(ErrorMessages.ErrorMessage.PRODUCT_NOT_FOUND.getMessage() + id);
                });

        productRepository.delete(product);
    }
}
