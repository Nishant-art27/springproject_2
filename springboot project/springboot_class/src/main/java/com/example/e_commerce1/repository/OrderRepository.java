package com.example.e_commerce1.repository;

import com.example.e_commerce1.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
