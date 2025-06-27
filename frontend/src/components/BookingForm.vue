<template>
  <form @submit.prevent="submitBooking" class="booking-form">
    <h1>🗒️ Smart Booking System</h1>

    <label>Email:</label>
    <input v-model="form.userEmail" type="email" required />

    <label>Slot (ISO):</label>
    <Datepicker
      v-model="selectedDate"
      :is-24="true"
      :enable-time-picker="true"
      :format="'yyyy-MM-dd HH:mm'"
      :dark="theme === 'dark'"
      required
      input-class="datepicker-input"
      placeholder="Select date and time"
    />

    <button type="submit">Book Slot</button>

    <div v-if="response" class="confirmation-card">
      <h3>✅ Booking Confirmed!</h3>
      <ul>
        <li><strong>ID:</strong> {{ response.id }}</li>
        <li><strong>Email:</strong> {{ response.userEmail }}</li>
        <li><strong>Slot:</strong> {{ response.slot }}</li>
        <li><strong>Booking Time:</strong> {{ formattedBookingTime }}</li>
        <li><strong>Status:</strong> {{ response.status }}</li>
      </ul>
    </div>
  </form>
</template>

<script setup lang="ts">
import { ref, computed, watch } from "vue";
import type { BookingRequest, BookingResponse } from "../types/booking";
import { createBooking } from "../api";
import dayjs from "dayjs";
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";
import { useTheme } from "../composables/useTheme";

const { theme } = useTheme();

const selectedDate = ref<Date | null>(null);

const form = ref<BookingRequest>({
  userEmail: "",
  slot: "",
});

const response = ref<BookingResponse | null>(null);

// Keep form.slot in sync if editing an existing booking (optional, for edit mode)
watch(
  () => form.value.slot,
  (val) => {
    if (val) {
      selectedDate.value = dayjs(val).toDate();
    }
  },
  { immediate: true }
);

const submitBooking = async () => {
  try {
    if (!selectedDate.value) throw new Error("Please select a date and time");
    form.value.slot = dayjs(selectedDate.value).format("YYYY-MM-DDTHH:mm:ss");
    const res = await createBooking(form.value);
    response.value = res;
  } catch (err) {
    console.error("Booking failed", err);
    alert("Booking failed. See console.");
  }
};

const formattedBookingTime = computed(() => {
  if (!response.value?.bookingTime) return "";
  const dt = new Date(response.value.bookingTime);
  return dt.toLocaleString();
});
</script>

<style scoped>
.booking-form {
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
  width: 100%;
  max-width: 400px;
  margin: 2rem auto;
  padding: 1.5rem;

  background-color: var(--form-bg); /* Use CSS variable for theme */
  color: var(--text-color);
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.4); /* More noticeable in dark mode */
  border: 1px solid rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(6px);
}

input,
button,
.datepicker-input {
  padding: 0.5rem;
  border-radius: 6px;
  border: none;
  font-size: 1rem;
}

input {
  background-color: var(--bg-color);
  color: var(--text-color);
  border: 1px solid #444;
}

input:focus {
  outline: 2px solid #0ea5e9;
  background-color: var(--bg-color);
  color: var(--text-color);
}

button {
  background-color: #0ea5e9;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s ease;
}

button:hover {
  background-color: #0284c7;
}

.confirmation-card {
  margin-top: 1rem;
  padding: 1rem;
  border: 1px solid #22c55e;
  border-radius: 6px;
  background-color: var(--bg-color);
  color: var(--text-color);
}

.confirmation-card h3 {
  color: #22c55e;
}

.datepicker-input {
  background-color: var(--bg-color);
  color: var(--text-color);
  border: 1px solid #444;
  width: 100%;
}

.datepicker-input:focus {
  outline: 2px solid #0ea5e9;
  background-color: var(--bg-color);
  color: var(--text-color);
}

@media (max-width: 480px) {
  .booking-form {
    width: 90%;
    padding: 1rem;
    font-size: 0.9rem;
  }

  input,
  button {
    font-size: 1rem;
  }
}
</style>
