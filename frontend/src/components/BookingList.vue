<template>
  <section>
    <h2>All Bookings</h2><button @click="getBookings">Refresh</button>
    <p v-if="error" class="error">{{ error }}</p><p v-else-if="bookings.length === 0">No bookings found.</p>
    <table v-else>
      <thead><tr><th>Booking ID</th><th>Customer</th><th>Room Type</th><th>Check-in</th><th>Check-out</th><th>Rooms Booked</th></tr></thead>
      <tbody><tr v-for="booking in bookings" :key="booking.bookingId"><td>{{ booking.bookingId }}</td><td>{{ booking.customerName }}</td><td>{{ booking.roomType }}</td><td>{{ booking.checkInDate }}</td><td>{{ booking.checkOutDate }}</td><td>{{ booking.roomsBooked }}</td></tr></tbody>
    </table>
  </section>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
const props = defineProps({ refresh: Number })
const bookings = ref([])
const error = ref('')
async function getBookings() {
  try {
    const response = await fetch('http://localhost:8080/bookings')
    if (!response.ok) throw new Error('Failed to load bookings')
    bookings.value = await response.json(); error.value = ''
  } catch (e) { error.value = e.message }
}
onMounted(getBookings)
watch(() => props.refresh, getBookings)
</script>

<style>
section { background: white; padding: 20px; margin: 20px 0; border-radius: 8px; }
button { padding: 10px; background: #234b78; color: white; border: none; border-radius: 5px; cursor: pointer; }
table { width: 100%; border-collapse: collapse; margin-top: 15px; }
th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }
th { background: #eaf0f6; }
.error { color: #c62828; }
</style>
