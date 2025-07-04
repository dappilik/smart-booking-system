package com.example.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlotAvailabilityService {

    private final StringRedisTemplate redisTemplate;

    public Mono<Boolean> isSlotAvailable(String slotKey) {
        return Mono.fromCallable(() -> {
            String value = redisTemplate.opsForValue().get(slotKey);
            return value == null || value.equalsIgnoreCase("available");
        });
    }

    public Mono<Void> markSlotAsBooked(String slotKey) {
        return Mono.fromRunnable(() -> redisTemplate.opsForValue().set(slotKey, "booked"));
    }
}
