package com.globalmart.app.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    CANCELED("CANCELED"),
    FULFILLED("FULFILLED");

    private final String status;

}
