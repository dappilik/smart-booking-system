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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        assertTrue(slotAvailabilityService.isSlotAvailable("slot1"));
    }

    @Test
    void testIsSlotAvailable_WhenSlotIsNull() {
        when(valueOperations.get("slot2")).thenReturn(null);
        assertTrue(slotAvailabilityService.isSlotAvailable("slot2"));
    }

    @Test
    void testIsSlotAvailable_WhenSlotIsBooked() {
        when(valueOperations.get("slot3")).thenReturn("booked");
        assertFalse(slotAvailabilityService.isSlotAvailable("slot3"));
    }

    @Test
    void testIsSlotAvailable_WhenSlotIsAvailableCaseInsensitive() {
        when(valueOperations.get("slot4")).thenReturn("AVAILABLE");
        assertTrue(slotAvailabilityService.isSlotAvailable("slot4"));
    }

    @Test
    void testMarkSlotAsBooked() {
        slotAvailabilityService.markSlotAsBooked("slot5");
        verify(valueOperations, times(1)).set("slot5", "booked");
    }
}

