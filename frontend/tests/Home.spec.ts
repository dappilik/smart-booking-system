import { mount } from "@vue/test-utils";
import Home from "../src/views/Home.vue";
import { describe, it, expect } from "vitest";

describe("Home.vue", () => {
  it("renders Home view", () => {
    const wrapper = mount(Home, {
      global: {
        stubs: ["BookingForm"],
      },
    });
    expect(wrapper.exists()).toBe(true);
  });
});
