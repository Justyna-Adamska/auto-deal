package com.example.autodeal.order.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "payment_details")
public class PaymentDetailsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private BigDecimal amount; // Całkowita kwota zamówienia

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentMethod;

    @Column(nullable = false)
    private BigDecimal reservedAmount; // Kwota zaliczki wpłacona online (20% ceny samochodu)

    @Column(nullable = false)
    private BigDecimal balanceAmount; // Kwota do zapłaty przy odbiorze samochodu

    @Column
    private Date paymentDate; // Data dokonania płatności

    @Column
    private Date reservationExpireDate; // Data wygaśnięcia rezerwacji

    @Column
    private String transactionId; // ID transakcji dla płatności online

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // np. "COMPLETED


    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderModel order;

}
