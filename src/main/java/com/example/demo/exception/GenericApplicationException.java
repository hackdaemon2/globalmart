package com.example.demo.exception;

import lombok.Getter;

@Getter
public class GenericApplicationException extends RuntimeException {

    private final String message;

    public GenericApplicationException(String message) {
        super(message);
        this.message = message;
    }
}
