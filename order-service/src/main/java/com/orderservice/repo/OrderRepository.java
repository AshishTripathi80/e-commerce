package com.orderservice.repo;

import com.orderservice.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    List<Orders> getByCustomerEmail(String email);
}
