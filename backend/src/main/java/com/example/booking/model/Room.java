package com.example.booking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rooms")
public class Room {
    @Id
    private String roomId;
    private RoomType roomType;
    private String checkInDate;
    private String checkOutDate;
    private int availabilityCount;
    private String availabilityStatus;
    private int capacity;

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }
    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }
    public int getAvailabilityCount() { return availabilityCount; }
    public void setAvailabilityCount(int availabilityCount) { this.availabilityCount = availabilityCount; }
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}
