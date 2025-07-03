package com.example.booking.kafka;

import com.example.booking.model.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final ObjectMapper objectMapper;

    // For test verification: store received events
    public static final List<Booking> receivedEvents = new CopyOnWriteArrayList<>();

    @KafkaListener(topics = "booking-events", groupId = "booking-consumer-group", containerFactory = "kafkaListenerFactory")
    public void consume(String message) {
        try {
            Booking booking = objectMapper.readValue(message, Booking.class);
            log.info("\uD83D\uDCE5 Received booking event: {}", booking);
            receivedEvents.add(booking); // Add to test event collector

            // Example: simulate sending email
            log.info("📧 Sending email to {}", booking.getUserEmail());

            // You can also: save to audit DB, call external service, etc.

        } catch (Exception e) {
            log.error("❌ Failed to process booking event", e);
        }
    }
}
