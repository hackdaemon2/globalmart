package com.globalmart.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public record ProductDTO(@JsonProperty("id") String guid,
                         String name,
                         String description,
                         BigDecimal price,
                         String currency,
                         String userID) {
}
