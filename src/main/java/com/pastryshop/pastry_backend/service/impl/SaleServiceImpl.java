package com.pastryshop.pastry_backend.service.impl;

import com.pastryshop.pastry_backend.model.Product;
import com.pastryshop.pastry_backend.model.Sale;
import com.pastryshop.pastry_backend.model.SaleItem;
import com.pastryshop.pastry_backend.repository.ProductRepository;
import com.pastryshop.pastry_backend.repository.SaleRepository;
import com.pastryshop.pastry_backend.service.SaleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public SaleServiceImpl(SaleRepository saleRepository,
                           ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Sale createSale(Sale sale) {

        /* ========================= */
        /* GENERATE SALE NUMBER      */
        /* ========================= */

        String lastSaleNumber = saleRepository
                .findTopByOrderBySaleNumberDesc()
                .map(Sale::getSaleNumber)
                .orElse("SALE-000");

        int nextNumber = Integer.parseInt(lastSaleNumber.split("-")[1]) + 1;
        sale.setSaleNumber(String.format("SALE-%03d", nextNumber));

        /* ========================= */
        /* PROCESS SALE ITEMS        */
        /* ========================= */

        for (SaleItem item : sale.getItems()) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException("Product not found"));

            // Stock check
            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName());
            }

            // Fill SaleItem snapshot
            item.setProductName(product.getName());
            item.setSellingPrice(product.getSellingPrice());
            item.setPurchasePrice(product.getPurchasePrice());

            /* ========================= */
            /* UPDATE PRODUCT DATA       */
            /* ========================= */

            // Decrease stock
            product.setQuantity(
                    product.getQuantity() - item.getQuantity()
            );

            // Update sales count
            product.setNumberOfSales(
                    product.getNumberOfSales() + item.getQuantity()
            );

            // Update benefit
            product.updateTotalBenefit(item.getQuantity());

            productRepository.save(product);
        }

        /* ========================= */
        /* FINALIZE SALE             */
        /* ========================= */

        sale.calculateTotals();

        if (sale.getPaymentMethod() == null) {
            sale.setPaymentMethod("CASH");
        }

        return saleRepository.save(sale);
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public Sale getSaleById(String id) {
        return saleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Sale not found"));
    }

    @Override
    public List<Sale> getSalesByDateRange(LocalDate from, LocalDate to) {
        return saleRepository.findSalesBetween(
                from.atStartOfDay(),
                to.atTime(23, 59, 59)
        );
    }
}
