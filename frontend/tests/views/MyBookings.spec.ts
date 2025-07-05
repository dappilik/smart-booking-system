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

  it("renders MyBookings view and heading", async () => {
    getAllBookingsMock.mockResolvedValue([]);
    const { default: MyBookingsReloaded } = await import(
      "../../src/views/MyBookings.vue"
    );
    const wrapper = mount(MyBookingsReloaded);
    await new Promise((r) => setTimeout(r));
    expect(wrapper.exists()).toBe(true);
    expect(wrapper.find("h2").text()).toContain("My Bookings");
  });

  it("shows bookings list with BookingItem components when bookings exist", async () => {
    getAllBookingsMock.mockResolvedValue(mockBookings);
    const { default: MyBookingsReloaded } = await import(
      "../../src/views/MyBookings.vue"
    );
    const wrapper = mount(MyBookingsReloaded, {
      global: {
        stubs: {
          BookingItem: {
            template: '<li class="booking-item-stub">Stub</li>',
          },
        },
      },
    });
    await new Promise((r) => setTimeout(r));
    // Should render two BookingItem stubs
    expect(wrapper.findAll(".booking-item-stub").length).toBe(2);
    // Should not show the empty message
    expect(wrapper.text()).not.toContain("No bookings yet.");
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
