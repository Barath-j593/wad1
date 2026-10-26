# Simple Room Booking Frontend

Vue 3 + Vite frontend. API calls use `fetch()` directly inside components. Each component contains its own `<style>` block; there is no separate CSS or API utility file.

## Run

1. Start the Spring Boot backend on `http://localhost:8080`.
2. Run `npm install` and `npm run dev` in this folder.
3. Open the Vite URL, normally `http://localhost:5173`.

## Expected backend endpoints

- `GET /rooms`
- `POST /rooms`
- `GET /bookings`
- `POST /bookings`
- `GET /bookings/count-by-type`

The frontend expects matching JSON field names. Enable CORS on the backend for `http://localhost:5173` if needed.
