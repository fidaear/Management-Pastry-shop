package com.pastryshop.pastry_backend.controller;




import com.pastryshop.pastry_backend.model.Product;
import com.pastryshop.pastry_backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public Product add(@RequestBody Product product) {
        return service.addProduct(product);
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAllProducts();
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @RequestBody Product product) {
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteProduct(id);
    }

    @PostMapping("/{id}/sell/{quantity}")
    public Product sell(@PathVariable String id, @PathVariable int quantity) {
        return service.sellProduct(id, quantity);
    }
}
