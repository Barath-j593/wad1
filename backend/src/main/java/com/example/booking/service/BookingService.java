package com.example.booking.service;

import com.example.booking.model.*;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BookingService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public BookingService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public Room addRoom(Room room) {
        if (room.getAvailabilityCount() < 0 || room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Availability cannot be negative and capacity must be positive");
        }
        if (room.getAvailabilityStatus() == null || room.getAvailabilityStatus().isBlank()) {
            room.setAvailabilityStatus(room.getAvailabilityCount() > 0 ? "AVAILABLE" : "NOT_AVAILABLE");
        }
        return roomRepository.save(room);
    }

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public List<RoomType> getRoomTypes() {
        return Arrays.asList(RoomType.values());
    }

    public Booking addBooking(Booking booking) {
        if (booking.getRoomsBooked() <= 0) {
            throw new IllegalArgumentException("roomsBooked must be greater than zero");
        }
        if (booking.getCheckInDate() == null || booking.getCheckOutDate() == null
                || booking.getCheckInDate().isBlank() || booking.getCheckOutDate().isBlank()) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }
        // Dates are strings in this simple example and should use YYYY-MM-DD format.
        if (booking.getCheckInDate().compareTo(booking.getCheckOutDate()) >= 0) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        Room room = roomRepository.findFirstByRoomType(booking.getRoomType())
                .orElseThrow(() -> new IllegalArgumentException("Room type not found"));

        if (room.getAvailabilityCount() < booking.getRoomsBooked()) {
            throw new IllegalArgumentException("Not enough rooms available");
        }

        room.setAvailabilityCount(room.getAvailabilityCount() - booking.getRoomsBooked());
        room.setAvailabilityStatus(room.getAvailabilityCount() == 0 ? "NOT_AVAILABLE" : "AVAILABLE");
        roomRepository.save(room);

        try {
            return bookingRepository.save(booking);
        } catch (RuntimeException ex) {
            // Best-effort rollback for this simple demo if booking save fails.
            room.setAvailabilityCount(room.getAvailabilityCount() + booking.getRoomsBooked());
            room.setAvailabilityStatus("AVAILABLE");
            roomRepository.save(room);
            throw ex;
        }
    }

    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    public List<BookingCount> getBookingCounts() {
        return bookingRepository.countBookingsByRoomType();
    }
}
