package com.pastryshop.pastry_backend.model;

public class SaleItem {

    private String productId;
    private String productName;

    private int quantity;

    private double purchasePrice;
    private double sellingPrice;

    /* ===================== */
    /* CALCULATED VALUES     */
    /* ===================== */

    public double getSubtotal() {
        return sellingPrice * quantity;
    }

    public double getBenefit() {
        return sellingPrice - purchasePrice;
    }

    /* ===================== */
    /* GETTERS & SETTERS     */
    /* ===================== */

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
