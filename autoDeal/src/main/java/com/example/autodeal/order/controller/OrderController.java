package com.example.autodeal.order.controller;

import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    // Tworzy nowe zamówienie
    public ResponseEntity<OrderModel> createOrder(@RequestBody OrderModel order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping("/{orderId}")
    // Pobiera zamówienie na podstawie jego ID
    public ResponseEntity<OrderModel> getOrderById(@PathVariable Integer orderId) {
        Optional<OrderModel> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Metody dla administratora

    @PutMapping("/admin/{orderId}")
    // Aktualizuje zamówienie (dostępne tylko dla admina)
    public ResponseEntity<OrderModel> updateOrder(@PathVariable Integer orderId, @RequestBody OrderModel order) {
        order.setId(orderId);
        return ResponseEntity.ok(orderService.updateOrder(order));
    }

    @DeleteMapping("/admin/{orderId}")
    // Usuwa zamówienie (dostępne tylko dla admina)
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin")
    // Pobiera wszystkie zamówienia (dostępne tylko dla admina)
    public ResponseEntity<List<OrderModel>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

}

