package com.example.autodeal.order.service;

import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderModel createOrder(OrderModel order) {
        return orderRepository.save(order);
    }

    public Optional<OrderModel> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    public List<OrderModel> getAllOrders() {
        return orderRepository.findAll();
    }

    public OrderModel updateOrder(OrderModel order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

}
