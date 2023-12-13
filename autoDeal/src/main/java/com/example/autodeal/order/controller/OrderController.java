package com.example.autodeal.order.controller;

import com.example.autodeal.exception.OrderNotFoundException;
import com.example.autodeal.order.dto.OrderDTO;
import com.example.autodeal.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
@Secured("ROLE_ADMIN")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable("id") Integer id, Model model) {
        OrderDTO order = orderService.getOrderById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
        model.addAttribute("order", order);
        return "orders/view";
    }

    @GetMapping("/new")
    public String newOrderForm(Model model) {
        model.addAttribute("order", new OrderDTO());
        return "orders/new";
    }

    @PostMapping
    public String createOrder(@ModelAttribute OrderDTO orderDTO) {
        orderService.createOrder(orderDTO);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String editOrderForm(@PathVariable("id") Integer id, Model model) {
        OrderDTO order = orderService.getOrderById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
        model.addAttribute("order", order);
        return "orders/edit";
    }

    @PostMapping("/{id}")
    public String updateOrder(@PathVariable("id") Integer id, @ModelAttribute OrderDTO orderDTO) {
        orderService.updateOrder(orderDTO);
        return "redirect:/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }

    @GetMapping("/listFragment")
    public String listOrdersFragment(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders/list :: ordersListFragment"; // Zwróć tylko fragment strony z listą zamówień
    }

}
