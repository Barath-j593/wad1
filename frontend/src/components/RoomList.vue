<template>
  <section>
    <div class="header-row">
      <h2>Rooms</h2>
      <button @click="getRooms">Refresh</button>
    </div>
    <p v-if="error" class="error">{{ error }}</p>
    <p v-else-if="rooms.length === 0">No rooms found.</p>
    <div v-else class="room-grid">
      <div
        v-for="room in rooms"
        :key="room.roomId"
        class="room-card"
        :class="room.roomType"
      >
        <h3>{{ room.roomId }}</h3>
        <p><strong>Type:</strong> <span class="badge" :class="room.roomType">{{ room.roomType }}</span></p>
        <p><strong>Capacity:</strong> {{ room.capacity }}</p>
        <p><strong>Available:</strong> {{ room.availabilityCount }}</p>
        <p><strong>Status:</strong> {{ room.availabilityStatus }}</p>
        <p v-if="room.checkInDate && room.checkOutDate">
          <strong>Dates:</strong> {{ room.checkInDate }} to {{ room.checkOutDate }}
        </p>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
const props = defineProps({ refresh: Number })
const rooms = ref([])
const error = ref('')

async function getRooms() {
  try {
    const response = await fetch('http://localhost:8080/rooms')
    if (!response.ok) throw new Error('Failed to load rooms')
    rooms.value = await response.json()
    error.value = ''
  } catch (e) {
    error.value = e.message
  }
}

onMounted(getRooms)
watch(() => props.refresh, getRooms)
</script>

<style>
section { background: white; padding: 20px; margin: 20px 0; border-radius: 8px; }
.header-row { display: flex; justify-content: space-between; align-items: center; }
button { padding: 10px; background: #234b78; color: white; border: none; border-radius: 5px; cursor: pointer; }

/* Grid layout for room cards */
.room-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 15px;
  margin-top: 15px;
}

.room-card {
  padding: 15px;
  background: #fafafa;
  border-radius: 8px;
  border: 3px solid #ccc; /* default border */
}

/* Distinct border color based on Room Type */
.room-card.STANDARD {
  border-color: #2196f3; /* Blue */
}

.room-card.DELUXE {
  border-color: #9c27b0; /* Purple */
}

.room-card.SUITE {
  border-color: #ff9800; /* Amber / Orange */
}

.room-card h3 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #234b78;
}

.room-card p {
  margin: 6px 0;
  font-size: 14px;
}

.badge {
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

.badge.STANDARD { background: #e3f2fd; color: #1976d2; }
.badge.DELUXE { background: #f3e5f5; color: #7b1fa2; }
.badge.SUITE { background: #fff3e0; color: #e65100; }

.error { color: #c62828; }
</style>
