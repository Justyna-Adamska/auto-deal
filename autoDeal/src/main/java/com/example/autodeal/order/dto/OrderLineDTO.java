package com.example.autodeal.order.dto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderLineDTO {
    private Integer id;
    private Integer productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;


}