package com.globalmart.app.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductFilterDTO(String name,
                               String productCode,
                               BigDecimal price,
                               String currency,
                               String userID,
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                               LocalDateTime startDate,
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                               LocalDateTime stopDate) {
}
