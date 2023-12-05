package com.example.autodeal.order.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class OrderDTO {

    private Integer id;
    private LocalDateTime orderDate;
    private Integer userId;
    private String status;
    private Set<OrderLineDTO> orderLines;
    private BigDecimal totalAmount;
    private PaymentDetailsDTO paymentDetails;

}
