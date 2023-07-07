package com.orderservice.service;

import com.orderservice.model.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "product-service") // Provide the name of the product-service
public interface ProductServiceClient {

    @GetMapping("api/products/{id}")
    ProductDTO getProduct(@PathVariable("id") Long id);

    @PostMapping("api/products/order/{id}")
    Long reduceProductQuantity(@PathVariable("id")Long id,@RequestBody Long unit);

    @PostMapping("api/products/cancel/{id}")
    Long increaseProductQuantity(@PathVariable("id")Long id,@RequestBody Long unit);
}
