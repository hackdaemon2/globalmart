package com.example.demo.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private final String accessToken;
    private final String refreshToken;
    private final String responseCode;
    private final String responseMessage;

    private final String tokenType = "Bearer";

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }
}
