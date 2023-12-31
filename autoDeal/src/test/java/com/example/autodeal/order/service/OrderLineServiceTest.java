package com.example.autodeal.order.service;

import com.example.autodeal.order.dto.OrderDTO;
import com.example.autodeal.order.dto.OrderLineDTO;
import com.example.autodeal.order.mapper.OrderMapper;
import com.example.autodeal.order.model.OrderLineModel;
import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.repository.OrderLineRepository;
import com.example.autodeal.order.repository.OrderRepository;
import com.example.autodeal.exception.OrderLineValidationException;
import com.example.autodeal.product.repository.ProductRepository;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OrderLineServiceTest {

    @Mock
    private OrderLineRepository orderLineRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderLineService orderLineService;

    @InjectMocks
    private OrderService orderService;

    private OrderLineDTO validOrderLineDTO;
    private OrderLineDTO invalidOrderLineDTO;
    private OrderLineModel orderLineModel;

    @Before
    public void setUp() {
        // Ustawianie poprawnego DTO
        validOrderLineDTO = new OrderLineDTO();
        validOrderLineDTO.setId(1);
        validOrderLineDTO.setProductId(1);
        validOrderLineDTO.setQuantity(10);
        validOrderLineDTO.setUnitPrice(new BigDecimal("100.00"));

        // Ustawianie niepoprawnego DTO
        invalidOrderLineDTO = new OrderLineDTO();
        invalidOrderLineDTO.setProductId(-1);
        invalidOrderLineDTO.setQuantity(-10);
        invalidOrderLineDTO.setUnitPrice(new BigDecimal("-100.00"));

        // Ustawienie modelu
        orderLineModel = new OrderLineModel();
        orderLineModel.setId(1);

        when(orderMapper.toOrderLineModel(any(OrderLineDTO.class))).thenReturn(orderLineModel);
        when(orderMapper.toOrderLineDTO(any(OrderLineModel.class))).thenReturn(validOrderLineDTO);
        when(productRepository.existsById(validOrderLineDTO.getProductId())).thenReturn(true);
    }

    @Test
    public void createOrderLine_ValidData_ShouldPass() {
        OrderLineModel savedOrderLine = new OrderLineModel();
        savedOrderLine.setId(1);
        when(orderLineRepository.save(any(OrderLineModel.class))).thenReturn(savedOrderLine);

        OrderLineDTO result = orderLineService.createOrderLine(validOrderLineDTO);
        assertNotNull(result);
        assertEquals(validOrderLineDTO.getId(), result.getId());
    }

    @Test(expected = OrderLineValidationException.class)
    public void createOrderLine_InvalidData_ShouldThrowException() {
        orderLineService.createOrderLine(invalidOrderLineDTO);
    }

    @Test
    public void getOrderLineById_ExistingId_ShouldReturnOrderLine() {
        when(orderLineRepository.findById(1)).thenReturn(Optional.of(orderLineModel));
        Optional<OrderLineDTO> result = orderLineService.getOrderLineById(1);

        assertTrue(result.isPresent());
        assertEquals(validOrderLineDTO.getId(), result.get().getId());
    }

    @Test
    public void getOrderLineById_NonExistingId_ShouldReturnEmpty() {
        when(orderLineRepository.findById(anyInt())).thenReturn(Optional.empty());
        Optional<OrderLineDTO> result = orderLineService.getOrderLineById(1);

        assertFalse(result.isPresent());
    }

    @Test
    public void updateOrderLine_ValidData_ShouldUpdateAndReturnOrderLine() {
        when(orderLineRepository.existsById(validOrderLineDTO.getId())).thenReturn(true);
        when(orderLineRepository.save(any(OrderLineModel.class))).thenReturn(orderLineModel);

        OrderLineDTO result = orderLineService.updateOrderLine(validOrderLineDTO);

        assertNotNull(result);
        assertEquals(validOrderLineDTO.getId(), result.getId());
    }

    @Test(expected = OrderLineValidationException.class)
    public void updateOrderLine_NonExistingId_ShouldThrowException() {
        when(orderLineRepository.existsById(anyInt())).thenReturn(false);
        orderLineService.updateOrderLine(validOrderLineDTO);
    }
    @Test
    public void deleteOrderLine_ExistingId_ShouldDeleteSuccessfully() {
        when(orderLineRepository.existsById(1)).thenReturn(true);
        doNothing().when(orderLineRepository).deleteById(1);

        orderLineService.deleteOrderLine(1);

        verify(orderLineRepository).deleteById(1);
    }

    @Test(expected = OrderLineValidationException.class)
    public void deleteOrderLine_NonExistingId_ShouldThrowException() {
        when(orderLineRepository.existsById(anyInt())).thenReturn(false);
        orderLineService.deleteOrderLine(1);
    }
    @Test
   public void findOrdersByUserId_WhenNoOrders_ShouldReturnEmptyList() {
        Integer userId = 1;
        when(orderRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        List<OrderDTO> result = orderService.findOrdersByUserId(userId);

        assertTrue(result.isEmpty());
    }

    @Test
   public void findOrdersByUserId_WhenOrdersExist_ShouldReturnOrderList() {
        Integer userId = 1;
        OrderModel mockOrder = mock(OrderModel.class);
        OrderDTO mockOrderDTO = mock(OrderDTO.class);

        when(orderRepository.findAllByUserId(userId)).thenReturn(Collections.singletonList(mockOrder));
        when(orderMapper.toOrderDTO(mockOrder)).thenReturn(mockOrderDTO);

        List<OrderDTO> result = orderService.findOrdersByUserId(userId);

        assertFalse(result.isEmpty());
        assertEquals(mockOrderDTO, result.get(0));
    }
}
