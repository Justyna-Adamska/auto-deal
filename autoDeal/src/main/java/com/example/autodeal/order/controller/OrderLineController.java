package com.example.autodeal.order.controller;

import com.example.autodeal.order.model.OrderLineModel;
import com.example.autodeal.order.service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orderlines")
public class OrderLineController {

    private final OrderLineService orderLineService;

    @Autowired
    public OrderLineController(OrderLineService orderLineService) {
        this.orderLineService = orderLineService;
    }

    @PostMapping
    // Tworzy nową linię zamówienia
    public ResponseEntity<OrderLineModel> createOrderLine(@RequestBody OrderLineModel orderLine) {
        return ResponseEntity.ok(orderLineService.createOrderLine(orderLine));
    }

    @GetMapping("/{orderLineId}")
    // Pobiera linię zamówienia na podstawie jej ID
    public ResponseEntity<OrderLineModel> getOrderLineById(@PathVariable Integer orderLineId) {
        Optional<OrderLineModel> orderLine = orderLineService.getOrderLineById(orderLineId);
        return orderLine.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Metody dla administratora

    @PutMapping("/admin/{orderLineId}")
    // Aktualizuje linię zamówienia (dostępne tylko dla admina)
    public ResponseEntity<OrderLineModel> updateOrderLine(@PathVariable Integer orderLineId, @RequestBody OrderLineModel orderLine) {
        orderLine.setId(orderLineId);
        return ResponseEntity.ok(orderLineService.updateOrderLine(orderLine));
    }

    @DeleteMapping("/admin/{orderLineId}")
    // Usuwa linię zamówienia (dostępne tylko dla admina)
    public ResponseEntity<Void> deleteOrderLine(@PathVariable Integer orderLineId) {
        orderLineService.deleteOrderLine(orderLineId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin")
    // Pobiera wszystkie linie zamówień (dostępne tylko dla admina)
    public ResponseEntity<List<OrderLineModel>> getAllOrderLines() {
        return ResponseEntity.ok(orderLineService.getAllOrderLines());
    }

}
