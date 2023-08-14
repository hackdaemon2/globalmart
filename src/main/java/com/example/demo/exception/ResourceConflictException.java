package com.example.demo.exception;

import lombok.Getter;

@Getter
public class ResourceConflictException extends RuntimeException {

    private String message;

    public ResourceConflictException(String message) {
        super(message);
        this.message = message;
    }
}
