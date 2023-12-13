package com.example.autodeal.cart;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "carts")
@ToString
public class CartModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public CartModel(Integer userId) {
        this.userId = userId;
        this.lastUpdated = new Date();
    }

    public void addItem(CartItem item) {
        if (item.getPrice() == null) {
            throw new IllegalArgumentException("Item price cannot be null");
        }

        items.add(item);
        item.setCart(this);
        this.lastUpdated = new Date();
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        this.lastUpdated = new Date();
    }

    public void clearItems() {
        this.items.clear();
        this.lastUpdated = new Date();
    }


    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getPrice());
        }
        return total;
    }


    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartModel cartModel = (CartModel) o;
        return Objects.equals(id, cartModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
