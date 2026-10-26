<template>
  <section>
    <h2>Book a Room</h2>
    <form @submit.prevent="bookRoom">
      <input v-model="booking.bookingId" placeholder="Booking ID" required>
      <input v-model="booking.customerName" placeholder="Customer name" required>
      <input v-model="booking.customerEmail" type="email" placeholder="Customer email" required>
      <select v-model="booking.roomType" required>
        <option value="">Select room type</option><option>STANDARD</option><option>DELUXE</option><option>SUITE</option>
      </select>
      <input v-model="booking.checkInDate" type="date" required>
      <input v-model="booking.checkOutDate" type="date" required>
      <input v-model.number="booking.roomsBooked" type="number" min="1" placeholder="Rooms to book" required>
      <button type="submit">Book Now</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </section>
</template>

<script setup>
import { ref } from 'vue'
const emit = defineEmits(['booked'])
const error = ref('')
const emptyBooking = () => ({ bookingId: '', customerName: '', customerEmail: '', roomType: '', checkInDate: '', checkOutDate: '', roomsBooked: 1 })
const booking = ref(emptyBooking())
async function bookRoom() {
  error.value = ''
  try {
    const response = await fetch('http://localhost:8080/bookings', {
      method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(booking.value)
    })
    if (!response.ok) throw new Error(await response.text() || 'Failed to book room')
    booking.value = emptyBooking()
    emit('booked')
  } catch (e) { error.value = e.message }
}
</script>

<style>
section { background: white; padding: 20px; margin: 20px 0; border-radius: 8px; }
form { display: flex; flex-wrap: wrap; gap: 10px; }
input, select, button { padding: 10px; border: 1px solid #ccc; border-radius: 5px; font-size: 14px; }
input, select { flex: 1 1 180px; min-width: 0; }
button { background: #234b78; color: white; border: none; cursor: pointer; }
.error { color: #c62828; }
</style>
