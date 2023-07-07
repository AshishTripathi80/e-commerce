package com.productservice.repo;

import com.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByName(String name);

    List<Product> findAllByCategory(String category);

    List<Product> findAllByBrand(String brand);
}
