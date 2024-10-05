package com.globalmart.app.models.requests;

import com.globalmart.app.dto.ProductFilterDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestParam;

public record ProductRequestParam(@Min(value = 0) @RequestParam(defaultValue = "0") long page,
                                  @RequestParam(defaultValue = "50") long size,
                                  @NotBlank(message = "sortOrder cannot be null") @RequestParam String sortOrder,
                                  @RequestParam ProductFilterDTO filter) {
}
