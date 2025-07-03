package com.example.booking.integration;

import com.example.booking.config.BaseTestContainerConfig;
import com.example.booking.config.RedisTestConfig;
import com.example.booking.model.BookingRequest;
import com.example.booking.repository.BookingRepository;
import com.example.booking.service.BookingService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import({RedisTestConfig.class})
class BookingServiceIT extends BaseTestContainerConfig {

    @Autowired
    BookingService bookingService;

    @Autowired
    BookingRepository bookingRepository;

    private static final String TEST_EMAIL = "test@example.com";
    private static Long bookingId;

    @Test
    @Order(1)
    void createBooking() {
        BookingRequest bookingRequest = BookingRequest.builder()
                .userEmail(TEST_EMAIL)
                .slot(LocalDateTime.now().plusHours(2))
                .build();

        StepVerifier.create(bookingService.createBooking(bookingRequest))
                .assertNext(saved -> {
                    assertNotNull(saved.getId());
                    assertEquals(TEST_EMAIL, saved.getUserEmail());
                    bookingId = saved.getId();
                })
                .verifyComplete();
    }

    @Test
    @Order(2)
    void getBooking() {
        StepVerifier.create(bookingService.getBooking(bookingId))
                .assertNext(fetched -> {
                    assertNotNull(fetched);
                    assertEquals(TEST_EMAIL, fetched.getUserEmail());
                })
                .verifyComplete();
    }

    @Test
    @Order(3)
    void getBookingsByEmail() {
        StepVerifier.create(bookingService.getBookings(TEST_EMAIL).collectList())
                .assertNext(bookings -> {
                    assertFalse(bookings.isEmpty());
                    assertEquals(TEST_EMAIL, bookings.getFirst().getUserEmail());
                })
                .verifyComplete();
    }

    @Test
    @Order(4)
    void getBookings() {
        StepVerifier.create(bookingService.getBookings(null).collectList())
                .assertNext(all -> assertFalse(all.isEmpty()))
                .verifyComplete();
    }
}