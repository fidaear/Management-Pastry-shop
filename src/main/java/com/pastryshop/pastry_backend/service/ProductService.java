package com.pastryshop.pastry_backend.service;

import com.pastryshop.pastry_backend.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Product createProduct(Product product, MultipartFile imageFile);

    Product updateProduct(String id, Product product, MultipartFile imageFile);

    void deleteProduct(String id);

    Product getProductById(String id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> searchProducts(String keyword);

    Map<String, Object> getProductStats();

    List<Product> getLowStockProducts(Integer threshold);

    byte[] exportProductsToExcel();
}
