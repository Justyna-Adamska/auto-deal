package com.example.autodeal.order.mapper;

import com.example.autodeal.order.dto.PaymentDetailsDTO;
import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.model.PaymentDetailsModel;
import com.example.autodeal.order.model.PaymentStatus;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PaymentDetailsMapperTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentDetailsMapper mapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToPaymentDetailsDTO() {

        PaymentDetailsModel model = new PaymentDetailsModel();
        model.setId(1);
        model.setAmount(new BigDecimal("10000.00"));
        model.setPaymentMethod(PaymentType.DEPOSIT);
        model.setReservedAmount(new BigDecimal("2000.00"));
        model.setBalanceAmount(new BigDecimal("8000.00"));
        model.setPaymentDate(LocalDate.of(2026, 12, 10));
        model.setReservationExpireDate(LocalDate.of(2026, 12, 15));
        model.setTransactionId("TRANS123");
        model.setStatus(PaymentStatus.COMPLETED);
        OrderModel order = new OrderModel();
        order.setId(2);
        model.setOrder(order);
        PaymentDetailsDTO dto = mapper.toPaymentDetailsDTO(model);

        assertNotNull(dto);
        assertNotNull(dto.getOrderId());
        assertEquals(model.getOrder().getId(), dto.getOrderId());
    }

    @Test
    public void testToPaymentDetailsModel() {

        PaymentDetailsDTO dto = new PaymentDetailsDTO();
        dto.setId(1);
        dto.setAmount(new BigDecimal("10000.00"));
        dto.setPaymentMethod("DEPOSIT");
        dto.setReservedAmount(new BigDecimal("2000.00"));
        dto.setBalanceAmount(new BigDecimal("8000.00"));
        dto.setPaymentDate(new Date());
        dto.setReservationExpireDate(new Date());
        dto.setTransactionId("TRANS123");
        dto.setStatus("COMPLETED");
        dto.setOrderId(2);

        OrderModel mockOrder = new OrderModel();
        mockOrder.setId(dto.getOrderId());

        when(orderRepository.findById(dto.getOrderId())).thenReturn(Optional.of(mockOrder));

        PaymentDetailsModel model = mapper.toPaymentDetailsModel(dto);

        assertNotNull(model);
        assertNotNull(model.getOrder());
        assertEquals(dto.getOrderId(), model.getOrder().getId());
    }


    @Test
    public void testToPaymentDetailsDTOWithNullModel() {
        PaymentDetailsDTO dto = mapper.toPaymentDetailsDTO(null);
        assertNull(dto);
    }


}
