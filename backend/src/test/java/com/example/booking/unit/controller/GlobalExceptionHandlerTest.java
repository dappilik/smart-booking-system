package com.example.booking.unit.controller;

import com.example.booking.controller.GlobalExceptionHandler;
import com.example.booking.exception.SlotAlreadyBookedException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBadRequest_withWebExchangeBindException() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        when(ex.getFieldErrors()).thenReturn(java.util.List.of());
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Validation failed", response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withServerWebInputException_malformedJson() {
        Throwable cause = new RuntimeException("Unexpected character");
        ServerWebInputException ex = mock(ServerWebInputException.class);
        when(ex.getMostSpecificCause()).thenReturn(cause);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Malformed JSON request body", response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withServerWebInputException_invalidDate() {
        String msg = "Text '2023-13-01T10:00:00' could not be parsed at index 5";
        Throwable cause = new RuntimeException(msg);
        ServerWebInputException ex = mock(ServerWebInputException.class);
        when(ex.getMostSpecificCause()).thenReturn(cause);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().get("message").toString().contains("could not be parsed"));
        assertTrue(((Map<?, ?>)response.getBody().get("fieldErrors")).containsKey("slot"));
    }

    @Test
    void handleBadRequest_withSlotAlreadyBookedException() {
        SlotAlreadyBookedException ex = new SlotAlreadyBookedException("Slot already booked");
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertEquals("Conflict", response.getBody().get("error"));
        assertEquals("Slot already booked", response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withOtherException() {
        Exception ex = new Exception("Some error");
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Some error", response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withWebExchangeBindException_multipleFieldErrors() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        org.springframework.validation.FieldError fieldError1 = new org.springframework.validation.FieldError("object", "field1", "must not be null");
        org.springframework.validation.FieldError fieldError2 = new org.springframework.validation.FieldError("object", "field2", "must be a date");
        when(ex.getFieldErrors()).thenReturn(java.util.List.of(fieldError1, fieldError2));
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Validation failed", response.getBody().get("message"));
        assertTrue(((Map<?, ?>)response.getBody().get("fieldErrors")).containsKey("field1"));
        assertTrue(((Map<?, ?>)response.getBody().get("fieldErrors")).containsKey("field2"));
    }

    @Test
    void handleBadRequest_withServerWebInputException_otherMessage() {
        Throwable cause = new RuntimeException("Some other error");
        ServerWebInputException ex = mock(ServerWebInputException.class);
        when(ex.getMostSpecificCause()).thenReturn(cause);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Some other error", response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withNullMessage() {
        ServerWebInputException ex = mock(ServerWebInputException.class);
        when(ex.getMostSpecificCause()).thenReturn(null);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withSlotAlreadyBookedException_nullMessage() {
        SlotAlreadyBookedException ex = new SlotAlreadyBookedException(null);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertEquals("Conflict", response.getBody().get("error"));
        assertNull(response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withOtherException_nullMessage() {
        Exception ex = new Exception((String) null);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertNull(response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withFieldErrorsEmpty() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        when(ex.getFieldErrors()).thenReturn(java.util.Collections.emptyList());
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().containsKey("fieldErrors"));
    }

    @Test
    void handleBadRequest_withWebExchangeBindException_fieldErrorNullMessage() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        org.springframework.validation.FieldError fieldError = new org.springframework.validation.FieldError("object", "field1", null);
        when(ex.getFieldErrors()).thenReturn(java.util.List.of(fieldError));
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().get("message"));
        assertTrue(((Map<?, ?>)response.getBody().get("fieldErrors")).containsKey("field1"));
        assertNull(((Map<?, ?>)response.getBody().get("fieldErrors")).get("field1"));
    }

    @Test
    void handleBadRequest_withServerWebInputException_parsingButNoText() {
        String msg = "could not be parsed at index 5";
        Throwable cause = new RuntimeException(msg);
        ServerWebInputException ex = mock(ServerWebInputException.class);
        when(ex.getMostSpecificCause()).thenReturn(cause);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(msg, response.getBody().get("message"));
        assertFalse(response.getBody().containsKey("fieldErrors"));
    }

    @Test
    void handleBadRequest_withServerWebInputException_wasExpecting() {
        Throwable cause = new RuntimeException("was expecting double-quote to start field name");
        ServerWebInputException ex = mock(ServerWebInputException.class);
        when(ex.getMostSpecificCause()).thenReturn(cause);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Malformed JSON request body", response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withSlotAlreadyBookedException_emptyMessage() {
        SlotAlreadyBookedException ex = new SlotAlreadyBookedException("");
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("", response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withOtherException_emptyMessage() {
        Exception ex = new Exception("");
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("", response.getBody().get("message"));
    }

    @Test
    void handleBadRequest_withOtherException_nullAndEmptyFieldErrors() {
        // Simulate a case where fieldErrors is empty and message is null
        Exception ex = new Exception((String) null);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertNull(response.getBody().get("message"));
        // fieldErrors should not be present
        assertFalse(response.getBody().containsKey("fieldErrors"));
    }

    @Test
    void handleBadRequest_withSlotAlreadyBookedException_nullAndEmptyFieldErrors() {
        // Simulate a case where fieldErrors is empty and message is null
        SlotAlreadyBookedException ex = new SlotAlreadyBookedException(null);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertEquals("Conflict", response.getBody().get("error"));
        assertNull(response.getBody().get("message"));
        // fieldErrors should not be present
        assertFalse(response.getBody().containsKey("fieldErrors"));
    }
}
