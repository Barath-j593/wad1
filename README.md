# Room Booking Management System

A full-stack room booking management system built with **Spring Boot (Java 21)**, **Maven**, **MongoDB**, and **Vue.js 3 (Vite)**.

---

## 🛠️ Technology Stack & Architecture

### Backend:
- **Language & Framework:** Java 21, Spring Boot 3.3.4
- **Build Tool:** Apache Maven
- **Database:** MongoDB (`rooms` and `bookings` collections)
- **Testing:** Spring Boot Test, JUnit 5, Mockito, MockMvc

#### Project Structure:
```
backend/
├── src/main/java/com/roombooking/
│   ├── RoomBookingApplication.java     # Application entrypoint
│   ├── config/
│   │   └── CorsConfig.java             # Global CORS configuration
│   ├── model/
│   │   ├── Room.java                   # Document for 'rooms' collection
│   │   ├── Booking.java                # Document for 'bookings' collection
│   │   ├── RoomType.java               # Enum: STANDARD, DELUXE, SUITE
│   │   └── AvailabilityStatus.java     # Enum: AVAILABLE, UNAVAILABLE
│   ├── dto/
│   │   ├── BookingRequest.java         # Payload for POST /api/bookings
│   │   ├── RoomTypeCountResponse.java  # Grouped count projection
│   │   └── ApiResponse.java            # Standardized API response
│   ├── repository/
│   │   ├── RoomRepository.java         # MongoRepository for rooms
│   │   └── BookingRepository.java      # MongoRepository with Aggregation
│   ├── service/
│   │   ├── RoomService.java            # Room business logic & validation
│   │   └── BookingService.java         # Booking availability logic & updates
│   └── controller/
│       ├── RoomController.java         # REST endpoints for rooms
│       └── BookingController.java      # REST endpoints for bookings
└── src/test/java/com/roombooking/
    ├── controller/
    │   ├── RoomControllerTest.java     # MockMvc tests for room endpoints
    │   └── BookingControllerTest.java  # MockMvc tests for booking endpoints
    └── service/
        └── BookingServiceTest.java     # Unit tests for availability updates & edge cases
```

### Frontend:
- **Framework:** Vue.js 3 (Options API / simple reactive model)
- **Build Tool:** Vite
- **Components:**
  - `RoomList.vue`: Room listing component displaying rooms with **distinct border colors per room type** (Standard = Sky Blue, Deluxe = Purple, Suite = Amber).
  - `BookingStats.vue`: Booking statistics component displaying bookings grouped by room type via MongoDB aggregation with refresh capability.
  - `App.vue`: Integrated layout with navigation, Add Room modal, New Booking modal, and Booking History view.

---

## 🚀 REST API Endpoints

### 1. Rooms API (`/api/rooms`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/rooms` | Create a new room in the `rooms` collection |
| `GET`  | `/api/rooms` | Retrieve all rooms |
| `GET`  | `/api/rooms/types` | Retrieve available room types (`STANDARD`, `DELUXE`, `SUITE`) |

#### Example Add Room Request Body:
```json
{
  "roomId": "RM-101",
  "roomType": "STANDARD",
  "checkInDate": "2026-11-01",
  "checkOutDate": "2026-11-30",
  "availabilityCount": 5,
  "capacity": 2
}
```

### 2. Bookings API (`/api/bookings`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/bookings` | Create a booking after validating availability; decrements room availability count and updates status |
| `GET`  | `/api/bookings` | Retrieve all booking records |
| `GET`  | `/api/bookings/count-by-room-type` | Return booking count grouped by room type using MongoDB aggregation |

#### Example Create Booking Request Body:
```json
{
  "customerName": "John Doe",
  "roomType": "DELUXE",
  "checkInDate": "2026-11-05",
  "checkOutDate": "2026-11-10",
  "roomsBooked": 2
}
```

---

## 🧪 Running Automated Tests (MockMvc & JUnit 5)

All 14 tests cover:
- Successful room creation (`POST /api/rooms`)
- Retrieval of all rooms and room types (`GET /api/rooms`, `GET /api/rooms/types`)
- Successful booking creation (`POST /api/bookings`)
- Booking rejection when availability is insufficient (HTTP 400 with error message)
- Booking rejection when check-in date is after check-out date (HTTP 400)
- Validation of required fields (missing customer name, invalid capacity)
- Correct room availability count decrement and transition to `UNAVAILABLE` when count reaches 0
- Correct booking counts grouped by room type aggregation (`GET /api/bookings/count-by-room-type`)

To run the test suite:
```powershell
cd backend
.\mvnw test
```

---

## 💻 How to Run Locally

### Prerequisites:
1. Java 21 JDK
2. MongoDB running locally on `localhost:27017`
3. Node.js (v18+) & npm

### 1. Start MongoDB:
Ensure the MongoDB service is active:
```powershell
Get-Service MongoDB
```

### 2. Start the Backend:
```powershell
cd backend
.\mvnw spring-boot:run
```
The backend will start at: `http://localhost:8080`

### 3. Start the Vue.js Frontend:
```powershell
cd frontend
npm install
npm run dev -- --port 5173
```
Open your browser and navigate to: `http://localhost:5173`
