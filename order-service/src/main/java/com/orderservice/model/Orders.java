package com.orderservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Table(schema = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerEmail;

    private Date orderDate;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ProductDTO> listOfProducts;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UnsuccessfulProduct> unsuccessfulProductList;

    private String address;

    private int orderQuantity;

}
