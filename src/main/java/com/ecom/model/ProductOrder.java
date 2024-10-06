package com.ecom.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    private String orderId;

    private LocalDate orderDate;

    @ManyToOne
    private Product product;

    @ManyToOne
    private UserDtl user;

    @OneToOne( cascade = CascadeType.ALL)
    private OrderAddress orderAddress;

    private Integer quantity;

    private Long price;

    private String status;

    private String paymentType;


}
