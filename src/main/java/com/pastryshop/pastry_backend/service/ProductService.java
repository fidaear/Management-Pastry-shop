package com.pastryshop.pastry_backend.service;



import com.pastryshop.pastry_backend.model.Product;
import com.pastryshop.pastry_backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product addProduct(Product product) {
        product.setQuantitySold(0);
        product.setBenefit(0);
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product updateProduct(String id, Product updated) {
        Product product = repository.findById(id).orElseThrow();
        product.setName(updated.getName());
        product.setBuyPrice(updated.getBuyPrice());
        product.setSellPrice(updated.getSellPrice());
        product.setImage(updated.getImage());
        return repository.save(product);
    }

    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

    public Product sellProduct(String id, int quantity) {
        Product product = repository.findById(id).orElseThrow();
        product.setQuantitySold(product.getQuantitySold() + quantity);
        double benefit = (product.getSellPrice() - product.getBuyPrice()) * product.getQuantitySold();
        product.setBenefit(benefit);
        return repository.save(product);
    }
}
