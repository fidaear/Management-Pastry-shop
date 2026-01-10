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
    private double purchasePrice;
    private double sellingPrice;
    private int quantity;
    private String image;

    private int numberOfSales = 0;
    private double totalBenefit = 0;

    private boolean active = true;
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    // ===== getters & setters =====



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
    /* ========================= */
    /* BUSINESS METHODS          */
    /* ========================= */

    public void updateTotalBenefit(int soldQuantity) {
        double benefitPerUnit = sellingPrice - purchasePrice;
        this.totalBenefit += benefitPerUnit * soldQuantity;
    }
}
