package com.globalmart.app.models.requests;

import com.globalmart.app.dto.OrderFilterDTO;
import com.globalmart.app.enums.SortOrder;
import jakarta.validation.constraints.Min;

public record OrderRequestParam(@Min(value = 0) long page,
                                long size,
                                SortOrder sortOrder,
                                OrderFilterDTO filter) {
}