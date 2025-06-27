# 🚀 Smart Booking System

A full-stack system for real-time service bookings with support for asynchronous workflows, fault tolerance, and test automation. Built using modern technologies like Java, Spring Boot, React, Kafka, and Redis.

[![Backend Coverage](https://codecov.io/gh/dappilik/smart-booking-system/branch/main/graph/badge.svg?flag=backend)](https://codecov.io/gh/dappilik/smart-booking-system)
[![Frontend Coverage](https://codecov.io/gh/dappilik/smart-booking-system/branch/main/graph/badge.svg?flag=frontend)](https://codecov.io/gh/dappilik/smart-booking-system)
[![CI Status](https://github.com/dappilik/smart-booking-system/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/dappilik/smart-booking-system/actions/workflows/backend-ci.yml)
[![CI Status](https://github.com/dappilik/smart-booking-system/actions/workflows/frontend-ci.yml/badge.svg)](https://github.com/dappilik/smart-booking-system/actions/workflows/frontend-ci.yml)

---

## 📚 Features

- ⏱️ Real-time slot availability and instant booking
- ♻️ Idempotent booking amendments and retries
- 🧠 Redis caching for performance
- 📨 Kafka-based async event-driven processing
- 🧪 Isolated unit, integration, and smoke tests
- 📈 Code coverage tracked for frontend and backend independently
- 📦 Fully containerized via Docker
- 🚦 CI/CD via GitHub Actions + Codecov enforcement

---

## 🛠️ Tech Stack

| Layer      | Tech Stack                                      |
|------------|--------------------------------------------------|
| Backend    | Java 21, Spring Boot 3.5, Kafka, Redis, PostgreSQL |
| Frontend   | React 18, Vite, TypeScript                       |
| DevOps     | Docker, GitHub Actions, JaCoCo, Codecov          |
| Monitoring | Prometheus, Grafana (✅), OpenTelemetry (🔜)     |

---

## 🧪 Test Structure

| Type           | Scope                            | Coverage |
|----------------|----------------------------------|----------|
| Unit Tests     | Method-level logic                | ✅       |
| Integration    | Component interactions + DB       | ✅       |
| Smoke Tests    | End-to-end path validation        | ✅       |
| Coverage Tags  | Frontend + Backend tracked separately with Codecov flags |

---

## 🚧 Local Development

### 🐳 Run All Services

```bash
docker-compose up --build
```

### 🖥️ Run Backend Separately

```bash
cd backend
./mvnw spring-boot:run
```

### 💻 Run Frontend Separately

```bash
cd frontend
npm ci
npm run dev
```

---

## ✅ Continuous Integration

Each PR triggers GitHub Actions workflows to:
- Run unit, integration, and smoke tests
- Collect coverage separately for frontend and backend
- Enforce coverage thresholds
- Block PRs unless all tests pass and coverage is sufficient

---

## 🧠 Architecture Overview

```
User → React Frontend → Spring Boot API → PostgreSQL / Redis
                          ↓
                        Kafka
                          ↓
                  Booking Worker (Async)
```

---

## 🔒 Quality Gates

- ✅ Coverage must pass minimum thresholds for both frontend and backend
- ✅ Tests must succeed for PRs to merge
- 🚫 No coverage for test files or mocks (ignored via config)

---

## 📈 Future Enhancements

- [ ] Distributed tracing with OpenTelemetry
- [ ] UI test automation (Cypress/Playwright)
- [ ] Rate limiting + circuit breaker support

---

## 👥 Contributors

Maintained by [@dappilik](https://github.com/dappilik) and open to collaboration.