package com.example.booking.unit.service;

import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.example.booking.repository.BookingRepository;
import com.example.booking.service.BookingService;
import com.example.booking.service.EventsService;
import com.example.booking.service.SlotAvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;
    @Mock
    SlotAvailabilityService slotAvailabilityService;
    @Mock
    EventsService eventsService;


    @InjectMocks
    BookingService bookingService;

    @BeforeEach
    void resetMocks() {
        reset(bookingRepository, slotAvailabilityService, eventsService);
    }


    @DisplayName("Should create booking when slot is available")
    @Test
    void testCreateBooking_SlotAvailable() {
        BookingRequest request = createBookingRequest();
        Booking booking = createBookingResponse(request);

        given(slotAvailabilityService.isSlotAvailable(anyString())).willReturn(Mono.just(true));
        given(bookingRepository.save(any())).willReturn(Mono.just(booking));
        given(eventsService.sendBookingEvent(any())).willReturn(Mono.empty());
        given(slotAvailabilityService.markSlotAsBooked(anyString())).willReturn(Mono.empty());

        StepVerifier.create(bookingService.createBooking(request))
                .expectNextMatches(b -> b.getStatus().equals("CONFIRMED"))
                .verifyComplete();

        verify(slotAvailabilityService).isSlotAvailable(anyString());
        verify(bookingRepository).save(any());
        verify(eventsService).sendBookingEvent(any());
        verify(slotAvailabilityService).markSlotAsBooked(anyString());
    }


    @DisplayName("Should throw exception when slot is not available")
    @Test
    void testCreateBooking_SlotNotAvailable() {
        BookingRequest request = createBookingRequest();
        given(slotAvailabilityService.isSlotAvailable(anyString())).willReturn(Mono.just(false));

        StepVerifier.create(bookingService.createBooking(request))
                .expectError(IllegalStateException.class)
                .verify();
    }

    @DisplayName("Should get booking by id")
    @Test
    void testGetBooking() {
        Booking booking = createBookingResponse(createBookingRequest());
        when(bookingRepository.findById(anyLong())).thenReturn(Mono.just(booking));

        StepVerifier.create(bookingService.getBooking(1L))
                .expectNext(booking)
                .verifyComplete();
    }

    @DisplayName("Should throw exception when booking not found")
    @Test
    void testGetBooking_NotFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(bookingService.getBooking(1L))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @DisplayName("Should get bookings by email")
    @Test
    void testGetBookingsByEmail() {
        Booking booking = createBookingResponse(createBookingRequest());
        when(bookingRepository.findByUserEmail(anyString())).thenReturn(Flux.just(booking));

        StepVerifier.create(bookingService.getBookings("test@example.com"))
                .expectNext(booking)
                .verifyComplete();
    }

    @DisplayName("Should get all bookings when email is null or empty")
    @ParameterizedTest
    @NullAndEmptySource
    void testGetBookings_All(String email) {
        Booking booking = createBookingResponse(createBookingRequest());
        when(bookingRepository.findAll()).thenReturn(Flux.just(booking));

        StepVerifier.create(bookingService.getBookings(email))
                .expectNext(booking)
                .verifyComplete();
    }

    // Helper methods for test data
    private BookingRequest createBookingRequest() {
        BookingRequest request = new BookingRequest();
        request.setUserEmail("test@example.com");
        request.setSlot(LocalDateTime.parse("2025-07-01T10:00:00"));
        return request;
    }

    private Booking createBookingResponse(BookingRequest request) {
        Booking booking = new Booking();
        booking.setUserEmail(request.getUserEmail());
        booking.setSlot(request.getSlot());
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        return booking;
    }

}