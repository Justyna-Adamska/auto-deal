package com.example.autodeal.order.repository;

import com.example.autodeal.order.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderModel, Integer> {

}
