package com.globalmart.app.dto;

import com.globalmart.app.enums.OrderStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record OrderFilterDTO(String id,
                             OrderStatus status,
                             String currency,
                             String userId,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             LocalDateTime startDate,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             LocalDateTime stopDate) {
}
