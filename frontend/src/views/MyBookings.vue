<template>
  <div class="p-6 max-w-4xl mx-auto w-full">
    <h2 class="text-2xl font-bold mb-4">📄 My Bookings</h2>
    <ul v-if="bookings.length" class="space-y-2">
      <BookingItem v-for="b in bookings" :key="b.id" :booking="b" />
    </ul>
    <p v-else class="text-gray-500">No bookings yet.</p>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import type { BookingResponse } from "../types/booking";
import { getAllBookings } from "../api";
import BookingItem from "../components/BookingItem.vue";

const bookings = ref<BookingResponse[]>([]);

onMounted(async () => {
  bookings.value = await getAllBookings();
});
</script>
