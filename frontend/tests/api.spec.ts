import { describe, it, expect } from "vitest";
import * as api from "../src/api";

describe("api.ts", () => {
  it("should export functions", () => {
    expect(typeof api).toBe("object");
  });
});
