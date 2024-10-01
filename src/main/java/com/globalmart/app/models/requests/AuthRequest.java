package com.globalmart.app.models.requests;

import jakarta.validation.constraints.NotBlank;
import org.json.JSONObject;

public record AuthRequest(@NotBlank(message = "username is required")
                          String username,

                          @NotBlank(message = "password is required")
                          String password) {

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
