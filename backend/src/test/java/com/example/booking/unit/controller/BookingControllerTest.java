package com.example.booking.unit.controller;

import com.example.booking.controller.BookingController;
import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.example.booking.service.BookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    BookingService bookingService;

    @InjectMocks
    BookingController bookingController;

    static BookingRequest bookingRequest() {
        return BookingRequest.builder()
                .userEmail("user@example.com")
                .slot(LocalDateTime.of(2024, 10, 1, 10, 0))
                .build();
    }

    static Booking booking() {
        return Booking.builder()
                .userEmail("user@example.com")
                .slot(LocalDateTime.of(2024, 10, 1, 10, 0))
                .bookingTime(LocalDateTime.now())
                .status("CONFIRMED")
                .build();
    }

    @ParameterizedTest
    @MethodSource("provideBookingRequests")
    @DisplayName("Should create booking for valid requests")
    void testCreateBooking(BookingRequest request, Booking expected) {
        when(bookingService.createBooking(request)).thenReturn(expected);

        ResponseEntity<Booking> response = bookingController.createBooking(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(bookingService).createBooking(request);
    }

    static Stream<Arguments> provideBookingRequests() {
        return Stream.of(
                Arguments.of(bookingRequest(), booking())
        );
    }

    @ParameterizedTest
    @CsvSource({"1", "2", "3"})
    @DisplayName("Should get booking by id")
    void testGetBooking(Long id) {
        Booking expected = booking();
        when(bookingService.getBooking(id)).thenReturn(expected);

        ResponseEntity<Booking> response = bookingController.getBooking(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(bookingService).getBooking(id);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"user@example.com"})
    @DisplayName("Should get bookings with and without email param")
    void testGetBookings(String email) {
        List<Booking> bookings = List.of(booking());
        when(bookingService.getBookings(email)).thenReturn(bookings);
        ResponseEntity<List<Booking>> response = bookingController.getBookings(email);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(bookings, response.getBody());
        verify(bookingService).getBookings(email);
    }
}
