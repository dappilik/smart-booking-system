# Smart Booking System Frontend

This project is the frontend for the Smart Booking System, designed to interact with a backend API for managing bookings. It is implemented using Vue.js, TypeScript, and Vite, and styled with Tailwind CSS.

## Project Structure

- `src/` - Main source code
  - `api.ts` - API client for backend communication (see below)
  - `components/` - Vue components
    - `BookingForm.vue` - Booking form component
    - `ThemeToggle.vue` - Theme toggle switch
  - `composables/` - Vue composables (e.g., useTheme)
  - `views/` - Main views/pages
    - `Home.vue` - Home page
    - `MyBookings.vue` - User bookings page
  - `types/` - TypeScript types (e.g., booking types)
- `public/` - Static assets
- `tests/` - Unit and component tests
  - `components/` - Component tests (e.g., `BookingForm.spec.ts`, `ThemeToggle.spec.ts`)
  - `views/` - View tests (e.g., `Home.spec.ts`, `MyBookings.spec.ts`)
  - `api.spec.ts` - API client tests
  - `App.spec.ts` - App-level tests

## API Client (`src/api.ts`)

- Uses Axios to communicate with the backend at `http://localhost:8080/api`.
- Exposes functions:
  - `createBooking(data: BookingRequest): Promise<BookingResponse>`
  - `getBooking(id: number): Promise<BookingResponse>`
  - `getAllBookings(): Promise<BookingResponse[]>`
- Types are defined in `src/types/booking.ts`.

## Unit Test Example

Below is an example of a unit test for a Vue component, based on the existing code in `tests/components/BookingForm.spec.ts`:

```typescript
import { mount } from "@vue/test-utils";
import BookingForm from "@/components/BookingForm.vue";

describe("BookingForm.vue", () => {
  it("renders form and emits submit event", async () => {
    const wrapper = mount(BookingForm);
    // Simulate user input and form submission as needed
    // Example: await wrapper.find('form').trigger('submit.prevent');
    // expect(wrapper.emitted()).toHaveProperty('submit');
  });
});
```

Update or add new tests in the `tests/` directory following this pattern.

## LLM Context & Implementation Notes

- This README is designed to provide context for Large Language Model (LLM) agents and developers working on or extending this project.
- All API endpoints and types are defined in a modular way for easy extension.
- Future implementations should:
  - Add new API functions to `src/api.ts` and corresponding types to `src/types/`.
  - Add new components to `src/components/` and views to `src/views/`.
  - Update this README with any new architectural decisions or major features.
- For LLM-based automation or code generation, this README provides a high-level overview of the codebase and conventions.

## Development

1. Install dependencies:
   ```sh
   npm install
   ```
2. Run the development server:
   ```sh
   npm run dev
   ```
3. Run tests:
   ```sh
   npm run test
   ```

## Contributing

- Follow the existing code style and structure.
- Write unit/component tests for new features.
- Document new modules and update this README as needed.

---

_This README is intended for both human developers and LLM agents to provide context for current and future development._
