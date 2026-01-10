package com.pastryshop.pastry_backend.service.impl;

import com.pastryshop.pastry_backend.model.Product;
import com.pastryshop.pastry_backend.repository.ProductRepository;
import com.pastryshop.pastry_backend.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final Path imageStorageLocation;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;

        // Define where to store images
        this.imageStorageLocation = Paths.get("uploads/images")
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.imageStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @Override
    public Product createProduct(Product product, MultipartFile image) {
        product.setNumberOfSales(0);
        product.setTotalBenefit(0);

        // Handle image if provided
        if (image != null && !image.isEmpty()) {
            String imagePath = storeImage(image);
            product.setImage(imagePath);
        }

        return repository.save(product);
    }

    @Override
    public Product updateProduct(String id, Product updated, MultipartFile image) {
        Product product = getProductById(id);

        product.setName(updated.getName());
        product.setSellingPrice(updated.getSellingPrice());
        product.setPurchasePrice(updated.getPurchasePrice());
        product.setQuantity(updated.getQuantity());

        // Update active status


        // Update image if provided
        if (image != null && !image.isEmpty()) {
            String imagePath = storeImage(image);
            product.setImage(imagePath);
        }

        return repository.save(product);
    }

    // Helper method to store image
    private String storeImage(MultipartFile image) {
        try {
            // Generate unique filename
            String originalFilename = image.getOriginalFilename();
            String fileExtension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID().toString() + fileExtension;

            // Copy file to target location
            Path targetLocation = this.imageStorageLocation.resolve(fileName);
            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Return the relative path or just filename
            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    // Other methods remain the same...
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

    @Override
    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

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

    @Override
    public byte[] exportProductsToExcel() {
        // TODO: implement Apache POI
        return new byte[0];
    }
}