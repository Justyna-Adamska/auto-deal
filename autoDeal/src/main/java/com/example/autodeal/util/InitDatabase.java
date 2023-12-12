package com.example.autodeal.util;

import com.example.autodeal.order.model.*;
import com.example.autodeal.order.repository.OrderRepository;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.UserRole;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class InitDatabase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final OrderRepository orderRepository;
    @PostConstruct
    public void  init(){

        UserRole userRole = new UserRole();
        userRole.setName("ROLE_ADMIN");
        UserRole savedAdmin = userRoleRepository.save(userRole);

        UserRole userRole2 = new UserRole();
        userRole2.setName("ROLE_USER");
        UserRole savedAdmin2 = userRoleRepository.save(userRole2);


        UserModel userModel = new UserModel();

        userModel.setFirstName("Marian");
        userModel.setLastName("Kowalski");
        userModel.setPassword(passwordEncoder.encode("abc"));
        userModel.setEmail("jan.kowalski@gmail.com");
        userModel.setPhone("501-151-658");
        userModel.setEnabled(true);
        userModel.setRoles(Set.of(savedAdmin));
        userRepository.save(userModel);



        UserModel userModel2 = new UserModel();

        userModel2.setFirstName("Jan");
        userModel2.setLastName("Kot");
        userModel2.setPassword(passwordEncoder.encode("asd"));
        userModel2.setEmail("jan.kot@gmail.com");
        userModel2.setPhone("501-965-123");
       userModel2.setEnabled(true);
        userModel2.setRoles(Set.of(savedAdmin2));//savedUser
        userRepository.save(userModel2);

        UserModel janKot = userRepository.findByEmail("jan.kot@gmail.com")
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        OrderModel order = new OrderModel();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(janKot);
        order.setStatus(OrderStatus.PROCESSING);

        Set<OrderLineModel> orderLines = new HashSet<>();

        OrderLineModel orderLine = new OrderLineModel();
        orderLine.setOrder(order);
        orderLine.setProductId(1);
        orderLine.setQuantity(1);
        orderLine.setUnitPrice(new BigDecimal("10000.00"));
        orderLines.add(orderLine);
        order.setOrderLines(orderLines);

        PaymentDetailsModel paymentDetails = new PaymentDetailsModel();
        paymentDetails.setAmount(new BigDecimal("10000.00"));
        paymentDetails.setPaymentMethod(PaymentType.ONLINE);
        paymentDetails.setReservedAmount(new BigDecimal("2000.00"));
        paymentDetails.setBalanceAmount(new BigDecimal("8000.00"));
        paymentDetails.setPaymentDate(LocalDate.now());
        paymentDetails.setStatus(PaymentStatus.COMPLETED);
        paymentDetails.setOrder(order);
        order.setPaymentDetails(paymentDetails);

        orderRepository.save(order);

        OrderModel secondOrder = new OrderModel();
        secondOrder.setOrderDate(LocalDateTime.now());
        secondOrder.setUser(janKot);
        secondOrder.setStatus(OrderStatus.PROCESSING);

        Set<OrderLineModel> secondOrderLines = new HashSet<>();
        OrderLineModel secondOrderLine = new OrderLineModel();
        secondOrderLine.setOrder(secondOrder);
        secondOrderLine.setProductId(2);
        secondOrderLine.setQuantity(1);
        secondOrderLine.setUnitPrice(new BigDecimal("5000.00"));
        secondOrderLines.add(secondOrderLine);
        secondOrder.setOrderLines(secondOrderLines);

        PaymentDetailsModel secondPaymentDetails = new PaymentDetailsModel();
        secondPaymentDetails.setAmount(new BigDecimal("5000.00"));
        secondPaymentDetails.setPaymentMethod(PaymentType.DEPOSIT);
        secondPaymentDetails.setReservedAmount(new BigDecimal("1000.00"));
        secondPaymentDetails.setBalanceAmount(new BigDecimal("4000.00"));
        secondPaymentDetails.setPaymentDate(LocalDate.now());
        secondPaymentDetails.setStatus(PaymentStatus.PARTIALLY_PAID);

        secondPaymentDetails.setOrder(secondOrder);
        secondOrder.setPaymentDetails(secondPaymentDetails);

        orderRepository.save(secondOrder);

    }
}
