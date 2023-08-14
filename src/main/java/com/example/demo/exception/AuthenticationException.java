package com.example.demo.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {

    private String message;

    public AuthenticationException(String message) {
        super(message);
        this.message = message;
    }
}
