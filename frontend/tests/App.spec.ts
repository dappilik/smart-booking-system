import { mount } from "@vue/test-utils";
import App from "../src/App.vue";
import { describe, it, expect } from "vitest";

const RouterLinkStub = {
  template: "<a><slot /></a>",
};

describe("App.vue", () => {
  it("renders navigation links", () => {
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
  });

  it("contains ThemeToggle component", () => {
    const wrapper = mount(App, {
      global: {
        stubs: {
          "router-link": RouterLinkStub,
          "router-view": true,
          ThemeToggle: true,
        },
      },
    });
    expect(wrapper.findComponent({ name: "ThemeToggle" }).exists()).toBe(true);
  });
});
