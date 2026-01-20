package com.example.e_commerce1.controller;

import com.example.e_commerce1.dto.CreateOrderRequestDTO;
import com.example.e_commerce1.model.Order;
import com.example.e_commerce1.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create order from cart
    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequestDTO dto) {
        return orderService.createOrder(dto.getUserId());
    }

    // Get order by id
    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
