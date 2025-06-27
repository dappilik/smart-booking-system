<template>
  <button @click="toggleTheme" class="theme-toggle">
    {{ theme === "dark" ? "☀️ Light Mode" : "🌙 Dark Mode" }}
  </button>
</template>

<script setup lang="ts">
import { useTheme } from "../composables/useTheme";
const { theme } = useTheme();

function toggleTheme() {
  theme.value = theme.value === "dark" ? "light" : "dark";
}
</script>

<style scoped>
.theme-toggle {
  background: transparent;
  color: var(--nav-text);
  border: 1px solid var(--nav-text);
  padding: 0.3rem 0.7rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85rem;
}
</style>
