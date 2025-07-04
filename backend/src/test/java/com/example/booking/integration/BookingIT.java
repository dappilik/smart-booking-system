package com.example.booking.integration;

import com.example.booking.config.BaseTestContainerConfig;
import com.example.booking.config.RedisTestConfig;
import com.example.booking.kafka.BookingEventListener;
import com.example.booking.model.Booking;
import com.example.booking.model.BookingRequest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureWebTestClient
@Import({RedisTestConfig.class})
class BookingIT extends BaseTestContainerConfig {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final String BASE_URL = "/api/bookings";
    private static final String TEST_EMAIL = "test@example.com";
    private static Long bookingId;
    private static LocalDateTime slot = LocalDateTime.now().plusHours(2);

    @Test
    @Order(1)
    void createBookingAndVerify() {
        BookingRequest bookingRequest = BookingRequest.builder()
                .userEmail(TEST_EMAIL)
                .slot(slot)
                .build();


        // Create booking and extract ID
        String bookingId = JsonPath.read(
                new String(
                        webTestClient.post()
                                .uri(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(bookingRequest)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .returnResult()
                                .getResponseBodyContent()
                ), "$.id").toString();

        assertNotNull(bookingId);

        // Get booking by ID
        webTestClient.get()
                .uri(BASE_URL + "/" + bookingId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(Integer.parseInt(bookingId))
                .jsonPath("$.userEmail").isEqualTo(TEST_EMAIL);

        // Verify Redis slot is marked as booked
        String slotKey = "slot:" + slot.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        Boolean slotBooked = stringRedisTemplate.hasKey(slotKey);
        assertNotNull(slotBooked);
        assertEquals(Boolean.TRUE, slotBooked);

        // Verify Kafka event was sent
        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    boolean eventReceived = BookingEventListener.receivedEvents.stream()
                            .anyMatch(b -> b.getId() != null && b.getId().toString().equals(bookingId));
                    assertEquals(true, eventReceived);
                });
    }

    @Test
    @Order(2)
    void getBookingsByEmail() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL).queryParam("email", TEST_EMAIL).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].userEmail").isEqualTo(TEST_EMAIL);
    }

    @Test
    @Order(3)
    void getBookings() {
        webTestClient.get()
                .uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].userEmail").exists();
    }

    @Test
    @Order(4)
    void getBookingsStream() {
        var result = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL).queryParam("email", TEST_EMAIL).queryParam("stream", true).build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .returnResult(com.example.booking.model.Booking.class);

        List<Booking> bookings = new java.util.concurrent.CopyOnWriteArrayList<>();
        result.getResponseBody().subscribe(bookings::add);

        // Await until at least one Booking with the expected userEmail is present
        await().atMost(java.time.Duration.ofSeconds(5)).untilAsserted(() -> {
            assertTrue(!bookings.isEmpty(), "Expected at least one booking in the stream");
            boolean found = bookings.stream().anyMatch(b -> TEST_EMAIL.equals(b.getUserEmail()));
            assertTrue(found, "Expected at least one Booking with userEmail=" + TEST_EMAIL);
        });
    }
}