package com.example.autodeal.order.controller;

import com.example.autodeal.order.dto.PaymentDetailsDTO;
import com.example.autodeal.order.service.PaymentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/payment-details")
@Secured("ROLE_ADMIN")
public class PaymentDetailsController {

    private final PaymentDetailsService paymentDetailsService;

    @Autowired
    public PaymentDetailsController(PaymentDetailsService paymentDetailsService) {
        this.paymentDetailsService = paymentDetailsService;
    }

    @PostMapping
    public String createPaymentDetails(@ModelAttribute PaymentDetailsDTO paymentDetailsDTO, Model model) {
        PaymentDetailsDTO createdPaymentDetails = paymentDetailsService.createPaymentDetails(paymentDetailsDTO);
        model.addAttribute("paymentDetails", createdPaymentDetails);
        return "paymentDetailsView";
    }

    @GetMapping("/{id}")
    public String getPaymentDetailsById(@PathVariable Integer id, Model model) {
        paymentDetailsService.getPaymentDetailsById(id).ifPresentOrElse(
                paymentDetails -> model.addAttribute("paymentDetails", paymentDetails),
                () -> model.addAttribute("error", "Payment details not found"));
        return "paymentDetailsView";
    }

    @GetMapping
    public String getAllPaymentDetails(Model model) {
        List<PaymentDetailsDTO> allPaymentDetails = paymentDetailsService.getAllPaymentDetails();
        model.addAttribute("paymentDetailsList", allPaymentDetails);
        return "paymentDetailsListView";
    }

}
