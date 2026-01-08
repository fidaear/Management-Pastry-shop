package com.pastryshop.pastry_backend.repository;

import com.pastryshop.pastry_backend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByName(String name);


    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByQuantityLessThan(Integer quantity);

    List<Product> findByIsActive(Boolean isActive);

    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    List<Product> searchByName(String keyword);

    @Query("{'quantity': {$lt: ?0}}")
    List<Product> findLowStock(Integer threshold);

    @Query(value = "{}", sort = "{'numberOfSales': -1}")
    List<Product> findTopSellingProducts();
}
