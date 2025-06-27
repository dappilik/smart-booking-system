import { mount } from "@vue/test-utils";
import ThemeToggle from "../../src/components/ThemeToggle.vue";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { nextTick } from "vue";

describe("ThemeToggle.vue", () => {
  let setItemMock: ReturnType<typeof vi.fn>;
  let getItemMock: ReturnType<typeof vi.fn>;
  let setAttributeSpy: ReturnType<typeof vi.spyOn>;

  beforeEach(() => {
    setItemMock = vi.fn();
    getItemMock = vi.fn();
    window.localStorage = {
      getItem: getItemMock,
      setItem: setItemMock,
      removeItem: vi.fn(),
      clear: vi.fn(),
      key: vi.fn(),
      length: 0,
    } as any;
    setAttributeSpy = vi.spyOn(document.documentElement, "setAttribute");
    getItemMock.mockReturnValue(null);
  });

  it("renders without crashing", () => {
    const wrapper = mount(ThemeToggle);
    expect(wrapper.exists()).toBe(true);
  });

  it("loads theme from localStorage and sets attribute on mount", () => {
    getItemMock.mockReturnValueOnce("light");
    mount(ThemeToggle);
    expect(getItemMock).toHaveBeenCalledWith("theme");
    expect(setAttributeSpy).toHaveBeenCalledWith("data-theme", "light");
  });

  it("toggles theme, updates attribute and localStorage", async () => {
    getItemMock.mockReturnValueOnce("dark");
    const wrapper = mount(ThemeToggle);
    // Initial state is dark
    expect(wrapper.text()).toContain("Light Mode");
    const button = wrapper.find("button");
    expect(button.exists()).toBe(true);
    await button.trigger("click");
    await nextTick();
    expect(setAttributeSpy).toHaveBeenLastCalledWith("data-theme", "light");
    expect(setItemMock).toHaveBeenLastCalledWith("theme", "light");
    expect(wrapper.text()).toContain("Dark Mode");
    await button.trigger("click");
    await nextTick();
    expect(setAttributeSpy).toHaveBeenLastCalledWith("data-theme", "dark");
    expect(setItemMock).toHaveBeenLastCalledWith("theme", "dark");
    expect(wrapper.text()).toContain("Light Mode");
  });
});
