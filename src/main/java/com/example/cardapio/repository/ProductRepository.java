package com.example.cardapio.repository;

import com.example.cardapio.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByCategoryIdAndIsAvailableTrue(Long categoryId);
    
    List<Product> findByIsAvailableTrue();
    
    @Query("SELECT p FROM Product p WHERE p.isAvailable = true AND p.isPromotion = true")
    List<Product> findPromotionProducts();
    
    List<Product> findByNameContainingIgnoreCase(String name);
}
