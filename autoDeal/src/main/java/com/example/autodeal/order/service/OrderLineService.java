package com.example.autodeal.order.service;

import com.example.autodeal.order.model.OrderLineModel;
import com.example.autodeal.order.repository.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;

    @Autowired
    public OrderLineService(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }

    public OrderLineModel createOrderLine(OrderLineModel orderLine) {
        return orderLineRepository.save(orderLine);
    }

    public Optional<OrderLineModel> getOrderLineById(Integer id) {
        return orderLineRepository.findById(id);
    }

    public List<OrderLineModel> getAllOrderLines() {
        return orderLineRepository.findAll();
    }

    public OrderLineModel updateOrderLine(OrderLineModel orderLine) {
        return orderLineRepository.save(orderLine);
    }

    public void deleteOrderLine(Integer id) {
        orderLineRepository.deleteById(id);
    }

}
