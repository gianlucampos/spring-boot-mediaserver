# Spring Boot Media Server with Ngrok

Simple media server in **Spring Boot 25**, running in **Docker** with a public tunnel via **ngrok**.

---

## Technologies

- Java 25 / Spring Boot
- Docker & Docker Compose
- Ngrok for external access
- Lombok for logging

---

## Requirements

- Java JDK 25 (for local build, optional if using Docker)
- Docker and Docker Compose installed
- Ngrok account with `NGROK_AUTHTOKEN` for stable URLs
- Add a env file with`HOST_MEDIA_DIR` for the storadge and token for ngrok

---

## Useful Commands

### Build and start the containers

```bash
    docker compose up --build
