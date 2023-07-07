package com.productservice.controller;

import com.productservice.model.MultipleProduct;
import com.productservice.model.PageResponse;
import com.productservice.model.Product;
import com.productservice.repo.ProductRepository;
import com.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger LOGGER = Logger.getLogger(ProductController.class.getName());

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    // Endpoint to get All products
    @GetMapping
    public PageResponse<Product> getAllProducts(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        LOGGER.log(Level.INFO, "GET request received for all products");

        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
        Page<Product> pagedResult = productService.getAllProducts(paging);

        PageResponse<Product> response = new PageResponse<>();
        response.setContent(pagedResult.getContent());
        response.setPageNo(pagedResult.getNumber());
        response.setPageSize(pagedResult.getSize());
        response.setTotalPages(pagedResult.getTotalPages());
        response.setTotalItems(pagedResult.getTotalElements());

        return response;
    }

    // Endpoint to create a new product
    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
        LOGGER.log(Level.INFO, "POST request received to create a new product: {0}", product.getName());
        return productService.createProduct(product, bindingResult);
    }

    // Endpoint to create multiple products
    @PostMapping("/addAll")
    public List<Product> createMultipleProducts(@Valid @RequestBody MultipleProduct multipleProduct, BindingResult bindingResult) {
        LOGGER.log(Level.INFO, "POST request received to create multiple products");
        productService.validationError(bindingResult);
        return productService.createMultipleProducts(multipleProduct);
    }

    // Endpoint to get a specific product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        LOGGER.log(Level.INFO, "GET request received for product with ID: {0}", id);
        return productService.getProductById(id);
    }

    // Endpoint to get a specific product by Name
    @GetMapping("name/{name}")
    public List<Product> getProductByName(@PathVariable String name) {
        LOGGER.log(Level.INFO, "GET request received for product with name: {0}", name);
        return productService.getProductByName(name);
    }

    // Endpoint to get a specific product by Name
    @GetMapping("category/{category}")
    public List<Product> getProductByBrand(@PathVariable String category) {
        LOGGER.log(Level.INFO, "GET request received for product with category: {0}", category);
        return productService.getProductByCategory(category);
    }


    // Endpoint to update an existing product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product productData, BindingResult bindingResult) {
        LOGGER.log(Level.INFO, "PUT request received to update product with ID: {0}", id);
        return productService.updateProduct(id, productData, bindingResult);
    }

    // Endpoint to delete a product by ID
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        LOGGER.log(Level.INFO, "DELETE request received to delete product with ID: {0}", id);
        productService.deleteProduct(id);
    }

    // Endpoint to search for products
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "category", required = false) String category,
                                        @RequestParam(value = "brand", required = false) String brand) {
        LOGGER.log(Level.INFO, "GET request received to search for products");
        return productService.searchProducts(name, category, brand);
    }

    @PostMapping("/order/{id}")
    public Long reduceProductQuantity(@PathVariable Long id, @RequestBody Long unit) {
        LOGGER.log(Level.INFO, "POST request received for reduceProductQuantity  with ID: {0}", id);
        Product product = productService.getProductById(id);

        if (product != null) {
            Long availableQuantity = product.getAvailableQuantity();
            Long reducedQuantity = availableQuantity - unit;

            if (reducedQuantity >= 0) {
                product.setAvailableQuantity(reducedQuantity);
                productRepository.save(product);
                return reducedQuantity;
            } else {
                // Handle the case where the requested quantity exceeds the available quantity
                throw new IllegalArgumentException("Requested quantity exceeds available quantity");
            }
        } else {
            // Handle the case where the product is not found
            throw new IllegalArgumentException("Product not found");
        }
    }
    @PostMapping("/cancel/{id}")
    public Long increaseProductQuantity(@PathVariable Long id, @RequestBody Long unit) {
        LOGGER.log(Level.INFO, "POST request received for IncreaseProductQuantity  with ID: {0}", id);
        Product product = productService.getProductById(id);

        if (product != null) {
            Long availableQuantity = product.getAvailableQuantity();
            Long reducedQuantity = availableQuantity + unit;
            product.setAvailableQuantity(reducedQuantity);
            productRepository.save(product);
            return reducedQuantity;

        } else {
            // Handle the case where the product is not found
            throw new IllegalArgumentException("Product not found");
        }
    }
}
