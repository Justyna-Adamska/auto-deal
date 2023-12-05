package com.example.autodeal.order.mapper;


import com.example.autodeal.order.dto.PaymentDetailsDTO;
import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.model.PaymentDetailsModel;
import com.example.autodeal.order.model.PaymentStatus;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        dto.setPaymentDate(model.getPaymentDate());
        dto.setReservationExpireDate(model.getReservationExpireDate());
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
        model.setPaymentDate(dto.getPaymentDate());
        model.setReservationExpireDate(dto.getReservationExpireDate());
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
