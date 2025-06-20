package com.example.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlotAvailabilityService {

    private final StringRedisTemplate redisTemplate;

    public boolean isSlotAvailable(String slotKey) {
        String value = redisTemplate.opsForValue().get(slotKey);
        return value == null || value.equalsIgnoreCase("available");
    }

    public void markSlotAsBooked(String slotKey) {
        redisTemplate.opsForValue().set(slotKey, "booked");
    }
}
