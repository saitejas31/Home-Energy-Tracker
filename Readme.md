# 🔋 Home Energy Tracker

A **production-style microservices system** that ingests, processes, and analyses household energy consumption in real time — then uses a local AI model to generate personalised savings recommendations.

Built as a portfolio project to demonstrate end-to-end microservices architecture with Spring Boot, event-driven patterns (Kafka), time-series storage (InfluxDB), AI integration (Spring AI + Ollama), and resilience engineering (Resilience4j).

---

## Architecture

```
External Client
      │
      ▼
┌─────────────────────────────────────────────────────┐
│            API Gateway  (:9000)                     │
│  Spring Cloud Gateway MVC + Resilience4j            │
└──┬──────────┬──────────┬──────────┬─────────────────┘
   │          │          │          │
   ▼          ▼          ▼          ▼
user-     device-    ingestion-  usage-         insight-
service   service    service     service ──────► service
(:8080)   (:8081)    (:8082)     (:8083)         (:8085)
MySQL     MySQL      (no DB)     InfluxDB        Ollama/llama2
                         │           │
                    Kafka topic:  Kafka topic:
                    energy-usage  energy-alerts
                                      │
                                      ▼
                                  alert-service
                                    (:8084)
                                  MySQL + SMTP
```

### Kafka Event Flow

```
POST /api/v1/ingestion
      ↓
[topic: energy-usage] → usage-service → InfluxDB
                              ↓ (every 10 seconds)
                         aggregate per user
                              ↓
                    threshold exceeded?
                              ↓ yes
                    [topic: energy-alerts] → alert-service
                                                  ↓
                                            email via Mailpit
                                            + audit log (MySQL)
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Services | Spring Boot 3.x / 4.x, Java 21 |
| Messaging | Apache Kafka (KRaft mode, no ZooKeeper) |
| Time-series DB | InfluxDB 2.7 |
| Relational DB | MySQL 8.3 |
| AI / LLM | Spring AI + Ollama (llama2) |
| API Gateway | Spring Cloud Gateway MVC |
| Resilience | Resilience4j (circuit breaker, retry) |
| Email (dev) | Mailpit (SMTP mock) |
| Auth | Keycloak 24 *(configured, not yet integrated in services)* |
| Observability | Micrometer + Prometheus actuator endpoints *(Grafana stack not yet wired)* |
| Containerisation | Docker Compose |
| Build | Maven, Java 21 |

---

## Services

| Service | Port | Description |
|---|---|---|
| [user-service](user-service/) | 8080 | User profiles, alerting preferences, energy thresholds |
| [device-service](device-service/) | 8081 | CRUD for household devices; links devices to users |
| [ingestion-service](ingestion-service/) | 8082 | Accepts energy readings via REST, publishes to Kafka |
| [usage-service](usage-service/) | 8083 | Kafka consumer → InfluxDB writer; threshold alerting scheduler; usage query API |
| [alert-service](alert-service/) | 8084 | Kafka consumer → sends alert emails; audits sends to MySQL |
| [insight-service](insight-service/) | 8085 | Fetches usage data; prompts local Ollama LLM for savings tips |
| [api-gateway](api-gateway/) | 9000 | Single entry point; routing + circuit breakers |

---

## How to Run Locally

### Prerequisites

| Requirement | Version |
|---|---|
| Docker + Docker Compose | ≥ 24 |
| Java | 21 |
| Maven | ≥ 3.9 |
| Ollama | Latest ([ollama.com](https://ollama.com)) |

### 1. Configure environment

Create a `.env` file in the project root (already git-ignored):

```env
MYSQL_ROOT_PASSWORD=your_password
MYSQL_PASSWORD=your_password
INFLUX_TOKEN=my-token
```

> The InfluxDB token `my-token` is the default set in docker-compose for local development. Change it for any non-local deployment.

### 2. Pull the Ollama model

```bash
ollama pull llama2
```

### 3. Start infrastructure

```bash
docker compose up -d
```

This starts: MySQL, Kafka (KRaft), Kafka UI, InfluxDB, Mailpit, Keycloak.

| UI | URL |
|---|---|
| Kafka UI | http://localhost:8070 |
| InfluxDB | http://localhost:8072 |
| Mailpit (email preview) | http://localhost:8025 |
| Keycloak admin | http://localhost:8091 |

### 4. Start services (each in a separate terminal)

```bash
# Start in order (user-service first to run Flyway migrations)
cd user-service   && ./mvnw spring-boot:run
cd device-service && ./mvnw spring-boot:run
cd ingestion-service && ./mvnw spring-boot:run
cd usage-service  && ./mvnw spring-boot:run
cd alert-service  && ./mvnw spring-boot:run
cd insight-service && ./mvnw spring-boot:run
cd api-gateway    && ./mvnw spring-boot:run
```

> **Note**: `user-service` must start first — it runs the Flyway migrations that create the `user`, `device`, and `alert` tables.

### 5. Example API calls (via gateway)

```bash
# Create a user
curl -X POST http://localhost:9000/api/v1/user \
  -H "Content-Type: application/json" \
  -H "X-Client-Type: web" \
  -d '{"name":"Alice","email":"alice@example.com","alerting":true,"energyAlertingThreshold":5.0}'

# Register a device
curl -X POST http://localhost:9000/api/v1/device \
  -H "Content-Type: application/json" \
  -H "X-Client-Type: web" \
  -d '{"name":"Fridge","deviceType":"REFRIGERATOR","location":"Kitchen","userId":1}'

# Ingest an energy reading
curl -X POST http://localhost:9000/api/v1/ingestion \
  -H "Content-Type: application/json" \
  -d '{"deviceId":1,"energyConsumed":1.5,"timestamp":"2026-08-11T10:00:00Z"}'

# Get AI savings tips for user 1
curl http://localhost:9000/api/v1/insight/saving-tips/1
```

---

## Database Schema

Managed by **Flyway** (runs automatically on `user-service` startup):

```
user       — id, name, surname, email, address, alerting, energy_alerting_threshold
device     — id, name, type, location, user_id → user.id
alert      — id, user_id, sent, created_at
```

InfluxDB measurement: `energy_usage` — tags: `deviceId`, fields: `energyConsumed`.

---

## Current Status & Known Limitations

This is an active portfolio project. The core pipeline is functional end-to-end:
**ingest → Kafka → InfluxDB → threshold check → alert email → AI insight**

Known limitations and in-progress items:

| Area | Status |
|---|---|
| **Keycloak / JWT auth** | Container configured, realm mounted — **not yet wired into service code or gateway JWT validation** |
| **Grafana / Prometheus** | Actuator endpoints enabled in usage-service and insight-service — Prometheus/Grafana containers not yet in docker-compose |
| **Ollama** | Requires local Ollama install + `ollama pull llama2`; no container in compose |
| **Tests** | Only Spring context-load tests exist; no unit or integration tests yet |
| **N+1 in usage-service** | Scheduled aggregation calls device-service once per device per 10 s cycle — batch endpoint planned |
| **Circuit breakers** | Configured in gateway for device-service; `usage-service` has Resilience4j in pom.xml but annotations not yet applied |
| **Service discovery** | All URLs are hardcoded `localhost:PORT`; no Eureka/Consul |
| **OllamaConfig** | Creates a `ChatClient` bean with a system prompt, but `InsightService` uses `OllamaChatModel` directly — system prompt not applied |

---

## Roadmap

- [ ] Wire Keycloak JWT validation into API gateway and downstream services
- [ ] Add Prometheus + Grafana to docker-compose
- [ ] Add Ollama to docker-compose (sidecar container)
- [ ] Replace per-device REST calls in usage-service with a batch `/device/batch` endpoint
- [ ] Apply Resilience4j `@CircuitBreaker` + `@Retry` annotations on usage-service HTTP clients
- [ ] Write unit tests (service layer) and integration tests (Testcontainers for Kafka + InfluxDB)
- [ ] Add input validation (`@Valid`) on all REST request bodies
- [ ] Expose Kafka dead-letter topic for failed alert events
- [ ] Fix insight-service to use the `ChatClient` bean (with system prompt) instead of raw `OllamaChatModel`
- [ ] Add `usage-service` route to gateway (currently proxied but not circuit-broken)

---

## Repository Structure

```
Home-Energy-Tracker/
├── docker/
│   ├── mysql/init.sql          # DB initialisation
│   └── keycloak/realms/        # Keycloak realm import
├── docker-compose.yml
├── user-service/
├── device-service/
├── ingestion-service/
├── usage-service/
├── alert-service/
├── insight-service/
└── api-gateway/
```

Each service folder is a self-contained Spring Boot Maven project.

---

## Branch Strategy

This is a **monorepo** with one `.git` root. Branches are named after services and each contains the full repository state at the time of that service's latest commit. All branches currently point to the same tip commit.

| Branch | Remote |
|---|---|
| `main` | default / README |
| `user-service` | ✅ pushed |
| `device-service` | ✅ pushed |
| `ingestion-service` | ✅ pushed |
| `usage-service` | ✅ pushed |
| `alert-service` | ✅ pushed |
| `insight-service` | ✅ pushed |
| `api-gateway` | ✅ pushed |
