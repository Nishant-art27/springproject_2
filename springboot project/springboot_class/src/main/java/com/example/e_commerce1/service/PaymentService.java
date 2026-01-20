package com.example.e_commerce1.service;

import com.example.e_commerce1.dto.RazorpayOrderResponseDTO;
import com.example.e_commerce1.model.Order;
import com.example.e_commerce1.model.Payment;
import com.example.e_commerce1.repository.OrderRepository;
import com.example.e_commerce1.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(RazorpayClient razorpayClient,
                          OrderRepository orderRepository,
                          PaymentRepository paymentRepository) {
        this.razorpayClient = razorpayClient;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    public RazorpayOrderResponseDTO createRazorpayOrder(String orderId) throws Exception {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if(!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Order already processed or cancelled");
        }

        JSONObject options = new JSONObject();
        options.put("amount", (int) (order.getTotalAmount() * 100));
        options.put("currency", "INR");
        options.put("receipt", orderId);

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(options);

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus("PENDING");
        payment.setPaymentId(razorpayOrder.get("id"));
        
        Payment savedPayment = paymentRepository.save(payment);
        
        order.setPayment(savedPayment);
        orderRepository.save(order);

        RazorpayOrderResponseDTO responseDto = new RazorpayOrderResponseDTO();
        responseDto.setRazorpayOrderId(razorpayOrder.get("id"));
        responseDto.setAmount(razorpayOrder.get("amount"));
        responseDto.setCurrency(razorpayOrder.get("currency"));

        return responseDto;
    }
}
