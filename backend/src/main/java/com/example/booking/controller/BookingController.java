package com.example.booking.controller;

import com.example.booking.model.Booking;
import com.example.booking.model.BookingCount;
import com.example.booking.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/bookings")

public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public Booking addBooking(@RequestBody Booking booking) {
        return service.addBooking(booking);
    }

    @GetMapping
    public List<Booking> getBookings() {
        return service.getBookings();
    }

    @GetMapping("/count-by-type")
    public List<BookingCount> getBookingCounts() {
        return service.getBookingCounts();
    }
}
