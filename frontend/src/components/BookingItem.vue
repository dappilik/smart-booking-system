<template>
  <li
    class="mb-3 border-b pb-2 px-4 py-2 rounded shadow-sm transition border-gray-200 dark:border-gray-700 bg-gradient-to-r from-blue-50 to-white dark:from-gray-800 dark:to-gray-900 hover:bg-blue-100 dark:hover:bg-blue-900"
  >
    <div
      class="flex flex-col sm:flex-row sm:items-center sm:gap-6 gap-1 w-full"
    >
      <div class="flex items-center gap-2 min-w-[120px]">
        <span class="font-semibold text-blue-700 dark:text-gray-200"
          >#{{ booking.id }}</span
        >
        <span class="font-medium text-blue-900 dark:text-gray-100">{{
          booking.userEmail
        }}</span>
      </div>
      <div class="flex items-center gap-1 min-w-[180px]">
        <span class="text-gray-600 dark:text-gray-400">Slot:</span>
        <span class="font-medium text-blue-900 dark:text-gray-100">{{
          booking.slot
        }}</span>
      </div>
      <div class="flex items-center gap-1 min-w-[220px]">
        <span class="text-gray-600 dark:text-gray-400">Booked at:</span>
        <span class="font-medium text-blue-900 dark:text-gray-100">{{
          booking.bookingTime
        }}</span>
      </div>
      <span
        :class="statusClass"
        class="px-2 py-1 rounded text-xs font-bold uppercase tracking-wide border ml-auto"
        >{{ booking.status }}</span
      >
    </div>
  </li>
</template>

<script setup lang="ts">
import type { BookingResponse } from "../types/booking";
import { computed } from "vue";

const props = defineProps<{ booking: BookingResponse }>();

const statusClass = computed(() => {
  switch (props.booking.status) {
    case "CONFIRMED":
      return "bg-green-50 text-green-800 border-green-200 dark:bg-green-900 dark:text-green-200 dark:border-green-700";
    case "PENDING":
      return "bg-yellow-50 text-yellow-800 border-yellow-200 dark:bg-yellow-900 dark:text-yellow-200 dark:border-yellow-700";
    case "CANCELLED":
      return "bg-red-50 text-red-800 border-red-200 dark:bg-red-900 dark:text-red-200 dark:border-red-700";
    default:
      return "bg-gray-50 text-gray-800 border-gray-200 dark:bg-gray-700 dark:text-gray-200 dark:border-gray-600";
  }
});
</script>
