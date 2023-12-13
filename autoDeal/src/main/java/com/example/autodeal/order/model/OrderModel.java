package com.example.autodeal.order.model;

import com.example.autodeal.user.model.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"orderLines", "user", "paymentDetails"})
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private PaymentDetailsModel paymentDetails;

    public OrderModel() {
        super();
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        if (orderLines != null) {
            for (OrderLineModel line : orderLines) {
                total = total.add(line.getTotalPrice());
            }
        }
        return total;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderDate, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderModel that = (OrderModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(orderDate, that.orderDate) &&
                status == that.status;
    }

}
