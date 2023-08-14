package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessTokenDTO {

    private final String accessToken;
    private final String refreshToken;
}
