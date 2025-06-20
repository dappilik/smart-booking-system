package com.example.booking.model;

import lombok.Data;

@Data
public class BookingRequest {
    private String userEmail;
    private String slot;
}