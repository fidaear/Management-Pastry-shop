package com.pastryshop.pastry_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String name;
    private String image;

    // Prices
    private double purchasePrice;   // buy price
    private double sellingPrice;    // sell price

    // Stock
    private int quantity;            // available stock

    // Statistics
    private int numberOfSales;       // total sold units
    private double totalBenefit;     // total profit

    /* ========================= */
    /* BUSINESS METHODS          */
    /* ========================= */

    public void updateTotalBenefit(int soldQuantity) {
        double benefitPerUnit = sellingPrice - purchasePrice;
        this.totalBenefit += benefitPerUnit * soldQuantity;
    }
}
