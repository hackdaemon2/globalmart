package com.example.demo.configurations;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.GenericApplicationException;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return new ResponseEntity<>(formulateErrorResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    private Map<String, Object> formulateErrorResponse(String message) {
        return Map.of("responseCode", "01", "responseMessage", message);
    }

    @ExceptionHandler(GenericApplicationException.class)
    public ResponseEntity<Object> handleGenericApplicationException(GenericApplicationException exception) {
        return new ResponseEntity<>(formulateErrorResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleGenericApplicationException(BadRequestException exception) {
        return new ResponseEntity<>(formulateErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ResponseEntity<>(formulateErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Map<String, Object>> handleResourceConflictException(ResourceConflictException exception) {
        return new ResponseEntity<>(formulateErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException exception) {
        return new ResponseEntity<>(formulateErrorResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
