package com.example.e_commerce1.repository;

import com.example.e_commerce1.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
