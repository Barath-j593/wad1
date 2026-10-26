package com.example.booking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bookings")
public class Booking {
    @Id
    private String bookingId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private RoomType roomType;
    private String checkInDate;
    private String checkOutDate;
    private int roomsBooked;

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }
    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }
    public int getRoomsBooked() { return roomsBooked; }
    public void setRoomsBooked(int roomsBooked) { this.roomsBooked = roomsBooked; }
}
