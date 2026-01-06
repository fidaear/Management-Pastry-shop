package com.pastryshop.pastry_backend.service;

import com.pastryshop.pastry_backend.model.Sale;

import java.time.LocalDate;
import java.util.List;

public interface SaleService {

    Sale createSale(Sale sale);

    List<Sale> getAllSales();

    List<Sale> getSalesByDateRange(LocalDate from, LocalDate to);

    Sale getSaleById(String id);
}

