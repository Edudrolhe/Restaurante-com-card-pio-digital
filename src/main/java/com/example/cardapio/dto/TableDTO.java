package com.example.cardapio.dto;

import com.example.cardapio.model.RestaurantTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableDTO {
    private Long id;
    private String number;
    private Integer capacity;
    private String status;
    private Boolean isActive;

    public TableDTO(RestaurantTable table) {
        this.id = table.getId();
        this.number = table.getNumber();
        this.capacity = table.getCapacity();
        this.status = table.getStatus().name();
        this.isActive = table.getIsActive();
    }
}
