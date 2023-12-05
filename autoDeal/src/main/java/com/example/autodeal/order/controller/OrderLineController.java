package com.example.autodeal.order.controller;

import com.example.autodeal.order.dto.OrderLineDTO;
import com.example.autodeal.order.service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orderlines")
public class OrderLineController {

    private final OrderLineService orderLineService;

    @Autowired
    public OrderLineController(OrderLineService orderLineService) {
        this.orderLineService = orderLineService;
    }

    @PostMapping
    public String createOrderLine(@ModelAttribute OrderLineDTO orderLineDTO, RedirectAttributes redirectAttributes) {
        OrderLineDTO created = orderLineService.createOrderLine(orderLineDTO);
        redirectAttributes.addFlashAttribute("success", "Order line created successfully!");
        return "redirect:/orderlines/" + created.getId();
    }

    @GetMapping("/{orderLineId}")
    public String getOrderLineById(@PathVariable Integer orderLineId, Model model) {
        Optional<OrderLineDTO> orderLineDTO = orderLineService.getOrderLineById(orderLineId);
        if (orderLineDTO.isPresent()) {
            model.addAttribute("orderLine", orderLineDTO.get());
            return "orderLine/orderLineView";
        } else {
            return "error/errorView";
        }
    }

    @PutMapping("/admin/{orderLineId}")
    public String updateOrderLine(@PathVariable Integer orderLineId, @ModelAttribute OrderLineDTO orderLineDTO, RedirectAttributes redirectAttributes) {
        orderLineDTO.setId(orderLineId);
        OrderLineDTO updated = orderLineService.updateOrderLine(orderLineDTO);
        redirectAttributes.addFlashAttribute("success", "Order line updated successfully!");
        return "redirect:/orderlines/admin/" + updated.getId();
    }

    @DeleteMapping("/admin/{orderLineId}")
    public String deleteOrderLine(@PathVariable Integer orderLineId, RedirectAttributes redirectAttributes) {
        orderLineService.deleteOrderLine(orderLineId);
        redirectAttributes.addFlashAttribute("success", "Order line deleted successfully!");
        return "redirect:/orderlines/admin";
    }

    @GetMapping("/admin")
    public String getAllOrderLines(Model model) {
        List<OrderLineDTO> allOrderLinesDTO = orderLineService.getAllOrderLines();
        model.addAttribute("orderLinesList", allOrderLinesDTO);
        return "admin/allOrderLinesView";
    }
}
