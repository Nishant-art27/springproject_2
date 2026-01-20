package com.example.e_commerce1.webhook;

import com.example.e_commerce1.model.Order;
import com.example.e_commerce1.model.Payment;
import com.example.e_commerce1.repository.OrderRepository;
import com.example.e_commerce1.repository.PaymentRepository;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/webhook")
public class PaymentWebhookController {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    public PaymentWebhookController(OrderRepository orderRepository, 
                                   PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/razorpay")
    public String handleRazorpayWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {

        try {
            boolean isValidSignature = Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    webhookSecret
            );

            if (!isValidSignature) {
                return "Invalid webhook signature!";
            }

            JSONObject jsonPayload = new JSONObject(payload);
            String eventType = jsonPayload.getString("event");

            if ("payment.captured".equals(eventType)) {

                String razorpayOrderId = jsonPayload
                        .getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity")
                        .getString("order_id");

                Optional<Payment> paymentOpt = paymentRepository.findAll().stream()
                        .filter(p -> razorpayOrderId.equals(p.getPaymentId()))
                        .findFirst();
                
                if(!paymentOpt.isPresent()) {
                    return "Payment record not found";
                }
                
                Payment payment = paymentOpt.get();
                
                payment.setStatus("SUCCESS");
                paymentRepository.save(payment);
                
                Order order = orderRepository.findById(payment.getOrderId())
                        .orElseThrow(() -> new RuntimeException("Order not found"));
                
                order.setStatus("PAID");
                order.setPayment(payment);
                orderRepository.save(order);
                
                System.out.println("Payment successful for order: " + order.getId());
            }

            return "Webhook processed successfully";

        } catch (Exception e) {
            System.err.println("Error processing webhook: " + e.getMessage());
            return "Webhook processing failed: " + e.getMessage();
        }
    }
}
