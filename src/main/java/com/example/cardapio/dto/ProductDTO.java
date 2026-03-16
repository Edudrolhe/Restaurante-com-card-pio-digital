package com.example.cardapio.dto;

import com.example.cardapio.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
    private Boolean isAvailable;
    private Integer preparationTime;
    private Boolean isPromotion;
    private BigDecimal promotionPrice;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        this.categoryName = product.getCategory() != null ? product.getCategory().getName() : null;
        this.isAvailable = product.getIsAvailable();
        this.preparationTime = product.getPreparationTime();
        this.isPromotion = product.getIsPromotion();
        this.promotionPrice = product.getPromotionPrice();
    }
}
