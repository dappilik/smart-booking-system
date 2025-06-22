import axios from 'axios';
import type { BookingRequest, BookingResponse } from './types/booking';

const API_BASE = 'http://localhost:8080/api';

export async function createBooking(data: BookingRequest): Promise<BookingResponse> {
  const response = await axios.post(`${API_BASE}/bookings`, data);
  return response.data;
}

export async function getBooking(id: number): Promise<BookingResponse> {
  const response = await axios.get(`${API_BASE}/bookings/${id}`);
  return response.data;
}

export async function getAllBookings(): Promise<BookingResponse[]> {
  const res = await axios.get(`${API_BASE}/bookings`);
  return res.data;
}

