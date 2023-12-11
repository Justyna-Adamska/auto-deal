package com.example.autodeal.cart;

import com.example.autodeal.exception.UserNotFoundException;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.service.OrderService;
import com.example.autodeal.product.service.ProductService;
import com.example.autodeal.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    private CartService cartService;
    private UserService userService;
    private OrderService orderService;
    private ProductService productService;
    private CartController cartController;
    private Model model;

    @BeforeEach
    public void setUp() {
        cartService = mock(CartService.class);
        userService = mock(UserService.class);
        orderService = mock(OrderService.class);
        model = mock(Model.class);
        productService = mock(ProductService.class);
        cartController = new CartController(cartService, userService, orderService);
    }

    @Test
    public void testGetCart_Success() throws Exception {
        Integer userId = 1;
        CartModel mockCart = new CartModel();
        when(cartService.getCartForUser(userId)).thenReturn(mockCart);

        String viewName = cartController.getCart(userId, model);

        verify(cartService).getCartForUser(userId);
        verify(model).addAttribute("cart", mockCart);
        assertEquals("cart/view", viewName);
    }


    @Test
    public void testGetCart_UserNotFound() throws Exception {
        Integer userId = 1;
        when(cartService.getCartForUser(userId)).thenThrow(new UserNotFoundException("User not found"));

        String viewName = cartController.getCart(userId, model);

        verify(model).addAttribute(eq("error"), anyString());
        assertEquals("error", viewName);
    }

    @Test
    public void testAddItemToCart_Success() throws Exception {
        Integer userId = 1;
        Integer productId = 10;

        doNothing().when(cartService).addItemToCart(userId, productId);

        String viewName = cartController.addItemToCart(userId, productId, model);
        verify(cartService).addItemToCart(userId, productId);

        assertEquals("redirect:/cart/" + userId, viewName);
    }



    @Test
    public void testRemoveItemFromCart_Success() {
        Integer userId = 1;
        Integer productId = 10;

        String viewName = cartController.removeItemFromCart(userId, productId);

        verify(cartService).removeItemFromCart(userId, productId);
        assertEquals("redirect:/cart/" + userId, viewName);
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