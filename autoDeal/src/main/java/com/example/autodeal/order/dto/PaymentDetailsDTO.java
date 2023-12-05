package com.example.autodeal.order.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class PaymentDetailsDTO {

    private Integer id;
    private BigDecimal amount;
    private String paymentMethod;
    private BigDecimal reservedAmount;
    private BigDecimal balanceAmount;
    private Date paymentDate;
    private Date reservationExpireDate;
    private String transactionId;
    private String status;
    private Integer orderId;


}


