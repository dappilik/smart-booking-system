import { describe, it, expect, vi, afterEach } from "vitest";
import axios from "axios";
import * as api from "../src/api";

vi.mock("axios");

const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("api.ts", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  it("should export functions", () => {
    expect(typeof api).toBe("object");
  });

  it("createBooking should post data and return response", async () => {
    const bookingRequest = { name: "John" };
    const bookingResponse = { id: 1, name: "John" };
    mockedAxios.post.mockResolvedValueOnce({ data: bookingResponse });
    const result = await api.createBooking(bookingRequest as any);
    expect(mockedAxios.post).toHaveBeenCalledWith(
      "http://localhost:8080/api/bookings",
      bookingRequest
    );
    expect(result).toEqual(bookingResponse);
  });

  it("getBooking should get data by id and return response", async () => {
    const bookingResponse = { id: 2, name: "Jane" };
    mockedAxios.get.mockResolvedValueOnce({ data: bookingResponse });
    const result = await api.getBooking(2);
    expect(mockedAxios.get).toHaveBeenCalledWith(
      "http://localhost:8080/api/bookings/2"
    );
    expect(result).toEqual(bookingResponse);
  });

  it("getAllBookings should get all bookings and return response", async () => {
    const bookings = [
      { id: 1, name: "John" },
      { id: 2, name: "Jane" },
    ];
    mockedAxios.get.mockResolvedValueOnce({ data: bookings });
    const result = await api.getAllBookings();
    expect(mockedAxios.get).toHaveBeenCalledWith(
      "http://localhost:8080/api/bookings"
    );
    expect(result).toEqual(bookings);
  });
});
