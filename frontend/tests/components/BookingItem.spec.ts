import { mount } from "@vue/test-utils";
import BookingItem from "../../src/components/BookingItem.vue";
import { describe, it, expect } from "vitest";

const baseBooking = {
  id: 42,
  userEmail: "user@example.com",
  slot: "2025-07-05T10:00:00",
  bookingTime: "2025-07-01T09:00:00",
  status: "CONFIRMED",
};

describe("BookingItem.vue", () => {
  it("renders booking details", () => {
    const wrapper = mount(BookingItem, {
      props: { booking: baseBooking },
    });
    expect(wrapper.text()).toContain("#42");
    expect(wrapper.text()).toContain("user@example.com");
    expect(wrapper.text()).toContain("2025-07-05T10:00:00");
    expect(wrapper.text()).toContain("2025-07-01T09:00:00");
    expect(wrapper.text()).toContain("CONFIRMED");
  });

  it("applies correct class for CONFIRMED status", () => {
    const wrapper = mount(BookingItem, {
      props: { booking: { ...baseBooking, status: "CONFIRMED" } },
    });
    const status = wrapper.find("span.font-bold");
    expect(status.classes().join(" ")).toContain("bg-green-50");
  });

  it("applies correct class for PENDING status", () => {
    const wrapper = mount(BookingItem, {
      props: { booking: { ...baseBooking, status: "PENDING" } },
    });
    const status = wrapper.find("span.font-bold");
    expect(status.classes().join(" ")).toContain("bg-yellow-50");
  });

  it("applies correct class for CANCELLED status", () => {
    const wrapper = mount(BookingItem, {
      props: { booking: { ...baseBooking, status: "CANCELLED" } },
    });
    const status = wrapper.find("span.font-bold");
    expect(status.classes().join(" ")).toContain("bg-red-50");
  });

  it("applies default class for unknown status", () => {
    const wrapper = mount(BookingItem, {
      props: { booking: { ...baseBooking, status: "UNKNOWN" } },
    });
    const status = wrapper.find("span.font-bold");
    expect(status.classes().join(" ")).toContain("bg-gray-50");
  });
});
