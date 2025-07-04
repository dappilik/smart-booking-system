package com.example.booking.service;

import com.example.booking.exception.SlotAlreadyBookedException;
import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.example.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SlotAvailabilityService slotAvailabilityService;
    private final EventsService eventsService;

    public Mono<Booking> createBooking(BookingRequest request) {
        String slotKey = buildSlotKey(request);
        return slotAvailabilityService.isSlotAvailable(slotKey)
                .flatMap(isAvailable -> {
                    if (isAvailable) {
                        Booking booking = buildBooking(request);
                        return bookingRepository.save(booking)
                                .doOnSuccess(savedBooking -> {
                                    slotAvailabilityService.markSlotAsBooked(slotKey).subscribe();
                                    eventsService.sendBookingEvent(savedBooking).subscribe();
                                });
                    } else {
                        return Mono.error(new SlotAlreadyBookedException("Slot already booked"));
                    }
                });
    }

    public Mono<Booking> getBooking(Long id) {
        return bookingRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Booking not found")));
    }

    public Flux<Booking> getBookings(String email) {
        if (email != null && !email.isEmpty()) {
            return getBookingsByEmail(email);
        } else {
            return getBookings();
        }
    }

    private Flux<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    private Flux<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByUserEmail(email);
    }

    private String buildSlotKey(BookingRequest request) {
        return "slot:" + request.getSlot();
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
