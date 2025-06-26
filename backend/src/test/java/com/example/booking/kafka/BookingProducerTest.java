package com.example.booking.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private BookingProducer bookingProducer;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        reset(kafkaTemplate);
    }

    @Test
    void testSendBookingEvent() {
        String bookingJson = "{\"id\":1,\"userEmail\":\"test@example.com\"}";
        bookingProducer.sendBookingEvent(bookingJson);
        verify(kafkaTemplate, times(1)).send("booking-events", bookingJson);
    }
}
