package com.example.booking.repository;

import com.example.booking.model.Booking;
import com.example.booking.model.BookingCount;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    @Aggregation(pipeline = {
        "{ '$group': { '_id': '$roomType', 'count': { '$sum': 1 } } }",
        "{ '$project': { '_id': 0, 'roomType': '$_id', 'count': 1 } }",
        "{ '$sort': { 'roomType': 1 } }"
    })
    List<BookingCount> countBookingsByRoomType();
}

//    @Aggregation(pipeline = {
//         "{ '$lookup': { 'from': 'rooms', 'localField': 'roomId', 'foreignField': 'roomId', 'as': 'roomDetails' } }",
//         "{ '$unwind': '$roomDetails' }",
//         "{ '$group': { '_id': '$roomDetails.roomType', 'count': { '$sum': 1 } } }",
//         "{ '$project': { '_id': 0, 'roomType': '$_id', 'count': 1 } }",
//         "{ '$sort': { 'roomType': 1 } }"
//     })
//     List<BookingCount> countBookingsByRoomType();

// @Aggregation(pipeline = {
//     "{ '$group': { '_id': { 'roomType': '$roomType', 'checkInDate': '$checkInDate' }, 'count': { '$sum': 1 } } }",
//     "{ '$project': { '_id': 0, 'roomType': '$_id.roomType', 'checkInDate': '$_id.checkInDate', 'count': 1 } }",
//     "{ '$sort': { 'roomType': 1, 'checkInDate': 1 } }"
// })
// List<BookingCount> countBookingsByRoomTypeAndDate();