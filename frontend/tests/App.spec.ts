import { mount } from "@vue/test-utils";
import App from "../src/App.vue";
import { describe, it, expect } from "vitest";

const RouterLinkStub = {
  template: "<a><slot /></a>",
};

describe("App.vue", () => {
  it("renders all navigation links", () => {
    const wrapper = mount(App, {
      global: {
        stubs: {
          "router-link": RouterLinkStub,
          "router-view": true,
          ThemeToggle: true,
        },
      },
    });
    expect(wrapper.text()).toContain("Home");
    expect(wrapper.text()).toContain("My Bookings");
    expect(wrapper.text()).toContain("Live Bookings Stream");
  });

  it("renders ThemeToggle component in navbar", () => {
    const wrapper = mount(App, {
      global: {
        stubs: {
          "router-link": RouterLinkStub,
          "router-view": true,
          ThemeToggle: true,
        },
      },
    });
    // Check ThemeToggle stub is present in the navbar
    const navbar = wrapper.find(".navbar");
    expect(navbar.html()).toContain("theme-toggle-stub");
  });
});
