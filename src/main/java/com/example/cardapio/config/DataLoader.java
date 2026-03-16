package com.example.cardapio.config;

import com.example.cardapio.model.Category;
import com.example.cardapio.model.Product;
import com.example.cardapio.model.RestaurantTable;
import com.example.cardapio.repository.CategoryRepository;
import com.example.cardapio.repository.ProductRepository;
import com.example.cardapio.repository.TableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            TableRepository tableRepository) {
        
        return args -> {
            if (categoryRepository.count() == 0) {
                
                // Categories
                Category entradas = new Category();
                entradas.setName("Entradas");
                entradas.setDescription("Aperitivos e entradas");
                entradas.setDisplayOrder(1);
                entradas.setIsActive(true);
                entradas = categoryRepository.save(entradas);
                
                Category principais = new Category();
                principais.setName("Pratos Principais");
                principais.setDescription("Pratos principais do restaurante");
                principais.setDisplayOrder(2);
                principais.setIsActive(true);
                principais = categoryRepository.save(principais);
                
                Category bebidas = new Category();
                bebidas.setName("Bebidas");
                bebidas.setDescription("Bebidas variadas");
                bebidas.setDisplayOrder(3);
                bebidas.setIsActive(true);
                bebidas = categoryRepository.save(bebidas);
                
                Category sobremesas = new Category();
                sobremesas.setName("Sobremesas");
                sobremesas.setDescription("Doces e sobremesas");
                sobremesas.setDisplayOrder(4);
                sobremesas.setIsActive(true);
                sobremesas = categoryRepository.save(sobremesas);
                
                Category fastfood = new Category();
                fastfood.setName("Fast Food");
                fastfood.setDescription("Lanches e salgados");
                fastfood.setDisplayOrder(5);
                fastfood.setIsActive(true);
                fastfood = categoryRepository.save(fastfood);
                
                // Products - Entradas
                Product p1 = new Product();
                p1.setName("Bruschetta");
                p1.setDescription("Pão italiano com tomate fresco, alho e manjericão");
                p1.setPrice(new BigDecimal("18.90"));
                p1.setImageUrl("https://images.unsplash.com/photo-1572695157366-5e585ab2b69f?w=600&q=80");
                p1.setCategory(entradas);
                p1.setIsAvailable(true);
                p1.setPreparationTime(10);
                productRepository.save(p1);
                
                Product p2 = new Product();
                p2.setName("Carpaccio");
                p2.setDescription("Finas fatias de carne com rúcula e parmesão");
                p2.setPrice(new BigDecimal("32.90"));
                p2.setImageUrl("https://images.unsplash.com/photo-1608897013039-887f21d8c804?w=600&q=80");
                p2.setCategory(entradas);
                p2.setIsAvailable(true);
                p2.setPreparationTime(8);
                productRepository.save(p2);
                
                Product p3 = new Product();
                p3.setName("Bolinhos de Queijo");
                p3.setDescription("6 unidades de bolinho crocante");
                p3.setPrice(new BigDecimal("22.90"));
                p3.setImageUrl("https://images.unsplash.com/photo-1544025162-d76694265947?w=600&q=80");
                p3.setCategory(entradas);
                p3.setIsAvailable(true);
                p3.setPreparationTime(12);
                p3.setIsPromotion(true);
                p3.setPromotionPrice(new BigDecimal("18.90"));
                productRepository.save(p3);
                
                // Pratos Principais
                Product p4 = new Product();
                p4.setName("Picanha na Brasa");
                p4.setDescription("Picanha 300g com arroz e fries");
                p4.setPrice(new BigDecimal("68.90"));
                p4.setImageUrl("https://images.unsplash.com/photo-1600891964092-4316c288032e?w=600&q=80");
                p4.setCategory(principais);
                p4.setIsAvailable(true);
                p4.setPreparationTime(25);
                productRepository.save(p4);
                
                Product p5 = new Product();
                p5.setName("Salmão Grelhado");
                p5.setDescription("Salmão com legumes e batata");
                p5.setPrice(new BigDecimal("58.90"));
                p5.setImageUrl("https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=600&q=80");
                p5.setCategory(principais);
                p5.setIsAvailable(true);
                p5.setPreparationTime(20);
                productRepository.save(p5);
                
                Product p6 = new Product();
                p6.setName("Filé Mignon");
                p6.setDescription("Filé 250g com purê");
                p6.setPrice(new BigDecimal("62.90"));
                p6.setImageUrl("https://images.unsplash.com/photo-1558030006-450675393462?w=600&q=80");
                p6.setCategory(principais);
                p6.setIsAvailable(true);
                p6.setPreparationTime(22);
                p6.setIsPromotion(true);
                p6.setPromotionPrice(new BigDecimal("52.90"));
                productRepository.save(p6);
                
                Product p7 = new Product();
                p7.setName("Frango Assado");
                p7.setDescription("Meio frango com batatas");
                p7.setPrice(new BigDecimal("45.90"));
                p7.setImageUrl("https://images.unsplash.com/photo-1598103442097-8b74394b95c6?w=600&q=80");
                p7.setCategory(principais);
                p7.setIsAvailable(true);
                p7.setPreparationTime(30);
                productRepository.save(p7);
                
                // Bebidas
                Product p8 = new Product();
                p8.setName("Refrigerante");
                p8.setDescription("Lata 350ml");
                p8.setPrice(new BigDecimal("6.90"));
                p8.setImageUrl("https://images.unsplash.com/photo-1581006852262-e4307cf6283a?w=600&q=80");
                p8.setCategory(bebidas);
                p8.setIsAvailable(true);
                p8.setPreparationTime(2);
                productRepository.save(p8);
                
                Product p9 = new Product();
                p9.setName("Suco Natural");
                p9.setDescription("Copo 500ml");
                p9.setPrice(new BigDecimal("12.90"));
                p9.setImageUrl("https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=600&q=80");
                p9.setCategory(bebidas);
                p9.setIsAvailable(true);
                p9.setPreparationTime(3);
                productRepository.save(p9);
                
                Product p10 = new Product();
                p10.setName("Cerveja Artesanal");
                p10.setDescription("Cerveja long neck");
                p10.setPrice(new BigDecimal("14.90"));
                p10.setImageUrl("https://images.unsplash.com/photo-1608270586620-248524c67de9?w=600&q=80");
                p10.setCategory(bebidas);
                p10.setIsAvailable(true);
                p10.setPreparationTime(2);
                productRepository.save(p10);
                
                // Sobremesas
                Product p11 = new Product();
                p11.setName("Pudim");
                p11.setDescription("Pudim de leite condensado");
                p11.setPrice(new BigDecimal("15.90"));
                p11.setImageUrl("https://images.unsplash.com/photo-1551024506-0bccd828d307?w=600&q=80");
                p11.setCategory(sobremesas);
                p11.setIsAvailable(true);
                p11.setPreparationTime(5);
                productRepository.save(p11);
                
                Product p12 = new Product();
                p12.setName("Tiramisu");
                p12.setDescription("Sobremesa italiana clássica");
                p12.setPrice(new BigDecimal("22.90"));
                p12.setImageUrl("https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=600&q=80");
                p12.setCategory(sobremesas);
                p12.setIsAvailable(true);
                p12.setPreparationTime(8);
                productRepository.save(p12);
                
                // Fast Food
                Product p13 = new Product();
                p13.setName("Hambúrguer Clássico");
                p13.setDescription("Pão, carne 150g, queijo, alface");
                p13.setPrice(new BigDecimal("28.90"));
                p13.setImageUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&q=80");
                p13.setCategory(fastfood);
                p13.setIsAvailable(true);
                p13.setPreparationTime(15);
                productRepository.save(p13);
                
                Product p14 = new Product();
                p14.setName("X-Burguer");
                p14.setDescription("Pão, carne 150g, queijo, bacon, ovo");
                p14.setPrice(new BigDecimal("32.90"));
                p14.setImageUrl("https://images.unsplash.com/photo-1553979459-d2229ba7433b?w=600&q=80");
                p14.setCategory(fastfood);
                p14.setIsAvailable(true);
                p14.setPreparationTime(15);
                p14.setIsPromotion(true);
                p14.setPromotionPrice(new BigDecimal("26.90"));
                productRepository.save(p14);
                
                Product p15 = new Product();
                p15.setName("Batata Frita");
                p15.setDescription("Porção média");
                p15.setPrice(new BigDecimal("16.90"));
                p15.setImageUrl("https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=600&q=80");
                p15.setCategory(fastfood);
                p15.setIsAvailable(true);
                p15.setPreparationTime(10);
                productRepository.save(p15);
                
                // Tables
                for (int i = 1; i <= 6; i++) {
                    RestaurantTable t = new RestaurantTable();
                    t.setNumber(String.valueOf(i));
                    t.setCapacity(i == 5 ? 8 : (i == 3 ? 6 : 4));
                    t.setStatus(RestaurantTable.TableStatus.FREE);
                    t.setIsActive(true);
                    tableRepository.save(t);
                }
                
                System.out.println("=== Database seeded with initial data ===");
            }
        };
    }
}
