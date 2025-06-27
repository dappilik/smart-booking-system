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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

        given(slotAvailabilityService.isSlotAvailable(anyString())).willReturn(true);
        given(bookingRepository.save(any())).willReturn(booking);

        Booking result = bookingService.createBooking(request);

        assertNotNull(result);
        assertEquals("CONFIRMED", result.getStatus());
        verify(slotAvailabilityService).isSlotAvailable(anyString());
        verify(bookingRepository).save(any());
        verify(eventsService).sendBookingEvent(any());
    }


    @DisplayName("Should throw exception when slot is not available")
    @Test
    void testCreateBooking_SlotNotAvailable() {
        BookingRequest request = createBookingRequest();

        given(slotAvailabilityService.isSlotAvailable(anyString())).willReturn(false);

        assertThrows(IllegalStateException.class, () -> bookingService.createBooking(request));
        verify(slotAvailabilityService).isSlotAvailable(anyString());
        verifyNoInteractions(bookingRepository, eventsService);
    }

    @Test
    @DisplayName("Should return booking by id")
    void testGetBooking_Found() {
        Booking booking = Booking.builder().userEmail("test@example.com").build();
        given(bookingRepository.findById(1L)).willReturn(Optional.of(booking));

        Booking result = bookingService.getBooking(1L);

        assertNotNull(result);
        assertEquals("test@example.com", result.getUserEmail());
        verify(bookingRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when booking not found")
    void testGetBooking_NotFound() {
        given(bookingRepository.findById(2L)).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookingService.getBooking(2L));
        verify(bookingRepository).findById(2L);
    }

    @Test
    @DisplayName("Should return bookings by email")
    void testGetBookingsByEmail() {
        Booking booking = Booking.builder().userEmail("test@example.com").build();
        given(bookingRepository.findByUserEmail("test@example.com")).willReturn(List.of(booking));

        List<Booking> result = bookingService.getBookings("test@example.com");

        assertFalse(result.isEmpty());
        assertEquals("test@example.com", result.getFirst().getUserEmail());
        verify(bookingRepository).findByUserEmail("test@example.com");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should return all bookings")
    void testGetBookings(String email) {
        Booking booking = Booking.builder().userEmail("test@example.com").build();
        given(bookingRepository.findAll()).willReturn(List.of(booking));

        List<Booking> result = bookingService.getBookings(email);

        assertFalse(result.isEmpty());
        verify(bookingRepository).findAll();
    }

    private static Booking createBookingResponse(BookingRequest request) {
        return Booking.builder()
                .userEmail(request.getUserEmail())
                .slot(request.getSlot())
                .bookingTime(LocalDateTime.now())
                .status("CONFIRMED")
                .build();
    }

    private static BookingRequest createBookingRequest() {
        return BookingRequest.builder()
                .userEmail("test@example.com")
                .slot(LocalDateTime.of(2024, 10, 1, 10, 0))
                .build();
    }

}