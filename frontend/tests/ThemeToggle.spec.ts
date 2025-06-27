import { mount } from "@vue/test-utils";
import ThemeToggle from "../src/components/ThemeToggle.vue";
import { describe, it, expect } from "vitest";

describe("ThemeToggle.vue", () => {
  it("renders without crashing", () => {
    const wrapper = mount(ThemeToggle);
    expect(wrapper.exists()).toBe(true);
  });
});
