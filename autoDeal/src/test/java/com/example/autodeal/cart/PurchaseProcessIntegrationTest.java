package com.example.autodeal.cart;

import com.example.autodeal.AutoDealApplication;
import com.example.autodeal.order.dto.OrderDTO;
import com.example.autodeal.order.dto.PaymentDetailsDTO;
import com.example.autodeal.order.mapper.OrderMapper;
import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.model.PaymentStatus;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.service.OrderService;
import com.example.autodeal.order.service.PaymentDetailsService;
import com.example.autodeal.product.enums.ProductType;
import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.product.repository.ProductRepository;
import com.example.autodeal.product.service.ProductService;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.UserRole;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.UserRoleRepository;
import com.example.autodeal.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest (classes = AutoDealApplication.class)
@WithMockUser(username = "testuser@example.com", roles = "USER")
@Transactional
@ActiveProfiles("test")
public class PurchaseProcessIntegrationTest {

    private static final BigDecimal ONLINE_PAYMENT_PERCENTAGE = BigDecimal.valueOf(0.20f);

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderMapper orderMapper;

    private UserModel testUser;
    private ProductModel testProduct;

    @BeforeEach
    public void setup() {

        UserRole userRole = userRoleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    UserRole newUserRole = new UserRole();
                    newUserRole.setName("ROLE_USER");
                    return userRoleRepository.save(newUserRole);
                });

        testUser = new UserModel();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setEmail("testuser@example.com");
        testUser.setPhone("1234567890");
        testUser.setLastLoginDate(LocalDateTime.now());
        testUser.setEnabled(true);

        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);
        testUser.setRoles(roles);

        userRepository.save(testUser);
        UserModel savedUser = userRepository.save(testUser);
        Integer userId = savedUser.getId();

        CartModel testCart = new CartModel();
        testCart.setUserId(userId);
        cartRepository.save(testCart);


        testProduct = ProductModel.builder()
                .name("Test Car")
                .price(10000)
                .carMake("Test Make")
                .mileage(50000)
                .origin("Test Country")
                .type(ProductType.SEDAN)
                .code(123456789L)
                .color("Red")
                .warranty(12)
                .productionYear(2020)
                .build();

        productRepository.save(testProduct);
    }
    @AfterEach
    public void cleanup() {
        if (testProduct != null && testProduct.getId() != null) {
            productRepository.deleteById(testProduct.getId());
        }
        if (testUser != null && testUser.getId() != null) {
            cartRepository.deleteByUserId(testUser.getId());
            userRoleRepository.deleteById(testUser.getId());
            userRepository.deleteById(testUser.getId());

        }
    }
    @Test
    public void testCompletePurchaseProcess() {

        cartService.addItemToCart(testUser.getId(), testProduct.getId());

        cartService.checkout(testUser.getId(), PaymentType.ONLINE);

        OrderModel orderModel = orderService.getLastOrderOfUser(testUser.getId());
        assertNotNull(orderModel);
        OrderDTO orderDTO = orderMapper.toOrderDTO(orderModel);
        assertFalse(orderDTO.getOrderLines().isEmpty());

        PaymentDetailsDTO paymentDetailsDTO = paymentDetailsService.getPaymentDetailsById(orderDTO.getId()).orElse(null);
        assertNotNull(paymentDetailsDTO);
        assertEquals(PaymentStatus.PENDING, PaymentStatus.valueOf(paymentDetailsDTO.getStatus()));

    }

    @Test
    public void testCompletePurchaseProcessWithDepositPayment() {

        cartService.addItemToCart(testUser.getId(), testProduct.getId());

        cartService.checkout(testUser.getId(), PaymentType.DEPOSIT);

        OrderModel orderModel = orderService.getLastOrderOfUser(testUser.getId());
        assertNotNull(orderModel);
        OrderDTO orderDTO = orderMapper.toOrderDTO(orderModel);
        assertFalse(orderDTO.getOrderLines().isEmpty());

        BigDecimal expectedDepositAmount = orderDTO.getTotalAmount()
                .multiply(ONLINE_PAYMENT_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);

        PaymentDetailsDTO paymentDetailsDTO = paymentDetailsService.getPaymentDetailsById(orderDTO.getId()).orElse(null);
        assertNotNull(paymentDetailsDTO);
        BigDecimal actualDepositAmount = paymentDetailsDTO.getReservedAmount().setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedDepositAmount, actualDepositAmount);

        assertEquals(PaymentStatus.PENDING, PaymentStatus.valueOf(paymentDetailsDTO.getStatus()));
    }


}