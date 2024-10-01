package com.globalmart.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodes {

    SUCCESS("0", "Success"),
    FAILURE("1", "Failure"),
    INVALID_REFRESH_TOKEN("2", "Invalid refresh token");

    private final String responseCode;
    private final String responseMessage;

}
