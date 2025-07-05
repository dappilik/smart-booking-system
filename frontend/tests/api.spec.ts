import {
  describe,
  it,
  expect,
  vi,
  afterEach,
  beforeEach,
  beforeAll,
  afterAll,
} from "vitest";
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
  describe("streamBookings", () => {
    let originalEventSource: any;
    let mockEventSourceInstance: any;

    beforeAll(() => {
      originalEventSource = (globalThis as any).EventSource;
    });
    afterAll(() => {
      (globalThis as any).EventSource = originalEventSource;
    });
    beforeEach(() => {
      mockEventSourceInstance = {
        onmessage: null,
        onerror: null,
        close: vi.fn(),
      };
      (globalThis as any).EventSource = vi.fn(() => mockEventSourceInstance);
    });

    it("should call onMessage when a message is received", () => {
      const onMessage = vi.fn();
      const onError = vi.fn();
      const eventSource = api.streamBookings(onMessage, onError);
      const booking = { id: 123, name: "Test" };
      const event = { data: JSON.stringify(booking) };
      // Simulate receiving a message
      mockEventSourceInstance.onmessage(event);
      expect(onMessage).toHaveBeenCalledWith(booking);
      expect(eventSource).toBe(mockEventSourceInstance);
    });

    it("should call onError if JSON parsing fails", () => {
      const onMessage = vi.fn();
      const onError = vi.fn();
      const eventSource = api.streamBookings(onMessage, onError);
      const event = { data: "not-json" };
      mockEventSourceInstance.onmessage(event);
      expect(onError).toHaveBeenCalledWith(event);
    });

    it("should set eventSource.onerror if onError is provided", () => {
      const onMessage = vi.fn();
      const onError = vi.fn();
      api.streamBookings(onMessage, onError);
      expect(mockEventSourceInstance.onerror).toBe(onError);
    });

    it("should not set eventSource.onerror if onError is not provided", () => {
      const onMessage = vi.fn();
      api.streamBookings(onMessage);
      expect(mockEventSourceInstance.onerror).toBeNull();
    });
  });
});
