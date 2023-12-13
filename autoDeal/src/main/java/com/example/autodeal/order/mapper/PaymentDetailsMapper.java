package com.example.autodeal.order.mapper;

import com.example.autodeal.order.dto.PaymentDetailsDTO;
import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.model.PaymentDetailsModel;
import com.example.autodeal.order.model.PaymentStatus;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class PaymentDetailsMapper {

    private final OrderRepository orderRepository;

    @Autowired
    public PaymentDetailsMapper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public PaymentDetailsDTO toPaymentDetailsDTO(PaymentDetailsModel model) {
        if (model == null) {
            return null;
        }

        PaymentDetailsDTO dto = new PaymentDetailsDTO();
        dto.setId(model.getId());
        dto.setAmount(model.getAmount());
        dto.setPaymentMethod(model.getPaymentMethod().name());
        dto.setReservedAmount(model.getReservedAmount());
        dto.setBalanceAmount(model.getBalanceAmount());
        if (model.getPaymentDate() != null) {
            dto.setPaymentDate(java.sql.Date.valueOf(model.getPaymentDate()));
        }
        if (model.getReservationExpireDate() != null) {
            dto.setReservationExpireDate(java.sql.Date.valueOf(model.getReservationExpireDate()));
        }
        dto.setTransactionId(model.getTransactionId());
        dto.setStatus(String.valueOf(model.getStatus()));


        if (model.getOrder() != null) {
            dto.setOrderId(model.getOrder().getId());
        }

        return dto;
    }


    public PaymentDetailsModel toPaymentDetailsModel(PaymentDetailsDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("PaymentDetailsDTO cannot be null");
        }

        PaymentDetailsModel model = new PaymentDetailsModel();
        model.setId(dto.getId());
        model.setAmount(dto.getAmount());
        model.setPaymentMethod(PaymentType.valueOf(dto.getPaymentMethod()));
        model.setReservedAmount(dto.getReservedAmount());
        model.setBalanceAmount(dto.getBalanceAmount());

        Date paymentDate = dto.getPaymentDate();
        if (paymentDate != null) {
            Instant instant = Instant.ofEpochMilli(paymentDate.getTime());
            LocalDate paymentLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            model.setPaymentDate(paymentLocalDate);
        } else {
            model.setPaymentDate(null);
        }

        Date expireDate = dto.getReservationExpireDate();
        if (expireDate != null) {
            Instant instant = Instant.ofEpochMilli(expireDate.getTime());
            LocalDate expireLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            model.setReservationExpireDate(expireLocalDate);
        } else {
            model.setReservationExpireDate(null);
        }


        model.setTransactionId(dto.getTransactionId());
        model.setStatus(PaymentStatus.valueOf(dto.getStatus()));

        if (dto.getOrderId() != null) {
            OrderModel order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new IllegalStateException("Order not found with ID: " + dto.getOrderId()));
            model.setOrder(order);
        }

        return model;
    }
}
