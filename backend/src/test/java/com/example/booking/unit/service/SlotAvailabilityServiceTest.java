package com.example.booking.unit.service;

import com.example.booking.service.SlotAvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SlotAvailabilityServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private SlotAvailabilityService slotAvailabilityService;

    @BeforeEach
    void setUp() {
        reset(redisTemplate, valueOperations);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testIsSlotAvailable_WhenSlotIsAvailable() {
        when(valueOperations.get("slot1")).thenReturn("available");
        StepVerifier.create(slotAvailabilityService.isSlotAvailable("slot1"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testIsSlotAvailable_WhenSlotIsNull() {
        when(valueOperations.get("slot2")).thenReturn(null);
        StepVerifier.create(slotAvailabilityService.isSlotAvailable("slot2"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testIsSlotAvailable_WhenSlotIsBooked() {
        when(valueOperations.get("slot3")).thenReturn("booked");
        StepVerifier.create(slotAvailabilityService.isSlotAvailable("slot3"))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void testIsSlotAvailable_WhenSlotIsAvailableCaseInsensitive() {
        when(valueOperations.get("slot4")).thenReturn("AVAILABLE");
        StepVerifier.create(slotAvailabilityService.isSlotAvailable("slot4"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testMarkSlotAsBooked() {
        StepVerifier.create(slotAvailabilityService.markSlotAsBooked("slot5"))
                .verifyComplete();
        verify(valueOperations, times(1)).set("slot5", "booked");
    }
}
