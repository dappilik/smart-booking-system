import { mount } from "@vue/test-utils";
import MyBookings from "../src/views/MyBookings.vue";
import { describe, it, expect, vi, beforeEach } from "vitest";

vi.mock("../src/api", () => ({
  getAllBookings: vi.fn().mockResolvedValue([]),
}));

describe("MyBookings.vue", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });
  it("renders MyBookings view", async () => {
    const wrapper = mount(MyBookings);
    // Wait for any promises to resolve
    await new Promise((r) => setTimeout(r));
    expect(wrapper.exists()).toBe(true);
  });
});
