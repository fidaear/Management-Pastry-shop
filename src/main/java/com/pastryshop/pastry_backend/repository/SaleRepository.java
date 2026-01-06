package com.pastryshop.pastry_backend.repository;


import com.pastryshop.pastry_backend.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends MongoRepository<Sale, String> {

    Optional<Sale> findTopByOrderBySaleNumberDesc();

    @Query("{ 'saleDate': { $gte: ?0, $lte: ?1 } }")
    List<Sale> findSalesBetween(LocalDateTime from, LocalDateTime to);
}

