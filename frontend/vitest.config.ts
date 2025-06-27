import { defineConfig } from "vitest/config";
import vue from "@vitejs/plugin-vue";

export default defineConfig({
  plugins: [vue()],
  test: {
    environment: "jsdom",
    globals: true,
    setupFiles: [],
    include: ["tests/**/*.spec.ts"],
    coverage: {
      provider: "v8",
      exclude: [
        "src/main.ts",
        "src/router.ts",
        "src/types/**",
        "src/assets/**",
        "**/*.d.ts",
      ],
    },
  },
});
