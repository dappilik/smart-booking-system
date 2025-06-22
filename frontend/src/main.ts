import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";
import router from "./router";

createApp(App).use(router).mount("#app");

router.beforeEach((to, from, next) => {
  const defaultTitle = "Smart Booking System";
  document.title = (to.meta.title as string) || defaultTitle;
  next();
});
