package com.example.autodeal.order.service;

import com.example.autodeal.exception.OrderCreationException;
import com.example.autodeal.exception.UserNotFoundException;
import com.example.autodeal.order.dto.OrderDTO;
import com.example.autodeal.order.dto.OrderLineDTO;
import com.example.autodeal.order.dto.PaymentDetailsDTO;
import com.example.autodeal.order.mapper.OrderMapper;
import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.repository.OrderRepository;
import com.example.autodeal.product.repository.ProductRepository;
import com.example.autodeal.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderDTO sampleOrderDTO;
    private OrderModel sampleOrderModel;

    @BeforeEach
    void setUp() {
        sampleOrderDTO = new OrderDTO();
        sampleOrderModel = new OrderModel();
    }

    @Test
    void createOrder_NullOrder_ThrowsException() {
        assertThrows(OrderCreationException.class, () -> orderService.createOrder(null));
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        when(userRepository.existsById(any())).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> orderService.createOrder(sampleOrderDTO));
    }

    @Test
    void createOrder_ValidOrder_ReturnsOrderDTO() throws Exception {
        Integer userId = 1;
        BigDecimal unitPrice = new BigDecimal("100.00");
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(1));

        OrderLineDTO orderLineDTO = OrderLineDTO.builder()
                .productId(1)
                .quantity(1)
                .unitPrice(unitPrice)
                .build();

        PaymentDetailsDTO paymentDetailsDTO = new PaymentDetailsDTO();
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .orderLines(Set.of(orderLineDTO))
                .totalAmount(totalAmount)
                .orderDate(LocalDateTime.now())
                .status("NEW")
                .paymentDetails(paymentDetailsDTO)
                .build();

        when(userRepository.existsById(any())).thenReturn(true);
        when(productRepository.existsById(any())).thenReturn(true);
        when(orderMapper.toOrderModel(any(OrderDTO.class))).thenReturn(new OrderModel());
        when(orderRepository.save(any(OrderModel.class))).thenReturn(new OrderModel());
        when(orderMapper.toOrderDTO(any(OrderModel.class))).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertNotNull(result);
        assertEquals(totalAmount, result.getTotalAmount());
    }




}
