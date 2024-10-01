package com.globalmart.app.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(@Size(message = "invalid length for name", min = 2, max = 255)
                             String name,

                             @Size(message = "invalid length for description", min = 2, max = 255)
                             String description,

                             @Size(message = "invalid length for productCode", min = 2, max = 255)
                             String productCode,

                             @JsonProperty("price")
                             @Positive(message = "invalid value for price")
                             BigDecimal price,

                             @Size(message = "invalid length for currency", min = 3, max = 3)
                             String currency,

                             @NotBlank(message = "invalid value for userID")
                             String userID) {
}