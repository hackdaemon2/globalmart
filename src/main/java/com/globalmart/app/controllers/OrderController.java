package com.globalmart.app.controllers;

import com.globalmart.app.dto.OrderDTO;
import com.globalmart.app.models.requests.OrderRequest;
import com.globalmart.app.models.requests.OrderRequestParam;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.globalmart.app.models.constants.SwaggerConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Create Order", description = "Create a customer order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_CREATED_STATUS, description = "Created"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<OrderDTO>> createOrder(@Valid @RequestBody OrderRequest order) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(orderService.createOrder(order));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Update Order", description = "Update a customer order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_OK_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<OrderDTO>> updateOrder(@NotNull @PathVariable String id,
                                                             @Valid @RequestBody OrderRequest order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Delete Order", description = "Delete a customer order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_NO_CONTENT_STATUS, description = "No Content"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<Void> deleteOrder(@NotNull @PathVariable String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Get Order", description = "Retrieve a customer order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_OK_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<OrderDTO>> getOrder(@NotNull @PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Get All Orders", description = "Retrieve all customer orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_OK_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<List<OrderDTO>>> getAllOrders(OrderRequestParam orderRequestParam) {
        return ResponseEntity.ok(orderService.getAllOrders(
                orderRequestParam.page(),
                orderRequestParam.size(),
                orderRequestParam.sortOrder(),
                orderRequestParam.filter()
        ));
    }
}
