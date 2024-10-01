package com.globalmart.app.dto;

import java.math.BigDecimal;

public record ProductFilterDTO(String name,
                               String productCode,
                               BigDecimal price,
                               String currency,
                               String userID,
                               String startDate,
                               String stopDate) {
}
