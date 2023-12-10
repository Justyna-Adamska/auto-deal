package com.example.autodeal.order.mapper;

import com.example.autodeal.order.dto.OrderDTO;
import com.example.autodeal.order.dto.OrderLineDTO;
import com.example.autodeal.order.model.OrderLineModel;
import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.model.OrderStatus;
import com.example.autodeal.order.model.PaymentDetailsModel;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class OrderMapper {

    private final UserRepository userRepository;
    private final PaymentDetailsMapper paymentDetailsMapper;

    @Autowired
    public OrderMapper(UserRepository userRepository, PaymentDetailsMapper paymentDetailsMapper) {
        this.userRepository = userRepository;
        this.paymentDetailsMapper = paymentDetailsMapper;
    }
    public OrderDTO toOrderDTO(OrderModel order) {
        if (order == null) {
            throw new IllegalArgumentException("OrderModel cannot be null");
        }

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setUserId(order.getUser().getId());
        dto.setStatus(String.valueOf(order.getStatus()));

        Set<OrderLineDTO> orderLineDTOs = new HashSet<>();
        if (order.getOrderLines() != null) {
            for (OrderLineModel line : order.getOrderLines()) {
                OrderLineDTO lineDTO = toOrderLineDTO(line);
                orderLineDTOs.add(lineDTO);
            }
        }
        dto.setOrderLines(orderLineDTOs);
        dto.setTotalAmount(order.getTotalAmount());

        if (order.getPaymentDetails() != null) {
            dto.setPaymentDetails(paymentDetailsMapper.toPaymentDetailsDTO(order.getPaymentDetails()));
        }
        return dto;
    }

    public OrderModel toOrderModel(OrderDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("OrderDTO cannot be null");
        }

        OrderModel order = new OrderModel();
        order.setId(dto.getId());
        order.setOrderDate(dto.getOrderDate());

        UserModel user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        order.setUser(user);

        if (dto.getStatus() != null) {
            order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        } else {
            order.setStatus(OrderStatus.NEW);
        }

        Set<OrderLineModel> orderLines = new HashSet<>();
        if (dto.getOrderLines() != null) {
            for (OrderLineDTO lineDTO : dto.getOrderLines()) {
                OrderLineModel line = toOrderLineModel(lineDTO);
                line.setOrder(order);
                orderLines.add(line);
            }
        }
        order.setOrderLines(orderLines);

        if (dto.getPaymentDetails() != null) {
            PaymentDetailsModel paymentDetails = paymentDetailsMapper.toPaymentDetailsModel(dto.getPaymentDetails());
            order.setPaymentDetails(paymentDetails);
        }

        return order;
    }


    public OrderLineDTO toOrderLineDTO(OrderLineModel line) {
        if (line == null) {
            throw new IllegalArgumentException("OrderLineModel cannot be null");
        }

        OrderLineDTO dto = new OrderLineDTO();
        dto.setId(line.getId());
        dto.setProductId(line.getProductId());
        dto.setQuantity(line.getQuantity());
        dto.setUnitPrice(line.getUnitPrice());
        dto.setTotalPrice(line.getTotalPrice());
        dto.setDiscount(line.getDiscount());

        return dto;
    }

    public OrderLineModel toOrderLineModel(OrderLineDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("OrderLineDTO cannot be null");
        }

        OrderLineModel line = new OrderLineModel();
        line.setId(dto.getId());
        line.setProductId(dto.getProductId());
        line.setQuantity(dto.getQuantity());
        line.setUnitPrice(dto.getUnitPrice());
        line.setTotalPrice(dto.getTotalPrice());
        line.setDiscount(dto.getDiscount());

        return line;
    }
}
