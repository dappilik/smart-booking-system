<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-4">Live Bookings Stream</h1>
    <ul>
      <BookingItem
        v-for="booking in bookings"
        :key="booking.id"
        :booking="booking"
      />
    </ul>
    <div v-if="error" class="text-red-500 mt-4">Stream error: {{ error }}</div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from "vue";
import { streamBookings } from "../api";
import type { BookingResponse } from "../types/booking";
import BookingItem from "../components/BookingItem.vue";

const bookings = ref<BookingResponse[]>([]);
const error = ref<string | null>(null);
let eventSource: EventSource | null = null;

onMounted(() => {
  eventSource = streamBookings(
    (data: BookingResponse) => {
      if (!bookings.value.some((b) => b.id === data.id)) {
        bookings.value.unshift(data);
      }
    },
    (_err: Event) => {
      error.value =
        "Failed to connect or stream closed." +
        (_err instanceof Error ? `: ${_err.message}` : "");
    }
  );
});

onBeforeUnmount(() => {
  if (eventSource) {
    eventSource.close();
  }
});
</script>

<style scoped>
ul {
  list-style: none;
  padding: 0;
}
</style>
