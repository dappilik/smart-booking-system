import { describe, it, expect } from "vitest";
import router from "../src/router";

function getRouteByPath(path: string) {
  return router.getRoutes().find((r) => r.path === path);
}

describe("router.ts", () => {
  it("should have a Home route at /", () => {
    const route = getRouteByPath("/");
    expect(route).toBeTruthy();
    expect(route?.meta?.title).toBe("Home - Smart Booking");
  });

  it("should have a MyBookings route at /bookings", () => {
    const route = getRouteByPath("/bookings");
    expect(route).toBeTruthy();
    expect(route?.meta?.title).toBe("My Bookings - Smart Booking");
  });

  it("should have a StreamBookings route at /stream-bookings", () => {
    const route = getRouteByPath("/stream-bookings");
    expect(route).toBeTruthy();
    expect(route?.meta?.title).toBe("Live Bookings Stream - Smart Booking");
  });

  it("should use HTML5 history mode", () => {
    expect(router.options.history).toBeDefined();
    expect(router.options.history.base).toBe("");
  });
});
