package com.example.booking.controller;

import com.example.booking.model.Room;
import com.example.booking.model.RoomType;
import com.example.booking.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final BookingService service;

    public RoomController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public Room addRoom(@RequestBody Room room) {
        return service.addRoom(room);
    }

    @GetMapping
    public List<Room> getRooms() {
        return service.getRooms();
    }

    @GetMapping("/types")
    public List<RoomType> getRoomTypes() {
        return service.getRoomTypes();
    }
}
