<template>
  <div class="app-container">
    <nav class="navbar">
      <div class="nav-links">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/bookings">My Bookings</RouterLink>
        <RouterLink to="/stream-bookings">Live Bookings Stream</RouterLink>
      </div>
      <ThemeToggle />
    </nav>
    <router-view />
  </div>
</template>

<script setup lang="ts">
import ThemeToggle from "./components/ThemeToggle.vue";
import { RouterLink, RouterView } from "vue-router";
</script>

<style>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: var(--nav-bg);
  color: var(--nav-text);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}

.nav-links a {
  margin-right: 1.5rem;
  color: var(--nav-text);
  text-decoration: none;
  font-weight: 500;
}

.nav-links a.router-link-active {
  text-decoration: underline;
}

.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>
