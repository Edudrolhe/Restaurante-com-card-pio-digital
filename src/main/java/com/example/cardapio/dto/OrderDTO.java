package com.example.cardapio.dto;

import com.example.cardapio.model.Order;
import com.example.cardapio.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private Long tableId;
    private String tableNumber;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String notes;
    private String waiterName;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.tableId = order.getTable() != null ? order.getTable().getId() : null;
        this.tableNumber = order.getTable() != null ? order.getTable().getNumber() : null;
        this.status = order.getStatus().name();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
        this.closedAt = order.getClosedAt();
        this.items = order.getItems().stream().map(OrderItemDTO::new).collect(Collectors.toList());
        this.totalAmount = order.getTotalAmount();
        this.discountAmount = order.getDiscountAmount();
        this.finalAmount = order.getFinalAmount();
        this.notes = order.getNotes();
        this.waiterName = order.getWaiterName();
    }
}
