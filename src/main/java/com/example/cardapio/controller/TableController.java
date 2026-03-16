package com.example.cardapio.controller;

import com.example.cardapio.dto.TableDTO;
import com.example.cardapio.dto.request.TableRequest;
import com.example.cardapio.model.RestaurantTable;
import com.example.cardapio.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableRepository tableRepository;

    @GetMapping
    public ResponseEntity<List<TableDTO>> getAll() {
        List<TableDTO> tables = tableRepository.findAll()
                .stream()
                .map(TableDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TableDTO>> getActive() {
        List<TableDTO> tables = tableRepository.findByIsActiveTrue()
                .stream()
                .map(TableDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableDTO> getById(@PathVariable Long id) {
        return tableRepository.findById(id)
                .map(table -> ResponseEntity.ok(new TableDTO(table)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TableDTO> create(@RequestBody TableRequest request) {
        RestaurantTable table = new RestaurantTable();
        table.setNumber(request.getNumber());
        table.setCapacity(request.getCapacity() != null ? request.getCapacity() : 4);
        table.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        table.setStatus(RestaurantTable.TableStatus.FREE);

        RestaurantTable saved = tableRepository.save(table);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TableDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableDTO> update(@PathVariable Long id, @RequestBody TableRequest request) {
        return tableRepository.findById(id)
                .map(table -> {
                    table.setNumber(request.getNumber());
                    if (request.getCapacity() != null) {
                        table.setCapacity(request.getCapacity());
                    }
                    if (request.getIsActive() != null) {
                        table.setIsActive(request.getIsActive());
                    }
                    RestaurantTable updated = tableRepository.save(table);
                    return ResponseEntity.ok(new TableDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TableDTO> updateStatus(@PathVariable Long id, @RequestParam RestaurantTable.TableStatus status) {
        return tableRepository.findById(id)
                .map(table -> {
                    table.setStatus(status);
                    RestaurantTable updated = tableRepository.save(table);
                    return ResponseEntity.ok(new TableDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (tableRepository.existsById(id)) {
            tableRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
