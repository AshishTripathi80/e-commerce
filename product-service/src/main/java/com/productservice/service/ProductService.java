package com.productservice.service;

import com.productservice.enums.Constants;
import com.productservice.exception.InvalidProductDataException;
import com.productservice.exception.ProductNotFoundException;
import com.productservice.model.MultipleProduct;
import com.productservice.model.Product;
import com.productservice.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Retrieve all products with pagination.
     *
     * @param pageable The pagination information.
     * @return The page of products.
     */
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * Create a new product.
     *
     * @param product       The product to be created.
     * @param bindingResult The BindingResult object for validation.
     * @return The created product.
     */
    public Product createProduct(Product product, BindingResult bindingResult) {
        // Check for validation errors
        validationError(bindingResult);
        product.setCode(UUID.randomUUID());
        return productRepository.save(product);
    }

    /**
     * Handle validation errors.
     *
     * @param bindingResult The BindingResult object containing validation errors.
     * @throws InvalidProductDataException if validation errors are present.
     */
    public void validationError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> errors = new ArrayList<>();

            // Convert each field error to a string representation
            for (FieldError fieldError : fieldErrors) {
                errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            }

            // Throw an exception with the validation errors
            throw new InvalidProductDataException("Validation Failed!", LocalDateTime.now(), errors);
        }
    }

    /**
     * Retrieve a product by ID.
     *
     * @param id The ID of the product.
     * @return The retrieved product.
     * @throws ProductNotFoundException if the product is not found.
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(Constants.ERROR_PRODUCT_NOT_FOUND.getMessage() + id));
    }

    /**
     * Retrieve products by name.
     *
     * @param name The name of the products.
     * @return The list of products with the given name.
     * @throws ProductNotFoundException if no products are found with the given name.
     */
    public List<Product> getProductByName(String name) {
        List<Product> products = productRepository.findAllByName(name);
        if (products.isEmpty()) {
            throw new ProductNotFoundException(Constants.ERROR_PRODUCT_NOT_FOUND.getMessage() + name);
        }
        return products;
    }

    /**
     * Retrieve products by category.
     *
     * @param category The category of the products.
     * @return The list of products with the given category.
     * @throws ProductNotFoundException if no products are found with the given category.
     */
    public List<Product> getProductByCategory(String category) {
        List<Product> products = productRepository.findAllByCategory(category);
        if (products.isEmpty()) {
            throw new ProductNotFoundException(Constants.ERROR_PRODUCT_NOT_FOUND.getMessage() + category);
        }
        return products;
    }

    /**
     * Retrieve products by brand.
     *
     * @param brand The brand of the products.
     * @return The list of products with the given brand.
     * @throws ProductNotFoundException if no products are found with the given brand.
     */
    public List<Product> getProductByBrand(String brand) {
        List<Product> products = productRepository.findAllByBrand(brand);
        if (products.isEmpty()) {
            throw new ProductNotFoundException(Constants.ERROR_PRODUCT_NOT_FOUND.getMessage() + brand);
        }
        return products;
    }

    /**
     * Update a product.
     *
     * @param id            The ID of the product to be updated.
     * @param productData   The updated product data.
     * @param bindingResult The BindingResult object for validation.
     * @return The updated product.
     */
    public Product updateProduct(Long id, Product productData, BindingResult bindingResult) {
        // Check for validation errors
        validationError(bindingResult);

        Product product = getProductById(id);

        product.setName(productData.getName());
        product.setDescription(productData.getDescription());
        product.setBrand(productData.getBrand());
        product.setPrice(productData.getPrice());
        product.setCategory(productData.getCategory());
        product.setAvailableQuantity(productData.getAvailableQuantity());
        product.setCode(productData.getCode());

        return productRepository.save(product);
    }

    /**
     * Delete a product by ID.
     *
     * @param id The ID of the product to be deleted.
     */
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    /**
     * Create multiple products.
     *
     * @param multipleProduct The object containing multiple products.
     * @return The list of created products.
     */
    public List<Product> createMultipleProducts(MultipleProduct multipleProduct) {
        List<Product> products = multipleProduct.getProducts();
        for (Product product : products) {
            UUID uuid = UUID.randomUUID();
            product.setCode(uuid);
        }


        return productRepository.saveAll(products);
    }

    /**
     * Search for products based on provided parameters.
     *
     * @param name     The name of the products to search for.
     * @param category The category of the products to search for.
     * @param brand    The brand of the products to search for.
     * @return The list of products matching the search criteria.
     */
    public List<Product> searchProducts(String name, String category, String brand) {
        // Perform the search based on the provided parameters
        List<Product> searchResults = new ArrayList<>();

        // Search by name
        if (name != null) {
            searchResults.addAll(getProductByName(name));
        }

        // Search by category
        if (category != null) {
            searchResults.addAll(getProductByCategory(category));
        }

        // Search by brand
        if (brand != null) {
            searchResults.addAll(getProductByBrand(brand));
        }

        return searchResults;
    }
}
