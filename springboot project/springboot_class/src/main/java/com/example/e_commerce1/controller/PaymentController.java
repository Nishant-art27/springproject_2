package com.example.e_commerce1.controller;

import com.example.e_commerce1.dto.RazorpayOrderResponseDTO;
import com.example.e_commerce1.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/razorpay/{orderId}")
    public RazorpayOrderResponseDTO createPayment(
            @PathVariable String orderId) throws Exception {

        return paymentService.createRazorpayOrder(orderId);
    }
}
