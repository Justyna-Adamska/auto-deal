package com.example.autodeal.cart;

import com.example.autodeal.product.dto.ProductDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@NoArgsConstructor
@Data
@Entity
@Table(name = "carts")
public class CartModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public CartModel(Integer userId) {
        this.userId = userId;
        this.lastUpdated = new Date();
    }

    public void addItem(ProductDto productDto) {
        CartItem newItem = new CartItem();
        newItem.setProductId(Long.valueOf(productDto.getId()));
        newItem.setPrice(new BigDecimal(productDto.getPrice()));
        items.add(newItem);
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

}
