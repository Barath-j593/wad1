<template>
  <main>
    <h1>Room Booking System</h1>
    <p v-if="message" class="message">{{ message }}</p>
    <AddRoom @added="handleAdded" />
    <BookRoom @booked="handleBooked" />
    <RoomList :refresh="refresh" />
    <BookingStats :refresh="refresh" />
    <BookingList :refresh="refresh" />
  </main>
</template>

<script setup>
import { ref } from 'vue'
import AddRoom from './components/AddRoom.vue'
import BookRoom from './components/BookRoom.vue'
import RoomList from './components/RoomList.vue'
import BookingStats from './components/BookingStats.vue'
import BookingList from './components/BookingList.vue'

const refresh = ref(0)
const message = ref('')
function handleAdded() { refresh.value++; message.value = 'Room added successfully!' }
function handleBooked() { refresh.value++; message.value = 'Booking created successfully!' }
</script>

<style>
* { box-sizing: border-box; }
body { margin: 0; font-family: Arial, sans-serif; background: #f4f6f8; color: #222; }
main { max-width: 1000px; margin: 30px auto; padding: 20px; }
h1 { text-align: center; color: #234b78; }
.message { padding: 12px; background: #e7f4e8; border-radius: 5px; }
@media (max-width: 600px) { main { padding: 10px; } }
</style>
