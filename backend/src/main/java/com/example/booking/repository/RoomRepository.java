package com.example.booking.repository;

import com.example.booking.model.Room;
import com.example.booking.model.RoomType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoomRepository extends MongoRepository<Room, String> {
    Optional<Room> findFirstByRoomType(RoomType roomType);
}
