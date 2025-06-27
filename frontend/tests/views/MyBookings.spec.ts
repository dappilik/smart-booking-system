let getAllBookingsMock = vi.fn();

vi.mock("../../src/api", () => ({
  getAllBookings: (...args: any[]) => getAllBookingsMock(...args),
}));

import { mount } from "@vue/test-utils";
import { describe, it, expect, beforeEach } from "vitest";

const mockBookings = [
  { id: 1, slot: "2025-06-27T10:00:00", status: "CONFIRMED" },
  { id: 2, slot: "2025-06-28T11:00:00", status: "CANCELLED" },
];

describe("MyBookings.vue", () => {
  beforeEach(() => {
    getAllBookingsMock = vi.fn();
  });

  it("renders MyBookings view", async () => {
    getAllBookingsMock.mockResolvedValue([]);
    const { default: MyBookingsReloaded } = await import(
      "../../src/views/MyBookings.vue"
    );
    const wrapper = mount(MyBookingsReloaded);
    await new Promise((r) => setTimeout(r));
    expect(wrapper.exists()).toBe(true);
  });

  it("shows bookings list when bookings exist", async () => {
    getAllBookingsMock.mockResolvedValue(mockBookings);
    const { default: MyBookingsReloaded } = await import(
      "../../src/views/MyBookings.vue"
    );
    const wrapper = mount(MyBookingsReloaded);
    await new Promise((r) => setTimeout(r));
    expect(wrapper.findAll("li").length).toBe(2);
    expect(wrapper.text()).toContain("ID: 1");
    expect(wrapper.text()).toContain("ID: 2");
    expect(wrapper.text()).toContain("CONFIRMED");
    expect(wrapper.text()).toContain("CANCELLED");
  });

  it("shows 'No bookings yet.' when bookings is empty", async () => {
    getAllBookingsMock.mockResolvedValue([]);
    const { default: MyBookingsReloaded } = await import(
      "../../src/views/MyBookings.vue"
    );
    const wrapper = mount(MyBookingsReloaded);
    await new Promise((r) => setTimeout(r));
    expect(wrapper.text()).toContain("No bookings yet.");
  });
});
