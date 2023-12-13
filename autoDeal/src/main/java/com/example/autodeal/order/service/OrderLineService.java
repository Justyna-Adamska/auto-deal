package com.example.autodeal.order.service;

import com.example.autodeal.exception.OrderLineValidationException;
import com.example.autodeal.order.dto.OrderLineDTO;
import com.example.autodeal.order.mapper.OrderMapper;
import com.example.autodeal.order.model.OrderLineModel;
import com.example.autodeal.order.repository.OrderLineRepository;
import com.example.autodeal.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderLineService(OrderLineRepository orderLineRepository, ProductRepository productRepository, OrderMapper orderMapper) {
        this.orderLineRepository = orderLineRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderLineDTO createOrderLine(OrderLineDTO orderLineDTO) {
        validateOrderLine(orderLineDTO);
        OrderLineModel orderLine = orderMapper.toOrderLineModel(orderLineDTO);
        OrderLineModel savedOrderLine = orderLineRepository.save(orderLine);
        return orderMapper.toOrderLineDTO(savedOrderLine);
    }

    public Optional<OrderLineDTO> getOrderLineById(Integer id) {
        return orderLineRepository.findById(id)
                .map(orderMapper::toOrderLineDTO);
    }

    public List<OrderLineDTO> getAllOrderLines() {
        return orderLineRepository.findAll().stream()
                .map(orderMapper::toOrderLineDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderLineDTO updateOrderLine(OrderLineDTO orderLineDTO) {
        validateOrderLine(orderLineDTO);
        if (!orderLineRepository.existsById(orderLineDTO.getId())) {
            throw new OrderLineValidationException("Order line not found");
        }
        OrderLineModel orderLine = orderMapper.toOrderLineModel(orderLineDTO);
        OrderLineModel updatedOrderLine = orderLineRepository.save(orderLine);
        return orderMapper.toOrderLineDTO(updatedOrderLine);
    }

    public void deleteOrderLine(Integer id) {
        if (!orderLineRepository.existsById(id)) {
            throw new OrderLineValidationException("Order line not found");
        }
        orderLineRepository.deleteById(id);
    }

    private void validateOrderLine(OrderLineDTO orderLineDTO) {
        if (orderLineDTO == null) {
            throw new OrderLineValidationException("Order line cannot be null");
        }
        if (!productRepository.existsById(orderLineDTO.getProductId())) {
            throw new OrderLineValidationException("Product with ID " + orderLineDTO.getProductId() + " does not exist");
        }
        if (orderLineDTO.getQuantity() == null || orderLineDTO.getQuantity() <= 0) {
            throw new OrderLineValidationException("Quantity must be greater than 0");
        }
        if (orderLineDTO.getUnitPrice() == null || orderLineDTO.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderLineValidationException("Unit price must be greater than 0");
        }
    }
}
