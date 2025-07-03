# Smart Booking System Backend

## Project Context for LLMs

### Overview
This is a Java backend for a smart booking system. The project is implemented using Spring Boot and is currently designed with a fully reactive stack using Project Reactor, Spring WebFlux, and Spring Data R2DBC. The system manages bookings, slot availability, and event publishing (Kafka).

### Current State
- **Language:** Java 21
- **Framework:** Spring Boot 3.x
- **Reactive Stack:**
  - Project Reactor (Mono/Flux)
  - Spring WebFlux
  - Spring Data R2DBC
- **Persistence:** PostgreSQL (via R2DBC)
- **Messaging:** Kafka (for booking events)
- **Testing:** JUnit 5, Mockito, StepVerifier (for reactive flows)

### Desired Future State (for migration or LLM code generation)
- **Reactive functional programming only:**
  - Continue using Project Reactor (Mono/Flux)
  - Spring WebFlux for asynchronous, non-blocking REST APIs
  - Spring Data R2DBC for reactive persistence
  - All service and controller methods remain asynchronous (Mono/Flux)
- **Testing:**
  - Parameterized tests using JUnit 5 (@ParameterizedTest, @MethodSource, @CsvSource)
  - Scenario-driven tests with arguments

### Key Domain Models
- `Booking`: Represents a booking record
- `BookingRequest`: DTO for creating a booking

### Example: Reactive vs Non-Reactive Service
```java
// Reactive
public Mono<Booking> createBooking(BookingRequest request);

// Non-Reactive
public Booking createBooking(BookingRequest request);
```

### Example: Reactive Controller Endpoint
```java
@PostMapping
public Mono<Booking> createBooking(@RequestBody BookingRequest request) {
    return bookingService.createBooking(request);
}
```

### Example: Parameterized Test (JUnit 5)
```java
@ParameterizedTest(name = "{0}")
@MethodSource("provideBookingRequests")
@DisplayName("Should create booking for valid requests")
void testCreateBooking(@SuppressWarnings("unused") String scenario, BookingRequest request, Booking expected) {
    // Arrange
    given(bookingService.createBooking(request)).willReturn(Mono.just(expected));

    // Act & Assert
    StepVerifier.create(bookingController.createBooking(request))
            .expectNext(expected)
            .verifyComplete();
    verify(bookingService).createBooking(request);
}

static Stream<Arguments> provideBookingRequests() {
    return Stream.of(
            Arguments.of("basic valid booking", bookingRequest(), booking())
    );
}
```

### Example: StepVerifier for Reactive Testing
```java
StepVerifier.create(bookingService.createBooking(request))
    .expectNextMatches(b -> b.getStatus().equals("CONFIRMED"))
    .verifyComplete();
```

### Example: BDDMockito for Mocking
```java
given(bookingService.createBooking(request)).willReturn(Mono.just(expected));
```

### How to Use This Context
- When prompting an LLM for code generation or refactoring, specify if you want the code in reactive or non-reactive style.
- For tests, request parameterized tests with scenarios as arguments.
- Reference the examples above for method signatures and test patterns.

---

## Project Structure
- `src/main/java/com/example/booking/controller/BookingController.java`: REST API endpoints
- `src/main/java/com/example/booking/service/BookingService.java`: Business logic (reactive)
- `src/main/java/com/example/booking/repository/BookingRepository.java`: Data access (reactive)
- `src/test/java/com/example/booking/unit/controller/BookingControllerTest.java`: Unit tests (reactive, StepVerifier)

---

## Migration Notes
- Continue to follow best practices for reactive functional programming with Project Reactor and Spring WebFlux.
- Ensure all new features and refactoring maintain asynchronous, non-blocking service and controller methods (Mono/Flux).
- Write parameterized tests for all business logic using JUnit 5, with scenario names and arguments for clarity.
- Use StepVerifier for testing reactive flows and BDDMockito for mocking in tests.
- Regularly review and update documentation and code examples to reflect the latest patterns in reactive programming.

---

## Contact
For more information, see the code examples and test files in this repository.
