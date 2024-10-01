package com.globalmart.app.controllers;

import com.globalmart.app.dto.InventoryDTO;
import com.globalmart.app.dto.OrderFilterDTO;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.services.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<AppResponse<InventoryDTO>> createInventory(@Valid @RequestBody InventoryRequest inventoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(inventoryService.createInventory(inventoryRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse<InventoryDTO>> updateInventory(@NotNull @PathVariable String id,
                                                                     @Valid @RequestBody InventoryRequest inventoryRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(inventoryService.updateInventory(id, inventoryRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@NotNull @PathVariable String id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent()
                             .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<InventoryDTO>> getInventory(@NotNull @PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(inventoryService.getInventory(id));
    }

    @GetMapping
    public ResponseEntity<AppResponse<List<InventoryDTO>>> getAllInventories(@Min(value = 0) @RequestParam(defaultValue = "0") long page,
                                                                             @RequestParam(defaultValue = "50") long size,
                                                                             @NotBlank(message = "sortOrder cannot be null") String sortOrder,
                                                                             @RequestParam OrderFilterDTO filter) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(inventoryService.getAllInventories(page, size, sortOrder, filter));
    }

}
