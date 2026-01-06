package com.pastryshop.pastry_backend.model;




import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "sales")
public class Sale {

    @Id
    private String id;

    @Field("sale_number")
    private String saleNumber;

    @Field("items")
    private List<SaleItem> items;

    @Field("total_amount")
    private Double totalAmount;

    @Field("total_benefit")
    private Double totalBenefit;

    @Field("payment_method")
    private String paymentMethod; // CASH, CARD

    @Field("sale_date")
    private LocalDateTime saleDate;

    public Sale() {
        this.saleDate = LocalDateTime.now();
    }

    public void calculateTotals() {
        this.totalAmount = items.stream()
                .mapToDouble(SaleItem::getSubtotal)
                .sum();

        this.totalBenefit = items.stream()
                .mapToDouble(item -> item.getBenefit() * item.getQuantity())
                .sum();
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSaleNumber() { return saleNumber; }
    public void setSaleNumber(String saleNumber) { this.saleNumber = saleNumber; }

    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { this.items = items; }

    public Double getTotalAmount() { return totalAmount; }
    public Double getTotalBenefit() { return totalBenefit; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getSaleDate() { return saleDate; }
}

