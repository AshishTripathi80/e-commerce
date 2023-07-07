package com.productservice;

import com.productservice.exception.InvalidProductDataException;
import com.productservice.exception.ProductNotFoundException;
import com.productservice.model.MultipleProduct;
import com.productservice.model.Product;
import com.productservice.repo.ProductRepository;
import com.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        // Mock the Page object
        Page<Product> mockPage = mock(Page.class);
        when(mockPage.getContent()).thenReturn(new ArrayList<>());

        // Mock the ProductRepository to return the mock Page
        when(productRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        // Call the service method
        Page<Product> result = productService.getAllProducts(Pageable.unpaged());

        // Verify the mock interactions
        verify(productRepository, times(1)).findAll(any(Pageable.class));

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getContent().isEmpty());
    }

    @Test
    void testCreateProduct() {
        // Mock the product and binding result
        Product mockProduct = new Product();
        BindingResult mockBindingResult = mock(BindingResult.class);
        when(mockBindingResult.hasErrors()).thenReturn(false);

        // Mock the ProductRepository to return the mock product
        when(productRepository.save(mockProduct)).thenReturn(mockProduct);

        // Call the service method
        Product result = productService.createProduct(mockProduct, mockBindingResult);

        // Verify the mock interactions
        verify(productRepository, times(1)).save(mockProduct);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockProduct, result);
    }

    @Test
    void testCreateProduct_WithValidationErrors() {
        // Mock the product and binding result with validation errors
        Product mockProduct = new Product();
        BindingResult mockBindingResult = mock(BindingResult.class);
        when(mockBindingResult.hasErrors()).thenReturn(true);

        // Call the service method and assert the exception
        Assertions.assertThrows(InvalidProductDataException.class,
                () -> productService.createProduct(mockProduct, mockBindingResult));

        // Verify that the save method was not called
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testGetProductById() {
        // Mock the product ID
        Long productId = 1L;

        // Mock the ProductRepository to return an Optional containing the mock product
        Product mockProduct = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        // Call the service method
        Product result = productService.getProductById(productId);

        // Verify the mock interactions
        verify(productRepository, times(1)).findById(productId);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockProduct, result);
    }

    @Test
    void testGetProductById_ProductNotFound() {
        // Mock the product ID
        Long productId = 1L;

        // Mock the ProductRepository to return an empty Optional
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call the service method and assert the exception
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(productId));

        // Verify the mock interactions
        verify(productRepository, times(1)).findById(productId);
    }



    @Test
    void testGetProductByName() {
        // Mock the product name
        String productName = "Test Product";

        // Mock the ProductRepository to return a list of products with the given name
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(new Product());
        mockProducts.add(new Product());
        when(productRepository.findAllByName(productName)).thenReturn(mockProducts);

        // Call the service method
        List<Product> result = productService.getProductByName(productName);

        // Verify the mock interactions
        verify(productRepository, times(1)).findAllByName(productName);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockProducts, result);
    }

    @Test
    void testGetProductByName_ProductNotFound() {
        // Mock the product name
        String productName = "Test Product";

        // Mock the ProductRepository to return an empty list
        when(productRepository.findAllByName(productName)).thenReturn(new ArrayList<>());

        // Call the service method and assert the exception
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.getProductByName(productName));

        // Verify the mock interactions
        verify(productRepository, times(1)).findAllByName(productName);
    }

    @Test
    void testGetProductByCategory() {
        // Mock the product category
        String productCategory = "Test Category";

        // Mock the ProductRepository to return a list of products with the given category
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(new Product());
        mockProducts.add(new Product());
        when(productRepository.findAllByCategory(productCategory)).thenReturn(mockProducts);

        // Call the service method
        List<Product> result = productService.getProductByCategory(productCategory);

        // Verify the mock interactions
        verify(productRepository, times(1)).findAllByCategory(productCategory);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockProducts, result);
    }

    @Test
    void testGetProductByCategory_ProductNotFound() {
        // Mock the product category
        String productCategory = "Test Category";

        // Mock the ProductRepository to return an empty list
        when(productRepository.findAllByCategory(productCategory)).thenReturn(new ArrayList<>());

        // Call the service method and assert the exception
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.getProductByCategory(productCategory));

        // Verify the mock interactions
        verify(productRepository, times(1)).findAllByCategory(productCategory);
    }

    @Test
    void testGetProductByBrand() {
        // Mock the product brand
        String productBrand = "Test Brand";

        // Mock the ProductRepository to return a list of products with the given brand
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(new Product());
        mockProducts.add(new Product());
        when(productRepository.findAllByBrand(productBrand)).thenReturn(mockProducts);

        // Call the service method
        List<Product> result = productService.getProductByBrand(productBrand);

        // Verify the mock interactions
        verify(productRepository, times(1)).findAllByBrand(productBrand);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockProducts, result);
    }

    @Test
    void testGetProductByBrand_ProductNotFound() {
        // Mock the product brand
        String productBrand = "Test Brand";

        // Mock the ProductRepository to return an empty list
        when(productRepository.findAllByBrand(productBrand)).thenReturn(new ArrayList<>());

        // Call the service method and assert the exception
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.getProductByBrand(productBrand));

        // Verify the mock interactions
        verify(productRepository, times(1)).findAllByBrand(productBrand);
    }



    @Test
    void testUpdateProduct() {
        // Mock the product ID and updated product data
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        // Mock the ProductRepository to return the original product and save the updated product
        Product mockProduct = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(mockProduct)).thenReturn(mockProduct);

        // Call the service method
        Product result = productService.updateProduct(productId, updatedProduct, mock(BindingResult.class));

        // Verify the mock interactions
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(mockProduct);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockProduct, result);
        Assertions.assertEquals(updatedProduct.getName(), result.getName());
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        // Mock the product ID and updated product data
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        // Mock the ProductRepository to return an empty Optional
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call the service method and assert the exception
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(productId, updatedProduct, mock(BindingResult.class)));

        // Verify the mock interactions
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        // Mock the product ID
        Long productId = 1L;

        // Mock the ProductRepository to return the mock product
        Product mockProduct = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        // Call the service method
        productService.deleteProduct(productId);

        // Verify the mock interactions
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(mockProduct);
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        // Mock the product ID
        Long productId = 1L;

        // Mock the ProductRepository to return an empty Optional
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call the service method and assert the exception
        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(productId));

        // Verify the mock interactions
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void testCreateMultipleProducts() {
        // Mock the multiple products
        MultipleProduct multipleProduct = new MultipleProduct();
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        multipleProduct.setProducts(products);

        // Mock the ProductRepository to save the products
        when(productRepository.saveAll(products)).thenReturn(products);

        // Call the service method
        List<Product> result = productService.createMultipleProducts(multipleProduct);

        // Verify the mock interactions
        verify(productRepository, times(1)).saveAll(products);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(products, result);
    }

    @Test
    void testSearchProducts() {
        // Mock the search parameters
        String productName = "Test Product";
        String productCategory = "Test Category";
        String productBrand = "Test Brand";

        // Mock the ProductRepository to return the search results
        List<Product> mockResults = new ArrayList<>();
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        mockResults.add(product1);
        mockResults.add(product2);
        mockResults.add(product3);
        when(productRepository.findAllByName(productName)).thenReturn(mockResults.subList(0, 1));
        when(productRepository.findAllByCategory(productCategory)).thenReturn(mockResults.subList(1, 2));
        when(productRepository.findAllByBrand(productBrand)).thenReturn(mockResults.subList(2, 3));

        // Call the service method
        List<Product> result = productService.searchProducts(productName, productCategory, productBrand);

        // Verify the mock interactions
        verify(productRepository, times(1)).findAllByName(productName);
        verify(productRepository, times(1)).findAllByCategory(productCategory);
        verify(productRepository, times(1)).findAllByBrand(productBrand);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockResults, result);
    }

}
