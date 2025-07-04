package com.example.booking.unit.service;

import com.example.booking.kafka.BookingProducer;
import com.example.booking.model.Booking;
import com.example.booking.service.EventsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventsServiceTest {
    @Mock
    private BookingProducer bookingProducer;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EventsService eventsService;

    @BeforeEach
    void setUp() {
        reset(bookingProducer, objectMapper);
    }

    @Test
    void sendBookingEvent_success() throws Exception {
        Booking booking = mock(Booking.class);
        String json = "{\"id\":1}";
        when(objectMapper.writeValueAsString(booking)).thenReturn(json);

        StepVerifier.create(eventsService.sendBookingEvent(booking))
                .verifyComplete();

        verify(objectMapper).writeValueAsString(booking);
        verify(bookingProducer).sendBookingEvent(json);
    }

    @Test
    void sendBookingEvent_jsonProcessingException() throws Exception {
        Booking booking = mock(Booking.class);
        when(objectMapper.writeValueAsString(booking)).thenThrow(new JsonProcessingException("error") {
        });

        StepVerifier.create(eventsService.sendBookingEvent(booking))
                .expectError(RuntimeException.class)
                .verify();

        verify(objectMapper).writeValueAsString(booking);
        verify(bookingProducer, never()).sendBookingEvent(anyString());
        // Optionally verify logging if log is injected
    }
}
