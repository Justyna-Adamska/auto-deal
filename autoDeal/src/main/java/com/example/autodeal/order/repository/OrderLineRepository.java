package com.example.autodeal.order.repository;

import com.example.autodeal.order.model.OrderLineModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLineModel, Integer> {

}
