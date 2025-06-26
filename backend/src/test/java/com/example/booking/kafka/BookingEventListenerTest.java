package com.example.booking.kafka;

import com.example.booking.model.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;


    @InjectMocks
    private BookingEventListener bookingEventListener;

    @BeforeEach
    void setUp() {
        reset(objectMapper);
    }

    @Test
    void testConsume_ValidMessage() throws Exception {
        String message = "{\"id\":1,\"userEmail\":\"test@example.com\"}";
        Booking booking = mock(Booking.class);
        when(objectMapper.readValue(message, Booking.class)).thenReturn(booking);
        when(booking.getUserEmail()).thenReturn("test@example.com");

        bookingEventListener.consume(message);

        verify(objectMapper, times(1)).readValue(message, Booking.class);
        verify(booking, times(1)).getUserEmail();
        verifyNoMoreInteractions(objectMapper, booking);
    }

    @Test
    void testConsume_InvalidMessage() throws Exception {
        String message = "invalid-json";
        when(objectMapper.readValue(message, Booking.class)).thenThrow(new RuntimeException("JSON error"));

        bookingEventListener.consume(message);

        verify(objectMapper, times(1)).readValue(message, Booking.class);
        verifyNoMoreInteractions(objectMapper);
    }

    @Test
    void testConsume_MessageWithMissingEmail() throws Exception {
        String message = "{\"id\":2}";
        Booking booking = mock(Booking.class);
        when(objectMapper.readValue(message, Booking.class)).thenReturn(booking);
        when(booking.getUserEmail()).thenReturn(null);

        bookingEventListener.consume(message);

        verify(objectMapper, times(1)).readValue(message, Booking.class);
        verify(booking, times(1)).getUserEmail();
        verifyNoMoreInteractions(objectMapper, booking);
    }
}
