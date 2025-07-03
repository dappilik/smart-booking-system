package com.example.booking.service;

import com.example.booking.kafka.BookingProducer;
import com.example.booking.model.Booking;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventsService {
    private final BookingProducer bookingProducer;
    private final ObjectMapper objectMapper;

    public Mono<Void> sendBookingEvent(Booking booking) {
        return Mono.fromRunnable(() -> {
            try {
                String eventJson = objectMapper.writeValueAsString(booking);
                bookingProducer.sendBookingEvent(eventJson);
            } catch (JsonProcessingException e) {
                log.error("Error while processing json {}", e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
    }
}
