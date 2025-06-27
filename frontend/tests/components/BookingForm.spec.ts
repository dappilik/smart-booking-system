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
    wrapper.vm.selectedDate = new Date("2025-06-27T10:00:00");
    await wrapper.find("form").trigger("submit.prevent");
    await nextTick();
    expect(api.createBooking).toHaveBeenCalled();
    expect(wrapper.find(".confirmation-card").exists()).toBe(true);
    expect(wrapper.text()).toContain("Booking Confirmed");
    expect(wrapper.text()).toContain("test@example.com");
    expect(wrapper.text()).toContain("CONFIRMED");
  });

  it("shows alert and logs error on booking failure", async () => {
    const error = new Error("Booking failed");
    (api.createBooking as any).mockRejectedValueOnce(error);
    const alertSpy = vi.spyOn(window, "alert").mockImplementation(() => {});
    const errorSpy = vi.spyOn(console, "error").mockImplementation(() => {});
    const wrapper = mount(BookingForm);
    await wrapper.find('input[type="email"]').setValue("fail@example.com");
    wrapper.vm.selectedDate = new Date("2025-06-27T10:00:00");
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
    wrapper.vm.selectedDate = new Date("2025-06-27T10:00:00");
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
    wrapper.vm.selectedDate = new Date("2025-06-27T10:00:00");
    await wrapper.find("form").trigger("submit.prevent");
    await nextTick();
    expect(wrapper.vm.formattedBookingTime).toBe("");
  });
});
