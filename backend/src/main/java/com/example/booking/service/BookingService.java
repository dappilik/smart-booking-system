package com.example.booking.service;

import com.example.booking.kafka.BookingProducer;
import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.example.booking.repository.BookingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SlotAvailabilityService slotAvailabilityService;
    private final BookingProducer bookingProducer;
    private final ObjectMapper objectMapper;

    public Booking createBooking(BookingRequest request) {
        System.out.println("Received slot: " + request.getSlot());
        String slotKey = "slot:" + request.getSlot();
        try {
            if (!slotAvailabilityService.isSlotAvailable(slotKey)) {
                throw new IllegalStateException("Slot already booked");
            }
        } catch (Exception e) {
            log.error("Error checking slot availability: {}", e.getMessage(), e);
        }
        Booking booking = Booking.builder()
                .userEmail(request.getUserEmail())
                .slot(request.getSlot())
                .bookingTime(LocalDateTime.now())
                .status("CONFIRMED") // In real flow, maybe "PENDING"
                .build();

        Booking saved = bookingRepository.save(booking);
        try {
            log.info("Booking saved: {}", objectMapper.writeValueAsString(saved));
            slotAvailabilityService.markSlotAsBooked(slotKey);
        } catch (JsonProcessingException e) {
            log.error("Error while logging booking: {}", e.getMessage(), e);
        }

        try {
            String eventJson = objectMapper.writeValueAsString(saved);
            bookingProducer.sendBookingEvent(eventJson);
        } catch (JsonProcessingException e) {
            log.error("Error while procesing json {}", e.getMessage(), e);
        }

        return saved;
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByUserEmail(email);
    }

    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }
}

