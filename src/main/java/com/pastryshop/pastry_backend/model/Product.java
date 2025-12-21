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
    private String image;

    private Double buyPrice;
    private Double sellPrice;

    private Integer quantitySold;
    private Double benefit;
}
