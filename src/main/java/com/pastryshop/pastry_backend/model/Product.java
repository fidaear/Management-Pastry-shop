package com.pastryshop.pastry_backend.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    private String name;
    private String image; // path or base64
    private double buyPrice;
    private double sellPrice;
    private int quantitySold;
    private double benefit;
}
