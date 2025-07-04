package com.example.booking.controller;

import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.example.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Mono<Booking> createBooking(@Valid @RequestBody BookingRequest request) {
        log.info("Received booking request: {}", request);
        return bookingService.createBooking(request);
    }

    @GetMapping("/{id}")
    public Mono<Booking> getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id);
    }

    @GetMapping(produces = {"application/json", "text/event-stream"})
    public Flux<Booking> getBookings(@RequestParam(required = false) String email,
                                     @RequestParam(required = false, defaultValue = "false") boolean stream) {
        Flux<Booking> bookings = bookingService.getBookings(email);
        log.info("Fetching bookings for email: {}, stream: {}", email, stream);
        if (stream) {
            // Add delay to simulate streaming
            return bookings.delayElements(java.time.Duration.ofSeconds(1));
        } else {
            return bookings.collectList().flatMapMany(Flux::fromIterable);
        }
    }
}
