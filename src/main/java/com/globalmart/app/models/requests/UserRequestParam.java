package com.globalmart.app.models.requests;

import com.globalmart.app.dto.UserFilterDTO;
import com.globalmart.app.enums.SortOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestParam;

public record UserRequestParam(@Min(value = 0) @RequestParam(defaultValue = "0") long page,
                               @RequestParam(defaultValue = "50") long size,
                               @NotBlank(message = "sortOrder cannot be null") SortOrder sortOrder,
                               @RequestParam UserFilterDTO filter) {
}
