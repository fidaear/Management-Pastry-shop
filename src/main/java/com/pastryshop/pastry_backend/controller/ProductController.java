package com.pastryshop.pastry_backend.controller;

import com.pastryshop.pastry_backend.model.Product;
import com.pastryshop.pastry_backend.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /* ================= CREATE ================= */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        return ResponseEntity.ok(
                productService.createProduct(product, image)
        );
    }

    /* ================= READ ================= */

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable String id) {
        return productService.getProductById(id);
    }



    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    @GetMapping("/low-stock")
    public List<Product> lowStock(@RequestParam(defaultValue = "10") Integer threshold) {
        return productService.getLowStockProducts(threshold);
    }

    /* ================= UPDATE ================= */

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product updateProduct(
            @PathVariable String id,
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        return productService.updateProduct(id, product, image);
    }

    /* ================= DELETE ================= */

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    /* ================= STATS ================= */

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return productService.getProductStats();
    }

    /* ================= EXPORT ================= */

    @GetMapping("/export/excel")
    public byte[] exportExcel() {
        return productService.exportProductsToExcel();
    }
}
