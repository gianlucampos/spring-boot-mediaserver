# Spring Boot Media Server

Simple **Spring Boot 25** media server running inside **Docker**, accessible from the internet via **Ngrok** *(paid for stable URLs)* 
or **Tailscale** *(free alternative)*.

---

## Technologies

- **Java 25 / Spring Boot**
- **Lombok** for logging and simplicity
- **Docker & Docker Compose**
- **Ngrok** *(optional public tunnel)*
- **Tailscale** *(optional secure P2P VPN)*

---

## Requirements

- **Java JDK 25** *(for local build; optional if using Docker)*
- **Docker** and **Docker Compose** installed
- **Ngrok** account + `NGROK_AUTHTOKEN` *(for stable URLs — optional)*
- **Tailscale** account *(for private peer-to-peer access — optional)*
- `.env` file with:
  ```bash
  HOST_MEDIA_DIR=/path/to/media
  NGROK_AUTHTOKEN=your_ngrok_token_here   # optional
  TAILSCALE_AUTHKEY=your_tailscale_key_here  # optional
---

## Useful Commands

### Build and start the containers

```bash
    docker compose up --build
