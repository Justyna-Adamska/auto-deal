package com.example.autodeal.user.controller;

import com.example.autodeal.order.dto.OrderDTO;
import com.example.autodeal.order.service.OrderService;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OrderService orderService;


    //wyświetlanie własnego profilu użytkownika
    @GetMapping("/profile")
    public String userProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserModel user = userService.findUserByEmail(email);
        model.addAttribute("user", user);
        return "user/userProfile";
    }

    //możliwość edycji własnych danych przez użytkownika
    @GetMapping("/edit")
    public String showEditFormFragment(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserModel user = userService.findUserByEmail(email);
        model.addAttribute("user", user);
        return "fragments/editProfileForm";
    }

    @PostMapping("/edit")
    public String saveEditedUser(@ModelAttribute UserModel user, Model model) {
        userService.saveEditUser(user);
        model.addAttribute("user", user);
        model.addAttribute("updateSuccess", true);
        return "redirect:/user/profile";
    }


    //mozliwość aby użytkownik mógł skasować swoje konto
    @PostMapping("/delete/{id}")
    public RedirectView deleteUserAccount(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        UserModel user = userService.findUserById(id);
        if (user != null && user.getEmail().equals(currentUserName)) {
            userService.deleteUser(id);
            return new RedirectView("/login");
        } else {
            return new RedirectView("/errorPage");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders")
    public String viewUserOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserModel user = userService.findUserByEmail(email);
        List<OrderDTO> orders = orderService.findOrdersByUserId(user.getId());

        model.addAttribute("orders", orders);
        return "user/user/userOrders";
    }

}