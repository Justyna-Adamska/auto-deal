package com.example.autodeal.cart;

import com.example.autodeal.exception.CartNotFoundException;
import com.example.autodeal.exception.ProductNotFoundException;
import com.example.autodeal.exception.UserNotFoundException;
import com.example.autodeal.order.dto.OrderDTO;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.service.OrderService;
import com.example.autodeal.order.service.PaymentDetailsService;
import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.product.service.ProductService;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    private CartService cartService;
    private CartRepository cartRepository;
    private ProductService productService;
    private OrderService orderService;
    private PaymentDetailsService paymentDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        productService = mock(ProductService.class);
        orderService = mock(OrderService.class);
        paymentDetailsService = mock(PaymentDetailsService.class);
        userService = mock(UserService.class);
        cartService = new CartService(cartRepository, productService, orderService, paymentDetailsService, userService, null);
    }

    private void setupAuthenticatedUser(Integer userId, String role) {
        Authentication auth = new UsernamePasswordAuthenticationToken(userId.toString(), null, Collections.singletonList(new SimpleGrantedAuthority(role)));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        UserModel mockUser = new UserModel();
        mockUser.setId(userId);
        when(userService.findUserById(userId)).thenReturn(mockUser);
    }

    @Test
    void getCartForUser_WhenAuthenticatedUserMatches_ShouldReturnCart() {
        Integer userId = 1;
        setupAuthenticatedUser(userId, "ROLE_USER");
        CartModel expectedCart = new CartModel(userId);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(expectedCart));
        CartModel result = cartService.getCartForUser(userId);
        assertEquals(expectedCart, result);
    }

    @Test
    void addItemToCart_WhenProductNotFound_ShouldThrowException() {
        Integer userId = 1;
        Integer productId = 1;
        setupAuthenticatedUser(userId, "ROLE_USER");
        when(productService.findProductById(productId)).thenReturn(null);
        assertThrows(ProductNotFoundException.class, () -> cartService.addItemToCart(userId, productId));
    }

    @Test
    void removeItemFromCart_WhenProductNotInCart_ShouldNotRemoveAnyItem() {
        Integer userId = 1;
        setupAuthenticatedUser(userId, "ROLE_USER");
        Integer productId = 1;
        CartModel mockCart = new CartModel(userId);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));
        cartService.removeItemFromCart(userId, productId);
        assertTrue(mockCart.getItems().isEmpty());
        verify(cartRepository, never()).delete(any(CartModel.class));
    }

    @Test
    void checkout_WithValidPaymentType_ShouldProcessOrder() {
        Integer userId = 123;
        setupAuthenticatedUser(userId, "ROLE_USER");
        CartModel mockCart = new CartModel(userId);
        PaymentType mockPaymentType = PaymentType.ONLINE;
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));
        assertDoesNotThrow(() -> cartService.checkout(userId, mockPaymentType));
        verify(orderService).createOrder(any(OrderDTO.class));
    }

    @Test
    void addItemToCart_WhenProductDoesNotExist_ShouldThrowException() {
        Integer userId = 1;
        Integer productId = 3;
        setupAuthenticatedUser(userId, "ROLE_USER");
        when(productService.findProductById(productId)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> cartService.addItemToCart(userId, productId));
    }
    @Test
    void getCartForUser_CartNotFound_ShouldThrowException() {
        Integer userId = 1;
        setupAuthenticatedUser(userId, "ROLE_USER");
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        assertThrows(CartNotFoundException.class, () -> cartService.getCartForUser(userId));
    }

    @Test
    void getCartForUser_NoAuthentication_ShouldThrowException() {
        SecurityContextHolder.getContext().setAuthentication(null);
        Integer userId = 1;
        assertThrows(UserNotFoundException.class, () -> cartService.getCartForUser(userId));
    }


    @Test
    void clearCartOnLogout_ShouldRemoveCart() {
        Integer userId = 1;
        setupAuthenticatedUser(userId, "ROLE_USER");
        cartService.clearCartOnLogout(userId);
        verify(cartRepository).deleteByUserId(userId);
    }
}
