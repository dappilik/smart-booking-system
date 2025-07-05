import { createRouter, createWebHistory } from "vue-router";
import Home from "./views/Home.vue";
import MyBookings from "./views/MyBookings.vue";
import StreamBookings from "./views/StreamBookings.vue";

const routes = [
  { path: "/", component: Home, meta: { title: "Home - Smart Booking" } },
  {
    path: "/bookings",
    component: MyBookings,
    meta: { title: "My Bookings - Smart Booking" },
  },
  {
    path: "/stream-bookings",
    component: StreamBookings,
    meta: { title: "Live Bookings Stream - Smart Booking" },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
