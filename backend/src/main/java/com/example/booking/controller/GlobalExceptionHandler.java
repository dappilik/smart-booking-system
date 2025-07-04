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

    @ExceptionHandler({ServerWebInputException.class, WebExchangeBindException.class, SlotAlreadyBookedException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();
        switch (ex) {
            case WebExchangeBindException bindException -> {
                error.put("status", HttpStatus.BAD_REQUEST.value());
                error.put("error", "Bad Request");
                error.put("message", "Validation failed");
                bindException.getFieldErrors().forEach(fieldError ->
                        fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
                );
            }
            case ServerWebInputException swe -> {
                error.put("status", HttpStatus.BAD_REQUEST.value());
                error.put("error", "Bad Request");
                Throwable mostSpecificCause = swe.getMostSpecificCause();
                String msg = mostSpecificCause != null ? mostSpecificCause.getMessage() : null;
                if (msg == null) {
                    error.put("message", null);
                } else if (msg.contains("Unexpected character") || msg.contains("was expecting")) {
                    error.put("message", "Malformed JSON request body");
                } else if (msg.contains("Text '") && msg.contains("' could not be parsed")) {
                    int start = msg.indexOf("Text '") + 6;
                    int end = msg.indexOf("' could not be parsed");
                    if (start > 5 && end > start) {
                        String invalidValue = msg.substring(start, end);
                        fieldErrors.put("slot", String.format("Invalid value '%s', must match format yyyy-MM-dd'T'HH:mm:ss", invalidValue));
                    }
                    error.put("message", msg);
                } else {
                    error.put("message", msg);
                }
            }
            case SlotAlreadyBookedException slotAlreadyBookedException -> {
                error.put("status", HttpStatus.CONFLICT.value());
                error.put("error", "Conflict");
                error.put("message", ex.getMessage());
            }
            default -> {
                error.put("status", HttpStatus.BAD_REQUEST.value());
                error.put("error", "Bad Request");
                error.put("message", ex.getMessage());
            }
        }
        if(!fieldErrors.isEmpty()){
            error.put("fieldErrors", fieldErrors);
        }

        return new ResponseEntity<>(error, error.get("status").equals(HttpStatus.CONFLICT.value()) ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST);
    }
}
