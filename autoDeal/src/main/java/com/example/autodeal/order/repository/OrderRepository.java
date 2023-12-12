package com.example.autodeal.order.repository;

import com.example.autodeal.order.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderModel, Integer> {
    Optional<OrderModel> findTopByUserIdOrderByOrderDateDesc(Integer userId);
    List<OrderModel> findAllByUserId(Integer userId);
}

