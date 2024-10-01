package com.globalmart.app.models.requests;

import jakarta.validation.constraints.NotBlank;
import org.json.JSONObject;

public record RefreshTokenRequest(@NotBlank(message = "refreshToken cannot be blank")
                                  String refreshToken) {

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
