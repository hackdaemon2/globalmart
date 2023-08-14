package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodes {

    SUCCESS("00", "Success"),
    FAILURE("01", "Failure"),
    INVALID_REFRESH_TOKEN("01", "Invalid refresh token");

    private final String responseCode;
    private final String responseMessage;
}
