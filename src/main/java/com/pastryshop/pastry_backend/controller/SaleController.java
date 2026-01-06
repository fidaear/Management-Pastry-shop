package com.pastryshop.pastry_backend.controller;


import com.pastryshop.pastry_backend.model.Sale;
import com.pastryshop.pastry_backend.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        return ResponseEntity.ok(saleService.createSale(sale));
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable String id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @GetMapping("/range")
    public ResponseEntity<List<Sale>> getSalesByDateRange(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        return ResponseEntity.ok(
                saleService.getSalesByDateRange(from, to)
        );
    }
}

