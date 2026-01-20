package com.example.e_commerce1.service;

import com.example.e_commerce1.exception.EmptyCartException;
import com.example.e_commerce1.exception.InsufficientStockException;
import com.example.e_commerce1.exception.OrderNotFoundException;
import com.example.e_commerce1.exception.ProductNotFoundException;
import com.example.e_commerce1.model.*;
import com.example.e_commerce1.repository.CartRepository;
import com.example.e_commerce1.repository.OrderRepository;
import com.example.e_commerce1.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(String userId) {

        List<CartItem> userCartItems = cartRepository.findByUserId(userId);

        if (userCartItems.isEmpty()) {
            throw new EmptyCartException("Cart is empty! Cannot create order.");
        }

        validateStockAvailability(userCartItems);

        List<OrderItem> orderItems = new ArrayList<>();
        double orderTotalAmount = 0.0;

        for (CartItem cartItem : userCartItems) {
            Product product = productRepository.findById(cartItem.getProductId()).get();

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            double itemSubtotal = product.getPrice() * cartItem.getQuantity();
            orderTotalAmount += itemSubtotal;
            orderItems.add(orderItem);
            
            int updatedStock = product.getStock() - cartItem.getQuantity();
            product.setStock(updatedStock);
            productRepository.save(product);
        }

        Order customerOrder = new Order();
        customerOrder.setUserId(userId);
        customerOrder.setItems(orderItems);
        customerOrder.setTotalAmount(orderTotalAmount);
        customerOrder.setStatus("CREATED");

        Order savedOrder = orderRepository.save(customerOrder);
        
        cartRepository.deleteByUserId(userId);

        return savedOrder;
    }
    
    private void validateStockAvailability(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Product prod = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + item.getProductId()));
            
            if (prod.getStock() < item.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + prod.getName() + 
                    ". Available: " + prod.getStock() + ", Required: " + item.getQuantity());
            }
        }
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
