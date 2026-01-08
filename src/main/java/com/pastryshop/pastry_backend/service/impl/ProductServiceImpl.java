package com.pastryshop.pastry_backend.service.impl;

import com.pastryshop.pastry_backend.model.Product;
import com.pastryshop.pastry_backend.repository.ProductRepository;
import com.pastryshop.pastry_backend.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    /* ================= CREATE ================= */

    @Override
    public Product createProduct(Product product, MultipartFile image) {
        product.setNumberOfSales(0);
        product.setTotalBenefit(0);

        // TODO: handle image storage later
        return repository.save(product);
    }

    /* ================= READ ================= */

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public Product getProductById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }



    @Override
    public List<Product> searchProducts(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> getLowStockProducts(Integer threshold) {
        return repository.findByQuantityLessThan(threshold);
    }

    /* ================= UPDATE ================= */

    @Override
    public Product updateProduct(String id, Product updated, MultipartFile image) {
        Product product = getProductById(id);

        product.setName(updated.getName());
        product.setSellingPrice(updated.getSellingPrice());
        product.setPurchasePrice(updated.getPurchasePrice());
        product.setQuantity(updated.getQuantity());

        // TODO: update image if provided
        return repository.save(product);
    }

    /* ================= DELETE ================= */

    @Override
    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

    /* ================= STATS ================= */

    @Override
    public Map<String, Object> getProductStats() {
        Map<String, Object> stats = new HashMap<>();

        double totalBenefit = repository.findAll()
                .stream()
                .mapToDouble(Product::getTotalBenefit)
                .sum();

        int totalSales = repository.findAll()
                .stream()
                .mapToInt(Product::getNumberOfSales)
                .sum();

        stats.put("totalBenefit", totalBenefit);
        stats.put("totalSales", totalSales);

        return stats;
    }

    /* ================= EXPORT ================= */

    @Override
    public byte[] exportProductsToExcel() {
        // TODO: implement Apache POI
        return new byte[0];
    }
}
