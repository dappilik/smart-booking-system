package com.example.booking.controller;

import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.example.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping()
    public ResponseEntity<List<Booking>> getBookings(@RequestParam(required = false) String email) {
        if (email != null && !email.isEmpty()) {
            return ResponseEntity.ok(bookingService.getBookingsByEmail(email));
        }
        return ResponseEntity.ok(bookingService.getBookings());
    }
}
