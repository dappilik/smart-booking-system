package com.example.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    private Long id;

    private String userEmail;

    private LocalDateTime slot;

    private LocalDateTime bookingTime;

    private String status; // e.g., CONFIRMED, PENDING, FAILED
}
