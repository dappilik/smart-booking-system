import { mount } from "@vue/test-utils";
import BookingForm from "../src/components/BookingForm.vue";
import { describe, it, expect } from "vitest";

describe("BookingForm.vue", () => {
  it("renders form", () => {
    const wrapper = mount(BookingForm);
    expect(wrapper.find("form").exists()).toBe(true);
  });
});
