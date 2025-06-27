package com.example.booking.controller;

import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.example.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        log.info("Received booking request: {}", request);
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping()
    public ResponseEntity<List<Booking>> getBookings(@RequestParam(required = false) String email) {
        return ResponseEntity.ok(bookingService.getBookings(email));
    }
}
