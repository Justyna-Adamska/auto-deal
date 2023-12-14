package com.example.autodeal.cart;

import com.example.autodeal.exception.UserNotFoundException;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.service.OrderService;
import com.example.autodeal.product.service.ProductService;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    private ProductService productService;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        productService = mock(ProductService.class);
        cartController = new CartController(cartService, userService, orderService);
    }

    @Test
    void whenShowCartCalledAuthenticated_thenReturnsCartView() {
        String email = "user@example.com";
        Integer userId = 1;
        CartModel cartModel = new CartModel();
        UserModel user = new UserModel();
        user.setId(userId);
        user.setEmail(email);

        when(authentication.getName()).thenReturn(email);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userService.findUserByEmail(email)).thenReturn(user);
        when(cartService.getCartForUser(userId)).thenReturn(cartModel);

        String viewName = cartController.showCart(model);

        verify(model).addAttribute("cart", cartModel);
        assertEquals("cart/cart", viewName);
    }



    @Test
    public void testAddItemToCart_Success() throws Exception {
        Integer userId = 1;
        Integer productId = 10;

        doNothing().when(cartService).addItemToCart(userId, productId);

        String viewName = cartController.addItemToCart(userId, productId, model);
        verify(cartService).addItemToCart(userId, productId);

        assertEquals("redirect:/cart/cart" + userId, viewName);
    }

    @Test
    public void testRemoveItemFromCart_Success() {
        Integer userId = 1;
        Integer productId = 10;

        String viewName = cartController.removeItemFromCart(userId, productId);

        verify(cartService).removeItemFromCart(userId, productId);
        assertEquals("redirect:/cart/cart" + userId, viewName);
    }

    @Test
    public void testCheckout_Success_Deposit() throws Exception {
        Integer userId = 1;
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String viewName = cartController.checkout(userId, PaymentType.DEPOSIT, model, redirectAttributes);
        verify(cartService).checkout(userId, PaymentType.DEPOSIT);
        assertEquals("redirect:/cart/order/depositConfirmation", viewName);
    }
}
