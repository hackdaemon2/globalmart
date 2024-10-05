package com.globalmart.app.dto;

import lombok.Getter;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public record UserFilterDTO(String firstName,
                            String lastName,
                            String username,
                            String email,
                            String phone,
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                            LocalDateTime startDate,
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                            LocalDateTime stopDate) {

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
