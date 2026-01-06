package com.pastryshop.pastry_backend.service.impl;

import com.pastryshop.pastry_backend.model.Product;
import com.pastryshop.pastry_backend.repository.ProductRepository;
import com.pastryshop.pastry_backend.service.ProductService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /* ===================== INIT ===================== */

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    /* ===================== CRUD ===================== */

    @Override
    public Product createProduct(Product product, MultipartFile imageFile) {

        if (productRepository.findByName(product.getName()).isPresent()) {
            throw new RuntimeException("Product already exists");
        }

        product.calculateBenefit();
        product.setNumberOfSales(0);
        product.setTotalBenefit(0.0);
        product.setIsActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile, product.getName());
            product.setImagePath(imagePath);
            product.setImageUrl("/products/images/" +
                    Paths.get(imagePath).getFileName());
        }

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String id, Product product, MultipartFile imageFile) {

        Product existing = getProductById(id);

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setCategory(product.getCategory());
        existing.setCostPrice(product.getCostPrice());
        existing.setSellingPrice(product.getSellingPrice());
        existing.setQuantity(product.getQuantity());
        existing.setBarcode(product.getBarcode());
        existing.setIngredients(product.getIngredients());

        existing.calculateBenefit();

        if (imageFile != null && !imageFile.isEmpty()) {
            deleteImage(existing.getImagePath());
            String imagePath = saveImage(imageFile, product.getName());
            existing.setImagePath(imagePath);
            existing.setImageUrl("/products/images/" +
                    Paths.get(imagePath).getFileName());
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = getProductById(id);
        product.setIsActive(false);
        productRepository.save(product);
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findByIsActive(true);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByName(keyword);
    }

    @Override
    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findLowStock(threshold);
    }

    /* ===================== STATS ===================== */

    @Override
    public Map<String, Object> getProductStats() {

        List<Product> products = productRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalProducts", products.size());

        stats.put("totalInventoryValue",
                products.stream()
                        .mapToDouble(p -> p.getCostPrice() * p.getQuantity())
                        .sum()
        );

        stats.put("totalPotentialBenefit",
                products.stream()
                        .mapToDouble(p -> p.getBenefit() * p.getQuantity())
                        .sum()
        );

        stats.put("topSellingProducts",
                productRepository.findTopSellingProducts().stream()
                        .limit(5)
                        .toList()
        );

        return stats;
    }

    /* ===================== EXCEL EXPORT ===================== */

    @Override
    public byte[] exportProductsToExcel() {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Name");
            header.createCell(1).setCellValue("Category");
            header.createCell(2).setCellValue("Quantity");
            header.createCell(3).setCellValue("Cost Price");
            header.createCell(4).setCellValue("Selling Price");
            header.createCell(5).setCellValue("Benefit");

            int rowIdx = 1;
            for (Product p : productRepository.findAll()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getName());
                row.createCell(1).setCellValue(p.getCategory());
                row.createCell(2).setCellValue(p.getQuantity());
                row.createCell(3).setCellValue(p.getCostPrice());
                row.createCell(4).setCellValue(p.getSellingPrice());
                row.createCell(5).setCellValue(p.getBenefit());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Excel export failed", e);
        }
    }

    /* ===================== FILE UTILS ===================== */

    private String saveImage(MultipartFile file, String name) {
        try {
            String ext = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            String filename = name.replaceAll("\\s+", "_")
                    + "_" + System.currentTimeMillis() + ext;

            Path path = Paths.get(uploadDir, filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    private void deleteImage(String path) {
        if (path == null) return;
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException ignored) {}
    }
}
