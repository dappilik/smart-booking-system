package com.example.booking.controller;

import com.example.booking.exception.SlotAlreadyBookedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @SuppressWarnings("ConstantConditions")
    @ExceptionHandler({ServerWebInputException.class, WebExchangeBindException.class, SlotAlreadyBookedException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();
        switch (ex) {
            case WebExchangeBindException bindException -> {
                error.put("status", HttpStatus.BAD_REQUEST.value());
                error.put("error", "Bad Request");
                error.put("message", bindException.getMessage());
                bindException.getFieldErrors().forEach(fieldError ->
                        fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
                );
            }
            case ServerWebInputException swe -> {
                error.put("status", HttpStatus.BAD_REQUEST.value());
                error.put("error", "Bad Request");
                if (swe.getMostSpecificCause() != null)
                    error.put("message", swe.getMostSpecificCause().getMessage());
            }
            case SlotAlreadyBookedException slotAlreadyBookedException -> {
                error.put("status", HttpStatus.CONFLICT.value());
                error.put("error", "Conflict");
                error.put("message", slotAlreadyBookedException.getMessage());
            }
            default -> {
                error.put("status", HttpStatus.BAD_REQUEST.value());
                error.put("error", "Bad Request");
                error.put("message", ex.getMessage());
            }
        }
        if (!fieldErrors.isEmpty()) {
            error.put("fieldErrors", fieldErrors);
        }
        return new ResponseEntity<>(error, error.get("status").equals(HttpStatus.CONFLICT.value()) ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST);
    }
}
