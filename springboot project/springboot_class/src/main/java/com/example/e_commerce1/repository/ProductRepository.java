package com.example.e_commerce1.repository;


import com.example.e_commerce1.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}

