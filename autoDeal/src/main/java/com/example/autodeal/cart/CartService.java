package com.example.autodeal.cart;

import com.example.autodeal.exception.*;
import com.example.autodeal.order.dto.OrderDTO;
import com.example.autodeal.order.dto.OrderLineDTO;
import com.example.autodeal.order.dto.PaymentDetailsDTO;
import com.example.autodeal.order.mapper.OrderMapper;
import com.example.autodeal.order.model.PaymentStatus;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.service.OrderService;
import com.example.autodeal.order.service.PaymentDetailsService;
import com.example.autodeal.product.dto.ProductDto;
import com.example.autodeal.product.mapper.ProductMapper;
import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.product.service.ProductService;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentDetailsService paymentDetailsService;
    private final UserService userService;
    private final OrderMapper orderMapper;


    @Autowired
    public CartService(CartRepository cartRepository, ProductService productService, OrderService orderService, PaymentDetailsService paymentDetailsService, UserService userService, OrderMapper orderMapper) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.orderService = orderService;
        this.paymentDetailsService = paymentDetailsService;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    public CartModel getCartForUser(Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("No authentication found.");
        }

        UserModel user = userService.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }

        return cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException("Cart not found for user with ID: " + userId));
    }

    public void addItemToCart(Integer userId, Integer productId) throws CartUpdateException, ProductNotFoundException {
        ProductModel productModel = productService.findProductById(productId);
        if (productModel == null) {
            throw new ProductNotFoundException("Product with ID " + productId + " not found.");
        }

        CartModel cart = getCartForUser(userId);
        ProductDto productDto = ProductMapper.mapToProductDto(productModel);

        cart.addItem(productDto);

        boolean productAdded = cart.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(Long.valueOf(productDto.getId())));
        if (!productAdded) {
            throw new CartUpdateException("Error adding product to cart");
        }

        cartRepository.save(cart);
    }

    public void removeItemFromCart(Integer userId, Integer productId) {
        CartModel cart = getCartForUser(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId.longValue()));
        cartRepository.save(cart);
    }

    public void checkout(Integer userId, PaymentType paymentType) throws OrderCreationException, UserNotFoundException, CartNotFoundException {
        CartModel cart = getCartForUser(userId);

        OrderDTO orderDTO = createOrderDTO(userId, cart);

        PaymentDetailsDTO paymentDetails = createPaymentDetails(paymentType, orderDTO);
        processPayment(paymentDetails, orderDTO);

        orderService.createOrder(orderDTO);
        clearCartOnLogout(userId);
    }

    private OrderDTO createOrderDTO(Integer userId, CartModel cart) {
        return OrderDTO.builder()
                .userId(userId)
                .orderDate(LocalDate.now().atStartOfDay())
                .orderLines(cart.getItems().stream()
                        .map(item -> OrderLineDTO.builder()
                                .productId(item.getProductId().intValue())
                                .unitPrice(item.getPrice())
                                .quantity(1)
                                .totalPrice(item.getPrice())
                                .build())
                        .collect(Collectors.toSet()))
                .totalAmount(cart.calculateTotal())
                .build();
    }


    private PaymentDetailsDTO createPaymentDetails(PaymentType paymentType, OrderDTO orderDTO) {
        BigDecimal amount;
        String paymentStatus;

        if (paymentType == PaymentType.DEPOSIT) {

            amount = orderService.calculateOnlinePaymentAmount(orderMapper.toOrderModel(orderDTO));
            paymentStatus = PaymentStatus.PARTIALLY_PAID.toString();
        } else {
            amount = orderDTO.getTotalAmount();
            paymentStatus = PaymentStatus.COMPLETED.toString();
        }

        PaymentDetailsDTO paymentDetails = new PaymentDetailsDTO();
        paymentDetails.setAmount(amount);
        paymentDetails.setPaymentMethod(paymentType.toString());
        paymentDetails.setStatus(paymentStatus);

        return paymentDetails;
    }


    private void processPayment(PaymentDetailsDTO paymentDetails, OrderDTO orderDTO) {

    }

    public void clearCartOnLogout(Integer userId) {
        cartRepository.deleteByUserId(userId);
    }
}
