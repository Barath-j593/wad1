package com.example.booking;

import com.example.booking.model.*;
import com.example.booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({
    com.example.booking.controller.RoomController.class,
    com.example.booking.controller.BookingController.class
})
@Import(com.example.booking.controller.ApiExceptionHandler.class)
class BookingControllerTest {
    @Autowired MockMvc mockMvc;

    @MockBean BookingService service;

    @Test
    void addRoom() throws Exception {
        Room room = new Room();
        room.setRoomType(RoomType.DELUXE);
        when(service.addRoom(any(Room.class))).thenReturn(room);

        mockMvc.perform(post("/rooms").contentType(MediaType.APPLICATION_JSON).content("""
            {"roomType":"DELUXE","checkInDate":"2026-11-01","checkOutDate":"2026-11-10",
             "availabilityCount":10,"availabilityStatus":"AVAILABLE","capacity":3}
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.roomType").value("DELUXE"));
    }

    @Test
    void getRooms() throws Exception {
        when(service.getRooms()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/rooms")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
    }

    @Test
    void getRoomTypes() throws Exception {
        when(service.getRoomTypes()).thenReturn(Arrays.asList(RoomType.values()));
        mockMvc.perform(get("/rooms/types"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").value("STANDARD"))
            .andExpect(jsonPath("$[1]").value("DELUXE"))
            .andExpect(jsonPath("$[2]").value("SUITE"));
    }

    @Test
    void addBooking() throws Exception {
        Booking booking = new Booking();
        booking.setCustomerName("Arun");
        booking.setRoomType(RoomType.DELUXE);
        when(service.addBooking(any(Booking.class))).thenReturn(booking);

        mockMvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON).content("""
            {"customerName":"Arun","customerEmail":"arun@example.com","customerPhone":"9876543210",
             "roomType":"DELUXE","checkInDate":"2026-11-02","checkOutDate":"2026-11-05","roomsBooked":2}
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerName").value("Arun"));
    }

    @Test
    void getBookings() throws Exception {
        when(service.getBookings()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/bookings")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
    }

    @Test
    void getBookingCounts() throws Exception {
        when(service.getBookingCounts()).thenReturn(
            Collections.singletonList(new BookingCount(RoomType.DELUXE, 3)));
        mockMvc.perform(get("/bookings/count-by-type"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].roomType").value("DELUXE"))
            .andExpect(jsonPath("$[0].count").value(3));
    }

    @Test
    void invalidRoomCountReturnsBadRequest() throws Exception {
        when(service.addBooking(any(Booking.class)))
            .thenThrow(new IllegalArgumentException("roomsBooked must be greater than zero"));

        mockMvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON).content("""
            {"customerName":"Arun","roomType":"DELUXE","checkInDate":"2026-11-02",
             "checkOutDate":"2026-11-05","roomsBooked":0}
            """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("roomsBooked must be greater than zero"));
    }
}
