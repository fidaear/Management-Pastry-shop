package com.pastryshop.pastry_backend.repository;



import com.pastryshop.pastry_backend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}