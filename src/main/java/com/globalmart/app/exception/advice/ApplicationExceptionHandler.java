package com.globalmart.app.exception.advice;

import com.globalmart.app.enums.ResponseCodes;
import com.globalmart.app.exception.*;
import com.globalmart.app.models.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.Objects;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return new ResponseEntity<>(formulateErrorResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleEnumException(MethodArgumentTypeMismatchException ex) {
        Class<?> requiredType = ex.getRequiredType();
        switch (Objects.requireNonNull(requiredType).getSimpleName()) {
            case "SortOrder":
                return ResponseEntity.badRequest()
                                     .body(formulateErrorResponse("Invalid sort order: %s".formatted(ex.getValue())));
            case "LocalDateTime":
                String invalidValue = ex.getValue() != null ? ex.getValue().toString() : "null";
                String message = "Invalid date-time format: %s. Please use 'yyyy-MM-ddTHH:mm:ss' format.".formatted(invalidValue);
                return ResponseEntity.badRequest()
                                     .body(formulateErrorResponse(message));
            default:
                return ResponseEntity.badRequest()
                                     .body(formulateErrorResponse("Invalid request parameter."));
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        return new ResponseEntity<>(formulateErrorResponse(exception.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(GenericApplicationException.class)
    public ResponseEntity<ErrorResponse> handleGenericApplicationException(GenericApplicationException exception) {
        return ResponseEntity.internalServerError()
                             .body(formulateErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception) {
        return ResponseEntity.badRequest()
                             .body(formulateErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ResponseEntity<>(formulateErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResponse> handleResourceConflictException(ResourceConflictException exception) {
        return new ResponseEntity<>(formulateErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(DateTimeParseException exception) {
        return ResponseEntity.internalServerError()
                             .body(formulateErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException exception) {
        return ResponseEntity.internalServerError()
                             .body(formulateErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException exception) {
        return ResponseEntity.internalServerError()
                             .body(formulateErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.internalServerError()
                             .body(formulateErrorResponse(exception.getMessage()));
    }

    private static ErrorResponse formulateErrorResponse(String message) {
        return new ErrorResponse(ResponseCodes.FAILURE.getResponseCode(), message);
    }

}
