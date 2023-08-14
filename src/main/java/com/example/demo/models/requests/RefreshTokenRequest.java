package com.example.demo.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenRequest {

    @NotBlank(message = "refreshToken cannot be blank")
    @NotEmpty(message = "refreshToken cannot be empty")
    @NotNull(message = "refreshToken cannot be null")
    private final String refreshToken;
}
