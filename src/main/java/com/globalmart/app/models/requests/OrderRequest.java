package com.globalmart.app.models.requests;

import com.globalmart.app.dto.ProductDTO;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(@Size(message = "invalid length for status", min = 2, max = 255)
                           String status,

                           @Size(message = "invalid length for currency", min = 3, max = 3)
                           String currency,

                           @Positive(message = "invalid value for userID")
                           String userID,

                           @Size(message = "invalid length for orderReference", min = 2, max = 255)
                           String orderReference,

                           List<ProductDTO> products) {
}
