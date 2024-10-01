package com.globalmart.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import java.math.BigDecimal;

public record OrderDTO(@JsonProperty("id") String guid,
                       String status,
                       BigDecimal totalAmount,
                       String currency,
                       String userID) {

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
