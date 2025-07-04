package com.example.booking.unit.controller;

import com.example.booking.controller.BookingController;
import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.example.booking.service.BookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideBookingRequests")
    @DisplayName("Should create booking for valid requests")
    void testCreateBooking(@SuppressWarnings("unused") String scenario, BookingRequest request, Booking expected) {
        // Arrange
        given(bookingService.createBooking(request)).willReturn(Mono.just(expected));

        // Act & Assert
        StepVerifier.create(bookingController.createBooking(request))
                .expectNext(expected)
                .verifyComplete();
        verify(bookingService).createBooking(request);
    }

    static Stream<Arguments> provideBookingRequests() {
        return Stream.of(
                Arguments.of("basic valid booking", bookingRequest(), booking())
        );
    }

    @ParameterizedTest
    @CsvSource({"1", "2", "3"})
    @DisplayName("Should get booking by id")
    void testGetBooking(Long id) {
        Booking expected = booking();
        given(bookingService.getBooking(id)).willReturn(Mono.just(expected));

        StepVerifier.create(bookingController.getBooking(id))
                .expectNext(expected)
                .verifyComplete();
        verify(bookingService).getBooking(id);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"user@example.com"})
    @DisplayName("Should get bookings with and without email param (non-stream)")
    void testGetBookings_NonStream(String email) {
        Booking booking = booking();
        given(bookingService.getBookings(email)).willReturn(Flux.just(booking));
        StepVerifier.create(bookingController.getBookings(email, false))
                .expectNext(booking)
                .verifyComplete();
        verify(bookingService).getBookings(email);
    }

    @Test
    @DisplayName("Should get bookings as stream (stream=true)")
    void testGetBookings_Stream() {
        String email = "user@example.com";
        Booking booking = booking();
        given(bookingService.getBookings(email)).willReturn(Flux.just(booking));
        StepVerifier.create(bookingController.getBookings(email, true))
                .expectNext(booking)
                .verifyComplete();
        verify(bookingService).getBookings(email);
    }
}
