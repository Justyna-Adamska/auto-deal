package com.example.autodeal.order.service;

import com.example.autodeal.exception.*;
import com.example.autodeal.order.dto.OrderDTO;
import com.example.autodeal.order.dto.OrderLineDTO;
import com.example.autodeal.order.mapper.OrderMapper;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.model.PaymentDetailsModel;
import com.example.autodeal.order.repository.OrderRepository;
import com.example.autodeal.product.repository.ProductRepository;
import com.example.autodeal.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final BigDecimal ONLINE_PAYMENT_PERCENTAGE = BigDecimal.valueOf(0.20);
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) throws OrderCreationException, UserNotFoundException, OrderLineValidationException, ProductNotFoundException {
        if (orderDTO == null) {
            throw new OrderCreationException("Order cannot be null");
        }

        // Walidacja danych użytkownika
        if (!userRepository.existsById(orderDTO.getUserId())) {
            throw new UserNotFoundException("User not found with ID: " + orderDTO.getUserId());
        }

        // Walidacja linii zamówienia
        if (orderDTO.getOrderLines() == null || orderDTO.getOrderLines().isEmpty()) {
            throw new OrderLineValidationException("Order must have at least one line item");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderLineDTO orderLineDTO : orderDTO.getOrderLines()) {
            if (!productRepository.existsById(orderLineDTO.getProductId())) {
                throw new ProductNotFoundException("Product not found with ID: " + orderLineDTO.getProductId());
            }

            if (orderLineDTO.getQuantity() <= 0 || orderLineDTO.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new OrderLineValidationException("Quantity and unit price must be positive");
            }

            totalAmount = totalAmount.add(orderLineDTO.getUnitPrice().multiply(BigDecimal.valueOf(orderLineDTO.getQuantity())));
        }

        // Walidacja daty zamówienia
        if (orderDTO.getOrderDate() != null && orderDTO.getOrderDate().isAfter(ChronoLocalDateTime.from(LocalDate.now()))) {
            throw new OrderCreationException("Order date cannot be in the future");
        }

        // Walidacja całkowitej kwoty zamówienia
        if (orderDTO.getTotalAmount() == null || orderDTO.getTotalAmount().compareTo(totalAmount) != 0) {
            throw new OrderCreationException("Total amount is not correct");
        }

        OrderModel order = orderMapper.toOrderModel(orderDTO);
        return orderMapper.toOrderDTO(orderRepository.save(order));
    }

    public Optional<OrderDTO> getOrderById(Integer id) throws OrderNotFoundException {
        return Optional.ofNullable(orderRepository.findById(id)
                .map(orderMapper::toOrderDTO)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id)));
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrder(OrderDTO orderDTO) throws OrderCreationException, OrderNotFoundException, UserNotFoundException, OrderLineValidationException, ProductNotFoundException {
        if (orderDTO == null) {
            throw new OrderCreationException("Order cannot be null");
        }

        // Walidacja istnienia zamówienia
        if (!orderRepository.existsById(orderDTO.getId())) {
            throw new OrderNotFoundException("Order not found with ID: " + orderDTO.getId());
        }

        // Walidacja danych użytkownika
        if (!userRepository.existsById(orderDTO.getUserId())) {
            throw new UserNotFoundException("User not found with ID: " + orderDTO.getUserId());
        }

        // Walidacja linii zamówienia
        if (orderDTO.getOrderLines() == null || orderDTO.getOrderLines().isEmpty()) {
            throw new OrderLineValidationException("Order must have at least one line item");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderLineDTO line : orderDTO.getOrderLines()) {
            if (!productRepository.existsById(line.getProductId())) {
                throw new ProductNotFoundException("Product not found with ID: " + line.getProductId());
            }

            if (line.getQuantity() <= 0 || line.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new OrderLineValidationException("Quantity and unit price must be positive");
            }

            totalAmount = totalAmount.add(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
        }

        // Walidacja daty zamówienia
        if (orderDTO.getOrderDate() != null && orderDTO.getOrderDate().isAfter(ChronoLocalDateTime.from(LocalDate.now()))) {
            throw new OrderCreationException("Order date cannot be in the future");
        }

        // Walidacja całkowitej kwoty zamówienia
        if (orderDTO.getTotalAmount() == null || orderDTO.getTotalAmount().compareTo(totalAmount) != 0) {
            throw new OrderCreationException("Total amount is not correct");
        }

        OrderModel order = orderMapper.toOrderModel(orderDTO);
        return orderMapper.toOrderDTO(orderRepository.save(order));
    }


    public void deleteOrder(Integer id) throws OrderNotFoundException {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    public BigDecimal calculateOnlinePaymentAmount(OrderModel order) throws PaymentDetailsException {
        if (order == null) {
            throw new PaymentDetailsException("Order cannot be null");
        }
        BigDecimal totalAmount = order.getTotalAmount();
        return totalAmount.multiply(ONLINE_PAYMENT_PERCENTAGE);
    }

    public PaymentDetailsModel preparePaymentDetails(OrderModel order) throws PaymentDetailsException {
        if (order == null) {
            throw new PaymentDetailsException("Order cannot be null");
        }
        BigDecimal onlinePaymentAmount = calculateOnlinePaymentAmount(order);
        PaymentDetailsModel paymentDetails = new PaymentDetailsModel();
        paymentDetails.setReservedAmount(onlinePaymentAmount);
        paymentDetails.setBalanceAmount(order.getTotalAmount().subtract(onlinePaymentAmount));
        paymentDetails.setAmount(order.getTotalAmount());
        paymentDetails.setPaymentMethod(PaymentType.DEPOSIT); // Ustawienie typu płatności
        return paymentDetails;
    }
}
