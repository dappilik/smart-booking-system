package com.example.booking.kafka;

import com.example.booking.model.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "booking-events", groupId = "booking-consumer-group", containerFactory = "kafkaListenerFactory")
    public void consume(String message) {
        try {
            Booking booking = objectMapper.readValue(message, Booking.class);
            log.info("📥 Received booking event: {}", booking);

            // Example: simulate sending email
            log.info("📧 Sending email to {}", booking.getUserEmail());

            // You can also: save to audit DB, call external service, etc.

        } catch (Exception e) {
            log.error("❌ Failed to process booking event", e);
        }
    }
}
