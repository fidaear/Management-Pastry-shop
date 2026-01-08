package com.pastryshop.pastry_backend.repository;

import com.pastryshop.pastry_backend.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends MongoRepository<Sale, String> {

    // Find the latest sale by saleNumber (optional)
    Optional<Sale> findTopByOrderBySaleNumberDesc();

    // Find all sales between two dates
    @Query("{'saleDate': {$gte: ?0, $lte: ?1}}")
    List<Sale> findSalesBetween(LocalDateTime from, LocalDateTime to);
    // Find all sales from a given date (for monthly stats)
    @Query("{'saleDate': {$gte: ?0}}")
    List<Sale> findSalesFrom(LocalDateTime from);
}
