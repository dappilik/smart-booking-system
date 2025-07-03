package com.example.booking.repository;

import com.example.booking.model.Booking;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BookingRepository extends ReactiveCrudRepository<Booking, Long> {
    Flux<Booking> findByUserEmail(String userEmail);
}
