export interface BookingRequest {
  userEmail: string;
  slot: string;
}

export interface BookingResponse {
  id: number;
  userEmail: string;
  slot: string;
  bookingTime: string;
  status: string;
}