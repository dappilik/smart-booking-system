<template>
  <div class="p-6 max-w-xl mx-auto">
    <h2 class="text-2xl font-bold mb-4">📄 My Bookings</h2>

    <ul v-if="bookings.length">
      <li v-for="b in bookings" :key="b.id" class="mb-3 border-b pb-2">
        <div><strong>ID:</strong> {{ b.id }}</div>
        <div><strong>Slot:</strong> {{ b.slot }}</div>
        <div><strong>Status:</strong> {{ b.status }}</div>
      </li>
    </ul>

    <p v-else class="text-gray-500">No bookings yet.</p>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import type { BookingResponse } from "../types/booking";
import { getAllBookings } from "../api";

const bookings = ref<BookingResponse[]>([]);

onMounted(async () => {
  bookings.value = await getAllBookings();
});
</script>
