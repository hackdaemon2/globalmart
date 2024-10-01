package com.globalmart.app.dto;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public record AccessTokenDTO(String accessToken,
                             String refreshToken) {

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
