package com.example.booking.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "booking-events";

    public void sendBookingEvent(String bookingJson) {
        kafkaTemplate.send(TOPIC, bookingJson);
    }
}

