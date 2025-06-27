# 🚀 Smart Booking System

A full-stack application to handle real-time service bookings, slot management, and asynchronous processing. Built with Java (Spring Boot), React, Kafka, Redis, and PostgreSQL.

---

## 📚 Features

- Real-time availability and instant bookings
- Booking amendments and retries (idempotent)
- Redis caching for availability
- Kafka for event-driven booking workflows
- Fully containerized with Docker and CI/CD using GitHub Actions

---

## 🛠️ Tech Stack

| Layer      | Tech                          |
|------------|-------------------------------|
| Backend    | Java 21, Spring Boot, Kafka, Redis, PostgreSQL |
| Frontend   | React + Vite                  |
| DevOps     | Docker, GitHub Actions, Prometheus, Grafana  |
| Monitoring | OpenTelemetry (planned)       |

---

## 🚧 Project Setup

### Prerequisites
- Docker & Docker Compose
- JDK 21
- Node.js 18+

### Run Everything Locally

```bash
# From root
docker-compose up --build
```
### Run Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Run Frontend
```bash
cd frontend
npm install
npm run dev
```

[![codecov](https://codecov.io/gh/dappilik/dappilik/branch/main/graph/badge.svg)](https://codecov.io/gh/dappilik/smart-booking-system)

# 📐 Architecture Diagram (Planned)
User → React App → Spring Boot API → DB/Redis
                        ↓
                    Kafka
                        ↓
                    Async Booking Worker
