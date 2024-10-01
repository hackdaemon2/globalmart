package com.globalmart.app.controllers;

import com.globalmart.app.dto.ProductDTO;
import com.globalmart.app.dto.ProductFilterDTO;
import com.globalmart.app.models.requests.ProductRequest;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.services.ProductService;
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
@RequestMapping(path = "/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<AppResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(productService.createProduct(productRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse<ProductDTO>> updateProduct(@NotNull @PathVariable String id,
                                                                 @Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(productService.updateProduct(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@NotNull @PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent()
                             .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<ProductDTO>> getProduct(@NotNull @PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<AppResponse<List<ProductDTO>>> getAllProduct(@Min(value = 0) @RequestParam(defaultValue = "0") long page,
                                                                       @RequestParam(defaultValue = "50") long size,
                                                                       @NotBlank(message = "sortOrder cannot be null") String sortOrder,
                                                                       @RequestParam ProductFilterDTO filter) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(productService.getAllProduct(page, size, sortOrder, filter));
    }

}
