package com.example.cardapio.repository;

import com.example.cardapio.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByTableId(Long tableId);
    
    @Query("SELECT o FROM Order o WHERE o.status NOT IN ('CLOSED', 'CANCELLED') ORDER BY o.createdAt DESC")
    List<Order> findActiveOrders();
    
    @Query("SELECT o FROM Order o WHERE o.status = 'OPEN' AND o.table.id = :tableId")
    Optional<Order> findOpenOrderByTableId(Long tableId);
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
}
