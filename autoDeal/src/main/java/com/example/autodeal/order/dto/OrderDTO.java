package com.example.autodeal.order.dto;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Set;
@Slf4j
public class OrderDTO {

    private Integer id;
    private LocalDateTime orderDate;
    private Integer userId;
    private Set<OrderLineDTO> orderLines;
    private String status;
    private Double totalPrice; // Calculated field


    public OrderDTO() {
        super();
    }
}
