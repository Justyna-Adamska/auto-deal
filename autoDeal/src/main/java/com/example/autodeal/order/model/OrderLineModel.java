package com.example.autodeal.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
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
    private BigDecimal unitPrice;

    @Column(name = "totalPrice", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "discount")
    private BigDecimal discount;

    public OrderLineModel() {
        super();
    }


    public BigDecimal calculateLineTotal() {
        BigDecimal quantityAsBigDecimal = BigDecimal.valueOf(quantity);

        if (discount != null) {
            return unitPrice.multiply(quantityAsBigDecimal).subtract(discount);
        } else {
            return unitPrice.multiply(quantityAsBigDecimal);
        }
    }

    @PrePersist
    @PreUpdate
    private void updateTotalPrice() {
        this.totalPrice = calculateLineTotal();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, quantity, unitPrice, totalPrice, discount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineModel that = (OrderLineModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(unitPrice, that.unitPrice) &&
                Objects.equals(totalPrice, that.totalPrice) &&
                Objects.equals(discount, that.discount);
    }

}
