package com.example.cardapio.repository;

import com.example.cardapio.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
    List<RestaurantTable> findByIsActiveTrue();
    List<RestaurantTable> findByStatus(RestaurantTable.TableStatus status);
    Optional<RestaurantTable> findByNumber(String number);
}
