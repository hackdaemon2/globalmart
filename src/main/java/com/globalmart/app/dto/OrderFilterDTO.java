package com.globalmart.app.dto;

public record OrderFilterDTO(String id,
                             String status,
                             String currency,
                             String userId,
                             String startDate,
                             String stopDate) {
}
