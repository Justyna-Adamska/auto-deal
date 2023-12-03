package com.example.autodeal.order.model;

import com.example.autodeal.user.model.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "user_order")
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "orderDate", nullable = false)
    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserModel user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderLineModel> orderLines;

    @Column(name = "status")
    private String status;

    public OrderModel() {
        super();
    }

    // Metoda do obliczania całkowitej wartości zamówienia
    public double calculateTotalPrice() {
        return orderLines.stream()
                .mapToDouble(OrderLineModel::getTotalPrice)
                .sum();
    }
}
