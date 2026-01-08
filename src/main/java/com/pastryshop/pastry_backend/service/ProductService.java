package com.pastryshop.pastry_backend.service;

import com.pastryshop.pastry_backend.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {

    /* CREATE */
    Product createProduct(Product product, MultipartFile image);

    /* READ */
    List<Product> getAllProducts();
    Product getProductById(String id);
    List<Product> searchProducts(String keyword);
    List<Product> getLowStockProducts(Integer threshold);

    /* UPDATE */
    Product updateProduct(String id, Product product, MultipartFile image);

    /* DELETE */
    void deleteProduct(String id);

    /* STATS */
    Map<String, Object> getProductStats();

    /* EXPORT */
    byte[] exportProductsToExcel();
}
