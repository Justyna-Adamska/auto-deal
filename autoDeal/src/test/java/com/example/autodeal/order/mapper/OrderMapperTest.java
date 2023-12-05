package com.example.autodeal.order.mapper;

import com.example.autodeal.order.dto.OrderDTO;
import com.example.autodeal.order.dto.OrderLineDTO;
import com.example.autodeal.order.dto.PaymentDetailsDTO;
import com.example.autodeal.order.model.OrderLineModel;
import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.model.OrderStatus;
import com.example.autodeal.order.model.PaymentDetailsModel;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class OrderMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentDetailsMapper paymentDetailsMapper;

    @InjectMocks
    private OrderMapper orderMapper;

    private OrderModel orderModel;
    private OrderDTO orderDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        UserModel user = new UserModel();
        user.setId(1);

        OrderLineModel orderLineModel = new OrderLineModel();
        orderLineModel.setId(1);
        orderLineModel.setProductId(1);
        orderLineModel.setQuantity(2);
        orderLineModel.setUnitPrice(BigDecimal.valueOf(10.0));
        orderLineModel.setTotalPrice(BigDecimal.valueOf(20.0));
        orderLineModel.setDiscount(BigDecimal.valueOf(5.0));

        Set<OrderLineModel> orderLineModels = new HashSet<>();
        orderLineModels.add(orderLineModel);

        orderModel = new OrderModel();
        orderModel.setId(1);
        orderModel.setUser(user);
        orderModel.setOrderDate(LocalDateTime.now());
        orderModel.setStatus(OrderStatus.NEW);
        orderModel.setOrderLines(orderLineModels);

        Set<OrderLineDTO> orderLineDTOs = new HashSet<>();

        OrderLineDTO orderLineDTO = new OrderLineDTO();
        orderLineDTO.setId(orderLineModel.getId());
        orderLineDTO.setProductId(orderLineModel.getProductId());
        orderLineDTO.setQuantity(orderLineModel.getQuantity());
        orderLineDTO.setUnitPrice(orderLineModel.getUnitPrice());
        orderLineDTO.setTotalPrice(orderLineModel.getTotalPrice());
        orderLineDTO.setDiscount(orderLineModel.getDiscount());
        orderLineDTOs.add(orderLineDTO);

        orderDTO = new OrderDTO();
        orderDTO.setId(1);
        orderDTO.setUserId(user.getId());
        orderDTO.setOrderDate(LocalDateTime.now());
        orderDTO.setStatus("NEW");
        orderDTO.setOrderLines(orderLineDTOs);
        orderDTO.setTotalAmount(BigDecimal.valueOf(20.0));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(paymentDetailsMapper.toPaymentDetailsDTO(any(PaymentDetailsModel.class)))
                .thenReturn(new PaymentDetailsDTO());
        when(paymentDetailsMapper.toPaymentDetailsModel(any(PaymentDetailsDTO.class)))
                .thenReturn(new PaymentDetailsModel());
    }


    @Test
    public void whenToOrderDtoThenCorrect() {
        OrderDTO dto = orderMapper.toOrderDTO(orderModel);
        assertEquals(orderModel.getId(), dto.getId());
        assertEquals(orderModel.getUser().getId(), dto.getUserId());
        assertEquals(orderModel.getStatus().name(), dto.getStatus());
        assertEquals(orderModel.getOrderLines().size(), dto.getOrderLines().size());
    }

    @Test
    public void whenToOrderModelThenCorrect() {
        OrderModel model = orderMapper.toOrderModel(orderDTO);
        assertEquals(orderDTO.getId(), model.getId());
        assertEquals(orderDTO.getUserId(), model.getUser().getId());
        assertEquals(OrderStatus.valueOf(orderDTO.getStatus()), model.getStatus());
        assertEquals(orderDTO.getOrderLines().size(), model.getOrderLines().size());
    }

    @Test
    public void whenToOrderLineDtoThenCorrect() {
        OrderLineModel orderLineModel = orderModel.getOrderLines().iterator().next();
        OrderLineDTO orderLineDTO = orderMapper.toOrderLineDTO(orderLineModel);

        assertEquals(orderLineModel.getId(), orderLineDTO.getId());
        assertEquals(orderLineModel.getProductId(), orderLineDTO.getProductId());
        assertEquals(orderLineModel.getQuantity(), orderLineDTO.getQuantity());
        assertEquals(orderLineModel.getUnitPrice(), orderLineDTO.getUnitPrice());
        assertEquals(orderLineModel.getTotalPrice(), orderLineDTO.getTotalPrice());
        assertEquals(orderLineModel.getDiscount(), orderLineDTO.getDiscount());
    }

    @Test
    public void whenToOrderLineModelThenCorrect() {
        OrderLineDTO orderLineDTO = orderDTO.getOrderLines().iterator().next();
        OrderLineModel orderLineModel = orderMapper.toOrderLineModel(orderLineDTO);

        assertEquals(orderLineDTO.getId(), orderLineModel.getId());
        assertEquals(orderLineDTO.getProductId(), orderLineModel.getProductId());
        assertEquals(orderLineDTO.getQuantity(), orderLineModel.getQuantity());
        assertEquals(0, orderLineDTO.getUnitPrice().compareTo(BigDecimal.valueOf(orderLineModel.getUnitPrice().doubleValue())));
        assertEquals(0, orderLineDTO.getTotalPrice().compareTo(BigDecimal.valueOf(orderLineModel.getTotalPrice().doubleValue())));
        assertEquals(0, orderLineDTO.getDiscount().compareTo(BigDecimal.valueOf(orderLineModel.getDiscount().doubleValue())));
    }
    @Test
    public void whenToOrderModelWithNonExistentUserThenThrowException() {
        OrderDTO dtoWithNonExistentUser = new OrderDTO();
        dtoWithNonExistentUser.setUserId(999); // A non-existent user ID
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            orderMapper.toOrderModel(dtoWithNonExistentUser);
        });

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenToOrderModelNullOrderLinesThenEmptySet() {
        orderDTO.setOrderLines(null);
        OrderModel model = orderMapper.toOrderModel(orderDTO);
        assertTrue(model.getOrderLines().isEmpty());
    }

    @Test
    public void whenUpdateOrderLineThenReflectChanges() {

        OrderLineDTO orderLineDTO = new OrderLineDTO();
        orderLineDTO.setId(1);
        orderLineDTO.setProductId(2);
        orderLineDTO.setQuantity(5);
        orderLineDTO.setUnitPrice(new BigDecimal("15.00"));
        orderLineDTO.setDiscount(new BigDecimal("3.00"));


        OrderLineModel updatedOrderLineModel = orderMapper.toOrderLineModel(orderLineDTO);
        assertEquals(orderLineDTO.getProductId(), updatedOrderLineModel.getProductId());
        assertEquals(orderLineDTO.getQuantity(), updatedOrderLineModel.getQuantity());
        assertEquals(0, orderLineDTO.getUnitPrice().compareTo(BigDecimal.valueOf(updatedOrderLineModel.getUnitPrice().doubleValue())));
        assertEquals(0, orderLineDTO.getDiscount().compareTo(BigDecimal.valueOf(updatedOrderLineModel.getDiscount().doubleValue())));
    }

}
