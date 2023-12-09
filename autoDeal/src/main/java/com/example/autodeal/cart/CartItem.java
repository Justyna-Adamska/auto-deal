package com.example.autodeal.cart;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartModel cart;

    public CartItem() {

    }

    public CartItem(Long productId, BigDecimal price) {
        this.productId = productId;
        this.price = price;
    }

}
