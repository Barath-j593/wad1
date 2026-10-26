<template>
  <section>
    <h2>Booking Statistics</h2><button @click="getStats">Refresh</button>
    <p v-if="error" class="error">{{ error }}</p><p v-else-if="stats.length === 0">No statistics available.</p>
    <table v-else>
      <thead><tr><th>Room Type</th><th>Booking Count</th></tr></thead>
      <tbody><tr v-for="item in stats" :key="item.roomType"><td>{{ item.roomType }}</td><td>{{ item.count }}</td></tr></tbody>
    </table>
  </section>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
const props = defineProps({ refresh: Number })
const stats = ref([])
const error = ref('')
async function getStats() {
  try {
    const response = await fetch('http://localhost:8080/bookings/count-by-type')
    if (!response.ok) throw new Error('Failed to load statistics')
    stats.value = await response.json(); error.value = ''
  } catch (e) { error.value = e.message }
}
onMounted(getStats)
watch(() => props.refresh, getStats)
</script>

<style>
section { background: white; padding: 20px; margin: 20px 0; border-radius: 8px; }
button { padding: 10px; background: #234b78; color: white; border: none; border-radius: 5px; cursor: pointer; }
table { width: 100%; border-collapse: collapse; margin-top: 15px; }
th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }
th { background: #eaf0f6; }
.error { color: #c62828; }
</style>
