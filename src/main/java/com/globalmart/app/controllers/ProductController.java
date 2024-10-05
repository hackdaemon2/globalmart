package com.globalmart.app.controllers;

import com.globalmart.app.dto.ProductDTO;
import com.globalmart.app.models.requests.ProductRequest;
import com.globalmart.app.models.requests.ProductRequestParam;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.services.ProductService;
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
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Create Product", description = "Create a new product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_CREATED_STATUS, description = "Created"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        AppResponse<ProductDTO> productDTO = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Update Product", description = "Update an existing product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_OK_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<ProductDTO>> updateProduct(@NotNull @PathVariable String id,
                                                                 @Valid @RequestBody ProductRequest productRequest) {
        AppResponse<ProductDTO> productDTO = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Delete Product", description = "Delete an existing product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_NO_CONTENT_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<Void> deleteProduct(@NotNull @PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Get Single Product", description = "Get an existing product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_OK_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<ProductDTO>> getProduct(@NotNull @PathVariable String id) {
        AppResponse<ProductDTO> productDTO = productService.getProduct(id);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Get All Products", description = "Retrieve all existing products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_OK_STATUS, description = "Success"),
            @ApiResponse(responseCode = HTTP_BAD_REQUEST_STATUS, description = "Bad Request"),
            @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR_STATUS, description = "Internal Server Error"),
    })
    @Parameter(name = AUTHORIZATION, description = BEARER_TOKEN, required = true, in = ParameterIn.HEADER)
    public ResponseEntity<AppResponse<List<ProductDTO>>> getAllProducts(ProductRequestParam productRequestParam) {
        AppResponse<List<ProductDTO>> products = productService.getAllProduct(
                productRequestParam.page(),
                productRequestParam.size(),
                productRequestParam.sortOrder(),
                productRequestParam.filter());
        return ResponseEntity.ok(products);
    }
}