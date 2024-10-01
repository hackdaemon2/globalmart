package com.globalmart.app.controllers;

import com.globalmart.app.dto.OrderDTO;
import com.globalmart.app.dto.OrderFilterDTO;
import com.globalmart.app.models.requests.OrderRequest;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.services.OrderService;
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
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<AppResponse<OrderDTO>> createOrder(@Valid @RequestBody OrderRequest order) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(orderService.createOrder(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse<OrderDTO>> updateOrder(@NotNull @PathVariable String id,
                                                             @Valid @RequestBody OrderRequest order) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@NotNull @PathVariable String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent()
                             .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<OrderDTO>> getOrder(@NotNull @PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(orderService.getOrder(id));
    }

    @GetMapping
    public ResponseEntity<AppResponse<List<OrderDTO>>> getAllOrders(@Min(value = 0) @RequestParam(defaultValue = "0") long page,
                                                                    @RequestParam(defaultValue = "50") long size,
                                                                    @NotBlank(message = "sortOrder cannot be null") String sortOrder,
                                                                    @RequestParam OrderFilterDTO filter) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(orderService.getAllOrders(page, size, sortOrder, filter));
    }

}
