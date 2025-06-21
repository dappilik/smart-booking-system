package com.example.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    private LocalDateTime slot;

    private LocalDateTime bookingTime;

    private String status; // e.g., CONFIRMED, PENDING, FAILED
}
