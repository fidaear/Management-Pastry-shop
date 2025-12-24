package com.pastryshop.pastry_backend.service;



import com.pastryshop.pastry_backend.model.Product;
import com.pastryshop.pastry_backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import com.pastryshop.pastry_backend.DTO.ProductStats;
import java.util.List;



import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product addProduct(Product product) {
        product.setQuantitySold(0);
        product.setBenefit(0.0);
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
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }

        Product product = repository.findById(id).orElseThrow();

        int sold = product.getQuantitySold() == null ? 0 : product.getQuantitySold();
        product.setQuantitySold(sold + quantity);

        double benefit = (product.getSellPrice() - product.getBuyPrice()) * product.getQuantitySold();
        product.setBenefit(benefit);

        return repository.save(product);
    }
    public List<ProductStats> productStats() {
        return repository.findAll().stream()
                .map(p -> new ProductStats(
                        p.getName(),
                        p.getQuantitySold() == null ? 0 : p.getQuantitySold(),
                        p.getBenefit() == null ? 0 : p.getBenefit()
                ))
                .toList(); // Java 16+
    }


}
