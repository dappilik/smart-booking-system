package com.example.booking.integration;

import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.example.booking.repository.BookingRepository;
import com.example.booking.service.BookingService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class BookingServiceIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

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

        Booking saved = bookingService.createBooking(bookingRequest);
        assertNotNull(saved.getId());
        assertEquals(TEST_EMAIL, saved.getUserEmail());

        bookingId = saved.getId(); // Save for later use
    }

    @Test
    @Order(2)
    void getBooking() {
        Booking fetched = bookingService.getBooking(bookingId);
        assertNotNull(fetched);
        assertEquals(TEST_EMAIL, fetched.getUserEmail());
    }

    @Test
    @Order(3)
    void getBookingsByEmail() {
        List<Booking> bookings = bookingService.getBookingsByEmail(TEST_EMAIL);
        assertFalse(bookings.isEmpty());
        assertEquals(TEST_EMAIL, bookings.getFirst().getUserEmail());
    }

    @Test
    @Order(4)
    void getBookings() {
        List<Booking> all = bookingService.getBookings();
        assertFalse(all.isEmpty());
    }
}