package com.example.cardapio.controller;

import com.example.cardapio.dto.OrderDTO;
import com.example.cardapio.dto.request.OrderRequest;
import com.example.cardapio.model.*;
import com.example.cardapio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final TableRepository tableRepository;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAll() {
        List<OrderDTO> orders = orderRepository.findAll()
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrderDTO>> getActive() {
        List<OrderDTO> orders = orderRepository.findActiveOrders()
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getByStatus(@PathVariable Order.OrderStatus status) {
        List<OrderDTO> orders = orderRepository.findByStatus(status)
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<OrderDTO>> getByTable(@PathVariable Long tableId) {
        List<OrderDTO> orders = orderRepository.findByTableId(tableId)
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok(new OrderDTO(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody OrderRequest request) {
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setWaiterName(request.getWaiterName());
        order.setNotes(request.getNotes());
        order.setDiscountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO);
        order.setStatus(Order.OrderStatus.OPEN);

        if (request.getTableId() != null) {
            RestaurantTable table = tableRepository.findById(request.getTableId()).orElse(null);
            order.setTable(table);
            if (table != null) {
                table.setStatus(RestaurantTable.TableStatus.OCCUPIED);
                tableRepository.save(table);
            }
        }

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            BigDecimal total = BigDecimal.ZERO;
            for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
                Product product = productRepository.findById(itemRequest.getProductId()).orElse(null);
                if (product != null) {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setProduct(product);
                    item.setQuantity(itemRequest.getQuantity());
                    
                    BigDecimal unitPrice = product.getIsPromotion() && product.getPromotionPrice() != null 
                            ? product.getPromotionPrice() 
                            : product.getPrice();
                    item.setUnitPrice(unitPrice);
                    item.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
                    item.setObservations(itemRequest.getObservations());
                    
                    order.getItems().add(item);
                    total = total.add(item.getTotalPrice());
                }
            }
            order.setTotalAmount(total);
            order.setFinalAmount(total.subtract(order.getDiscountAmount()));
        } else {
            order.setTotalAmount(BigDecimal.ZERO);
            order.setFinalAmount(BigDecimal.ZERO);
        }

        Order saved = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderDTO(saved));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long id, @RequestParam Order.OrderStatus status) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    order.setUpdatedAt(LocalDateTime.now());
                    
                    if (status == Order.OrderStatus.CLOSED || status == Order.OrderStatus.CANCELLED) {
                        order.setClosedAt(LocalDateTime.now());
                        if (order.getTable() != null) {
                            RestaurantTable table = order.getTable();
                            table.setStatus(RestaurantTable.TableStatus.FREE);
                            tableRepository.save(table);
                        }
                    }
                    
                    Order updated = orderRepository.save(order);
                    return ResponseEntity.ok(new OrderDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<OrderDTO> addItem(@PathVariable Long id, @RequestBody OrderRequest.OrderItemRequest itemRequest) {
        return orderRepository.findById(id)
                .map(order -> {
                    Product product = productRepository.findById(itemRequest.getProductId()).orElse(null);
                    if (product != null) {
                        OrderItem item = new OrderItem();
                        item.setOrder(order);
                        item.setProduct(product);
                        item.setQuantity(itemRequest.getQuantity());
                        
                        BigDecimal unitPrice = product.getIsPromotion() && product.getPromotionPrice() != null 
                                ? product.getPromotionPrice() 
                                : product.getPrice();
                        item.setUnitPrice(unitPrice);
                        item.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
                        item.setObservations(itemRequest.getObservations());
                        
                        order.getItems().add(item);
                        recalculateTotal(order);
                        
                        Order updated = orderRepository.save(order);
                        return ResponseEntity.ok(new OrderDTO(updated));
                    }
                    return ResponseEntity.badRequest().<OrderDTO>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<OrderDTO> removeItem(@PathVariable Long id, @PathVariable Long itemId) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.getItems().removeIf(item -> item.getId().equals(itemId));
                    recalculateTotal(order);
                    
                    Order updated = orderRepository.save(order);
                    return ResponseEntity.ok(new OrderDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void recalculateTotal(Order order) {
        BigDecimal total = order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
        order.setFinalAmount(total.subtract(order.getDiscountAmount()));
        order.setUpdatedAt(LocalDateTime.now());
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
