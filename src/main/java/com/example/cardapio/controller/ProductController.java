package com.example.cardapio.controller;

import com.example.cardapio.dto.ProductDTO;
import com.example.cardapio.dto.request.ProductRequest;
import com.example.cardapio.model.Category;
import com.example.cardapio.model.Product;
import com.example.cardapio.repository.CategoryRepository;
import com.example.cardapio.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll() {
        List<ProductDTO> products = productRepository.findAll()
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ProductDTO>> getAvailable() {
        List<ProductDTO> products = productRepository.findByIsAvailableTrue()
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productRepository.findByCategoryIdAndIsAvailableTrue(categoryId)
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/promotions")
    public ResponseEntity<List<ProductDTO>> getPromotions() {
        List<ProductDTO> products = productRepository.findPromotionProducts()
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(new ProductDTO(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setPreparationTime(request.getPreparationTime());
        product.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);
        product.setIsPromotion(request.getIsPromotion() != null ? request.getIsPromotion() : false);
        product.setPromotionPrice(request.getPromotionPrice());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            product.setCategory(category);
        }

        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductRequest request) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(request.getName());
                    product.setDescription(request.getDescription());
                    product.setPrice(request.getPrice());
                    product.setImageUrl(request.getImageUrl());
                    product.setPreparationTime(request.getPreparationTime());
                    if (request.getIsAvailable() != null) {
                        product.setIsAvailable(request.getIsAvailable());
                    }
                    if (request.getIsPromotion() != null) {
                        product.setIsPromotion(request.getIsPromotion());
                    }
                    product.setPromotionPrice(request.getPromotionPrice());

                    if (request.getCategoryId() != null) {
                        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
                        product.setCategory(category);
                    }

                    Product updated = productRepository.save(product);
                    return ResponseEntity.ok(new ProductDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
