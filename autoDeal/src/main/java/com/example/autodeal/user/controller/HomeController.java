package com.example.autodeal.user.controller;

import com.example.autodeal.product.ProductMapper;
import com.example.autodeal.product.dto.ProductDto;
import com.example.autodeal.product.service.ProductService;
import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.service.NotificationService;
import com.example.autodeal.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class HomeController {


    private final UserService userService;
    private final ProductService productService;

    private NotificationService notificationService;

    public HomeController(UserService userService, NotificationService notificationService, ProductService productService) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.productService = productService;
    }


    @GetMapping("/registration")
    public String registration() {
        return "home/registration";
    }

    @PostMapping("/registration")
    public String registerNewUser(SignUpDto signUpDto, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        userService.registerNewUser(signUpDto, response);
        redirectAttributes.addFlashAttribute("success", "User is registered successfully!");
        return "redirect:/registration-success";
    }


    @GetMapping("/registration-success")
    public String registrationSuccess() {
        return "home/registrationSuccess";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        userService.confirmUserRegistration(token);
        return "home/accountVerified";
    }


    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String userRole = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        model.addAttribute("username", username);
        model.addAttribute("userRole", userRole);

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "home/login";
    }


    @GetMapping("/admin")
    public String admin() {
        return "admin/adminDashboard";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "home/forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        String response = notificationService.requestPasswordReset(email);

        if ("SUCCESS".equals(response)) {
            redirectAttributes.addFlashAttribute("message", "Password reset link has been sent to the provided email address.");
        } else {
            redirectAttributes.addFlashAttribute("error", response);
        }
        return "redirect:/login";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("password") String password,
                                      RedirectAttributes redirectAttributes) {
        String response = notificationService.resetPassword(token, password);

        if ("SUCCESS".equals(response)) {
            redirectAttributes.addFlashAttribute("message", "Your password has been reset successfully.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", response);
            return "redirect:/reset-password?token=" + token;
        }
    }

    @GetMapping("/listings")
    public String showCarListings(Model model) {
        List<ProductDto> productDtos = productService.findAllProducts()
                .stream()
                .map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
        model.addAttribute("products", productDtos);
        return "/home/car-listings";
    }
}
