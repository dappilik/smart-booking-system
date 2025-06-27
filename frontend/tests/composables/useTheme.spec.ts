import { describe, it, expect, vi, beforeEach } from "vitest";
import { nextTick } from "vue";

describe("useTheme composable", () => {
  let getItemMock: ReturnType<typeof vi.fn>;
  let setItemMock: ReturnType<typeof vi.fn>;
  let setAttributeSpy: ReturnType<typeof vi.spyOn>;

  beforeEach(() => {
    vi.resetModules();
    getItemMock = vi.fn();
    setItemMock = vi.fn();
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

  it("should default to dark theme if nothing in localStorage", async () => {
    const { useTheme } = await import("../../src/composables/useTheme");
    const { theme } = useTheme();
    await nextTick();
    expect(theme.value).toBe("dark");
    expect(setAttributeSpy).toHaveBeenCalledWith("data-theme", "dark");
  });

  it("should load theme from localStorage if present", async () => {
    getItemMock.mockReturnValueOnce("light");
    const { useTheme } = await import("../../src/composables/useTheme");
    const { theme } = useTheme();
    await nextTick();
    expect(theme.value).toBe("light");
    expect(setAttributeSpy).toHaveBeenCalledWith("data-theme", "light");
  });

  it("should update document attribute and localStorage when theme changes", async () => {
    const { useTheme } = await import("../../src/composables/useTheme");
    const { theme } = useTheme();
    theme.value = "light";
    await nextTick();
    expect(setAttributeSpy).toHaveBeenLastCalledWith("data-theme", "light");
    expect(setItemMock).toHaveBeenLastCalledWith("theme", "light");
    theme.value = "dark";
    await nextTick();
    expect(setAttributeSpy).toHaveBeenLastCalledWith("data-theme", "dark");
    expect(setItemMock).toHaveBeenLastCalledWith("theme", "dark");
  });
});
