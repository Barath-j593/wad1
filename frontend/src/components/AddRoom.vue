<template>
  <section>
    <h2>Add Room</h2>
    <form @submit.prevent="addRoom">
      <input v-model="room.roomId" placeholder="Room ID" required>
      <select v-model="room.roomType" required>
        <option value="">Select room type</option><option>STANDARD</option><option>DELUXE</option><option>SUITE</option>
      </select>
      <input v-model.number="room.capacity" type="number" min="1" placeholder="Capacity" required>
      <input v-model.number="room.availabilityCount" type="number" min="0" placeholder="Available rooms" required>
      <input v-model="room.checkInDate" type="date" required>
      <input v-model="room.checkOutDate" type="date" required>
      <button type="submit">Add Room</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </section>
</template>

<script setup>
import { ref } from 'vue'
const emit = defineEmits(['added'])
const error = ref('')
const emptyRoom = () => ({ roomId: '', roomType: '', capacity: 1, availabilityCount: 1, checkInDate: '', checkOutDate: '' })
const room = ref(emptyRoom())
async function addRoom() {
  error.value = ''
  try {
    const response = await fetch('http://localhost:8080/rooms', {
      method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(room.value)
    })
    if (!response.ok) throw new Error(await response.text() || 'Failed to add room')
    room.value = emptyRoom()
    emit('added')
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
