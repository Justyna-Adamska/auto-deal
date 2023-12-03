package com.example.autodeal.order.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_line")
public class OrderLineModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private OrderModel order;

    @Column(name = "productId", nullable = false)
    private Integer productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unitPrice", nullable = false)
    private Double unitPrice;

    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice;

    @Column(name = "discount")
    private Double discount;

    public OrderLineModel() {
        super();
    }

    // Metoda do obliczania całkowitej ceny dla OrderLine
    public double calculateLineTotal() {
        if (discount != null) {
            return (unitPrice * quantity) - discount;
        } else {
            return unitPrice * quantity;
        }
    }

    @PrePersist
    @PreUpdate
    private void updateTotalPrice() {
        this.totalPrice = calculateLineTotal();
    }
}
