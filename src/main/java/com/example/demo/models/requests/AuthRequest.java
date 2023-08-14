package com.example.demo.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;

@Getter
@AllArgsConstructor
public class AuthRequest {

    @NotBlank(message = "username cannot be blank")
    @NotEmpty(message = "username cannot be empty")
    @NotNull(message = "username cannot be null")
    private final String username;

    @NotBlank(message = "password cannot be blank")
    @NotEmpty(message = "password cannot be empty")
    @NotNull(message = "password cannot be null")
    private final String password;

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }
}
