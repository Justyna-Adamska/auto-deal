package com.example.autodeal.cart;

import com.example.autodeal.exception.*;
import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.model.PaymentDetailsModel;
import com.example.autodeal.order.model.PaymentType;
import com.example.autodeal.order.service.OrderService;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public CartController(CartService cartService, UserService userService, OrderService orderService) {
        this.cartService = cartService;
        this.userService = userService;
        this.orderService = orderService;
    }


    @GetMapping("")
    public String showCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        String email = authentication.getName();
        UserModel user = userService.findUserByEmail(email);
        if (user == null) {
            model.addAttribute("error", "User not found.");
            return "/cart/error";
        }
        CartModel cart = cartService.getCartForUser(user.getId());
        model.addAttribute("cart", cart);
        return "cart/cart";
    }



    @PostMapping("/add")
    public String addItemToCart(@RequestParam Integer userId, @RequestParam Integer productId, Model model) {
        try {
            cartService.addItemToCart(userId, productId);
            return "redirect:/cart/cart" + userId;
        } catch (CartUpdateException | ProductNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "/cart/error";
        }
    }

    @PostMapping("/remove")
    public String removeItemFromCart(@RequestParam Integer userId, @RequestParam Integer productId) {
        cartService.removeItemFromCart(userId, productId);
        return "redirect:/cart/cart" + userId;
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam Integer userId, @RequestParam PaymentType paymentType, Model model, RedirectAttributes redirectAttributes) {
        try {
            cartService.checkout(userId, paymentType);
            if (paymentType == PaymentType.DEPOSIT) {
                redirectAttributes.addAttribute("userId", userId);
                return "redirect:/cart/order/depositConfirmation";
            } else {
                redirectAttributes.addAttribute("userId", userId);
                return "redirect:/cart/order/completedConfirmation";
            }
        } catch (OrderCreationException | UserNotFoundException | CartNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "/cart/error";
        }
    }

    @GetMapping("/order/depositConfirmation")
    public String showDepositConfirmation(@RequestParam("email") String email, Model model) {
        try {
            UserModel user = userService.findUserByEmail(email);
            if (user == null) {
                throw new UserNotFoundException("User not found.");
            }
            CartModel cart = cartService.getCartForUser(user.getId());
            model.addAttribute("cart", cart);
            return "cart/depositConfirmation";
        } catch (UserNotFoundException | CartNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "/cart/error";
        }
    }

    @GetMapping("/order/completedConfirmation")
    public String showCompletedConfirmation(@RequestParam("userId") Integer userId, Model model) {
        try {
            UserModel user = userService.findUserById(userId);
            if (user == null) {
                throw new UserNotFoundException("User not found.");
            }

            OrderModel order = orderService.getLastOrderOfUser(userId);
            PaymentDetailsModel paymentDetails = orderService.preparePaymentDetails(order);

            model.addAttribute("user", user);
            model.addAttribute("paymentDetails", paymentDetails);
            return "cart/completedConfirmation";
        } catch (UserNotFoundException | PaymentDetailsException e) {
            model.addAttribute("error", e.getMessage());
            return "/cart/error";
        }
    }


}
