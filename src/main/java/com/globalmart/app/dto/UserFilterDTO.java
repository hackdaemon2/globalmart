package com.globalmart.app.dto;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public record UserFilterDTO(String firstName,
                            String lastName,
                            String username,
                            String email,
                            String phone,
                            String startDate,
                            String stopDate) {

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
