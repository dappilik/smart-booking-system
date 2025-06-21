<template>
  <button @click="toggleTheme" class="theme-toggle">
    {{ theme === "dark" ? "☀️ Light Mode" : "🌙 Dark Mode" }}
  </button>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";

const theme = ref<"light" | "dark">("dark");

onMounted(() => {
  const saved = localStorage.getItem("theme") as "light" | "dark";
  theme.value = saved || "dark";
  document.documentElement.setAttribute("data-theme", theme.value);
});

function toggleTheme() {
  theme.value = theme.value === "dark" ? "light" : "dark";
  document.documentElement.setAttribute("data-theme", theme.value);
  localStorage.setItem("theme", theme.value);
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
