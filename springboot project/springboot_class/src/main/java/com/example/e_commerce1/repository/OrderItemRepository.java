package com.example.e_commerce1.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.e_commerce1.model.OrderItem;

public interface OrderItemRepository extends MongoRepository<OrderItem, String> {
    
    List<OrderItem> findByOrderId(String orderId);
}
