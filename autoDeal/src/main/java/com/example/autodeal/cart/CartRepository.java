package com.example.autodeal.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartModel, Long> {
    Optional<CartModel> findByUserId(Integer userId);
    void deleteByUserId(Integer userId);
}

