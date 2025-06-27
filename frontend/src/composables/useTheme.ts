import { ref, watch } from "vue";

const theme = ref<"light" | "dark">("dark");
// Immediately initialize theme from localStorage
const saved = localStorage.getItem("theme") as "light" | "dark";
theme.value = saved || "dark";
document.documentElement.setAttribute("data-theme", theme.value);

watch(theme, (val) => {
  document.documentElement.setAttribute("data-theme", val);
  localStorage.setItem("theme", val);
});

export function useTheme() {
  return { theme };
}
