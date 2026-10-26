# Room Booking Backend

Simple Spring Boot + Maven + MongoDB REST API. Check-in and check-out dates are stored as `String` and should use `YYYY-MM-DD`.

## Requirements
- Java 17+
- Maven 3.9+
- MongoDB running locally on port 27017

## Run
1. Start MongoDB.
2. From this project directory, run:
   ```bash
   mvn spring-boot:run
   ```
3. Run tests:
   ```bash
   mvn test
   ```

MongoDB database: `roomdb` (configured in `src/main/resources/application.properties`).

## Routes

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/rooms` | Add room record |
| GET | `/rooms` | Get all rooms |
| GET | `/rooms/types` | Get enum room types |
| POST | `/bookings` | Create booking and reduce availability |
| GET | `/bookings` | Get all bookings |
| GET | `/bookings/count-by-type` | Count booking documents by room type |

## Example: add room
POST `/rooms`, Content-Type `application/json`:
```json
{
  "roomType": "DELUXE",
  "checkInDate": "2026-11-01",
  "checkOutDate": "2026-11-10",
  "availabilityCount": 10,
  "availabilityStatus": "AVAILABLE",
  "capacity": 3
}
```

## Example: create booking
POST `/bookings`:
```json
{
  "customerName": "Arun",
  "customerEmail": "arun@example.com",
  "customerPhone": "9876543210",
  "roomType": "DELUXE",
  "checkInDate": "2026-11-02",
  "checkOutDate": "2026-11-05",
  "roomsBooked": 2
}
```

A successful booking reduces the first room document found for that room type. This is a teaching/demo implementation: it does not calculate availability by overlapping dates and is not safe for concurrent production bookings. For date-specific inventory, model inventory per date/room type and use atomic updates or MongoDB transactions.

## Notes
- The booking count endpoint counts booking documents, not the sum of `roomsBooked`.
- The MockMvc tests mock the service layer, so they test controllers without requiring a running MongoDB instance.
