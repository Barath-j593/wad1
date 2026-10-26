package com.example.booking.model;

public class BookingCount {
    private RoomType roomType;
    private long count;

    public BookingCount() {}

    public BookingCount(RoomType roomType, long count) {
        this.roomType = roomType;
        this.count = count;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
