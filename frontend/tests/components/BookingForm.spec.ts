import { mount } from "@vue/test-utils";
import BookingForm from "../../src/components/BookingForm.vue";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { nextTick } from "vue";

vi.mock("../../src/api", () => ({
  createBooking: vi.fn(),
}));

import * as api from "../../src/api";

describe("BookingForm.vue", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("renders form", () => {
    const wrapper = mount(BookingForm);
    expect(wrapper.find("form").exists()).toBe(true);
  });

  it("submits form and shows confirmation card on success", async () => {
    const bookingResponse = {
      id: 123,
      userEmail: "test@example.com",
      slot: "2025-06-27T10:00:00",
      bookingTime: new Date().toISOString(),
      status: "CONFIRMED",
    };
    (api.createBooking as any).mockResolvedValueOnce(bookingResponse);
    const wrapper = mount(BookingForm);
    await wrapper.find('input[type="email"]').setValue("test@example.com");
    // setData does not work for refs defined with script setup, so set via input interaction
    // Find the Datepicker and emit the update:modelValue event
    await wrapper.find('input[type="email"]').setValue("a@b.com");
    // Simulate date selection via Datepicker
    await wrapper.vm.$emit(
      "update:selectedDate",
      new Date("2025-06-27T10:00:00")
    );
    // Or set selectedDate via direct assignment if accessible
    if ((wrapper.vm as any).selectedDate !== undefined) {
      (wrapper.vm as any).selectedDate = new Date("2025-06-27T10:00:00");
    }
    await wrapper.find("form").trigger("submit.prevent");
    await nextTick();
    expect(api.createBooking).toHaveBeenCalled();
    expect(wrapper.find(".confirmation-card").exists()).toBe(true);
    expect(wrapper.text()).toContain("Booking Confirmed");
    expect(wrapper.text()).toContain("test@example.com");
    expect(wrapper.text()).toContain("CONFIRMED");
  });

  it("shows alert and logs error on booking failure (no date selected)", async () => {
    const alertSpy = vi.spyOn(window, "alert").mockImplementation(() => {});
    const errorSpy = vi.spyOn(console, "error").mockImplementation(() => {});
    const wrapper = mount(BookingForm);
    await wrapper.find('input[type="email"]').setValue("fail@example.com");
    // Do not set selectedDate
    await wrapper.find("form").trigger("submit.prevent");
    await nextTick();
    expect(api.createBooking).not.toHaveBeenCalled();
    expect(alertSpy).toHaveBeenCalledWith("Booking failed. See console.");
    expect(errorSpy).toHaveBeenCalled();
    alertSpy.mockRestore();
    errorSpy.mockRestore();
  });

  it("shows alert and logs error on booking failure (API error)", async () => {
    const error = new Error("Booking failed");
    (api.createBooking as any).mockRejectedValueOnce(error);
    const alertSpy = vi.spyOn(window, "alert").mockImplementation(() => {});
    const errorSpy = vi.spyOn(console, "error").mockImplementation(() => {});
    const wrapper = mount(BookingForm);
    await wrapper.find('input[type="email"]').setValue("fail@example.com");
    // setData does not work for refs defined with script setup, so set via direct assignment
    (wrapper.vm as any).selectedDate = new Date("2025-06-27T10:00:00");
    await wrapper.find("form").trigger("submit.prevent");
    await nextTick();
    expect(api.createBooking).toHaveBeenCalled();
    expect(alertSpy).toHaveBeenCalledWith("Booking failed. See console.");
    expect(errorSpy).toHaveBeenCalled();
    alertSpy.mockRestore();
    errorSpy.mockRestore();
  });

  it("computed formattedBookingTime returns formatted string", async () => {
    const bookingResponse = {
      id: 1,
      userEmail: "a@b.com",
      slot: "2025-06-27T10:00:00",
      bookingTime: "2025-06-27T09:00:00Z",
      status: "CONFIRMED",
    };
    (api.createBooking as any).mockResolvedValueOnce(bookingResponse);
    const wrapper = mount(BookingForm);
    await wrapper.find('input[type="email"]').setValue("a@b.com");
    (wrapper.vm as any).selectedDate = new Date("2025-06-27T10:00:00");
    await wrapper.find("form").trigger("submit.prevent");
    await nextTick();
    // formattedBookingTime is shown in the confirmation card
    expect(wrapper.text()).toContain(
      new Date("2025-06-27T09:00:00Z").toLocaleString()
    );
  });

  it("computed formattedBookingTime returns empty string if no bookingTime", async () => {
    (api.createBooking as any).mockResolvedValueOnce({
      id: 1,
      userEmail: "a@b.com",
      slot: "2025-06-27T10:00:00",
      status: "CONFIRMED",
      // no bookingTime
    });
    const wrapper = mount(BookingForm);
    await wrapper.find('input[type="email"]').setValue("a@b.com");
    (wrapper.vm as any).selectedDate = new Date("2025-06-27T10:00:00");
    await wrapper.find("form").trigger("submit.prevent");
    await nextTick();
    // If bookingTime is missing, the confirmation card should not show a value for it
    const card = wrapper.find(".confirmation-card");
    if (card.exists()) {
      // Should show 'Booking Time:' with nothing after it (except maybe whitespace)
      const match = card.text().match(/Booking Time:\s*(.*)Status:/);
      expect(match).toBeTruthy();
      // The captured group should be empty or only whitespace
      expect(match && match[1].trim()).toBe("");
    } else {
      // If confirmation card is not rendered, computed should be empty
      const formattedBookingTime = (wrapper.vm as any).formattedBookingTime;
      expect(formattedBookingTime).toBe("");
    }
  });
});
