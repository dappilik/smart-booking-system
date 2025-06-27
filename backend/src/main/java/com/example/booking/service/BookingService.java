package com.example.booking.service;

import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.example.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SlotAvailabilityService slotAvailabilityService;
    private final EventsService eventsService;

    public Booking createBooking(BookingRequest request) {
        return Optional.of(request)
                .map(this::buildSlotKey)
                .filter(slotAvailabilityService::isSlotAvailable)
                .map(slotKey -> bookSlot(request, slotKey))
                .orElseThrow(() -> new IllegalStateException("Slot already booked"));
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public List<Booking> getBookings(String email) {
        if (email != null && !email.isEmpty()) {
            return getBookingsByEmail(email);
        } else {
            return getBookings();
        }
    }

    private List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    private List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByUserEmail(email);
    }

    private String buildSlotKey(BookingRequest request) {
        return "slot:" + request.getSlot();
    }

    private Booking bookSlot(BookingRequest request, String slotKey) {
        Booking booking = buildBooking(request);
        Booking savedBooking = bookingRepository.save(booking);
        slotAvailabilityService.markSlotAsBooked(slotKey);
        eventsService.sendBookingEvent(savedBooking);
        return savedBooking;
    }

    private Booking buildBooking(BookingRequest request) {
        return Booking.builder()
                .userEmail(request.getUserEmail())
                .slot(request.getSlot())
                .bookingTime(LocalDateTime.now())
                .status("CONFIRMED")
                .build();
    }

}
