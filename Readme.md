# Qualify Guru V2 | Core API

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)
![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)

An enterprise-grade, REST API that leverages Artificial Intelligence to optimize candidate resumes. Built with a strict **Modular Monolith** architecture using Spring Modulith, this system ensures high cohesion, domain isolation, and scalability.

## Architecture & Enterprise Patterns

Qualify Guru V2 implements production-ready backend patterns:

*   **Modular Monolith Design:** Strict logical separation between domains (Identity, Candidate Profile, Optimization, Statistics) preventing architectural degradation.
*   **Defensive Engineering (Rate Limiting):** Implements a two-tier Token Bucket algorithm via `Bucket4j` to enforce global and user-specific request limits, protecting third-party AI APIs from DDoS or runaway costs.
*   **Database Version Control:** Uses `Flyway` for deterministic, version-controlled schema migrations across all environments.
*   **Hybrid Cloud Storage:** Utilizes `MinIO` for local development with a seamless transition path to `AWS S3` for production object storage.
*   **Stateless Authentication:** Secured via JSON Web Tokens (JWT) and Spring Security.
*   **Event-Driven Communication:** Asynchronous processing between internal modules using Spring Modulith Events.

## Tech Stack

*   **Core:** Java 21, Spring Boot 3 (Spring Modulith, Spring Web, Spring Security)
*   **Database & Migrations:** MySQL, H2 (In-memory testing), Flyway
*   **Storage & AI:** AWS S3 SDK (via MinIO), Google Gemini AI API
*   **DevOps & CI/CD:** Docker, Docker Compose, Multi-stage Dockerfiles, GitHub Actions

## Getting Started (Local Development)

The entire infrastructure (Database, Object Storage, and API) is containerized and orchestrated via Docker Compose.

### Prerequisites
*   Docker & Docker Compose installed.
*   A `.env` file in the root directory containing your secrets:
    ```env
    JWT_SECRET=your_base64_encoded_secret_key
    GEMINI_API_KEY=your_google_ai_key
    ```

### Running the Stack
Spin up the MySQL database, MinIO storage, and the Spring Boot application with a single command:

```bash
docker compose up -d --build
```

The API will be available at `http://localhost:8080`.

### Testing

The test suite runs in complete isolation utilizing an in-memory H2 database. It bypasses complex schema migrations during testing in favor of rapid, schema-on-the-fly execution for sliced module tests.

Run the test suite:
```bash
mvn clean test
```

## Core API Endpoints

| **Method** | **Endpoint** | **Description** | **Auth Required** |
| ---------- | ------------ | --------------- | ----------------- |
| `POST` | `/api/v1/auth/login` | Authenticates a user and returns a JWT. | No |
| `POST` | `/api/v1/candidates/resumes` | Uploads a resume to S3 storage. | Yes |
| `POST` | `/api/v1/resume-optimization` | Triggers AI analysis on a stored resume. | Yes |

> **Note:** Protected endpoints utilize Bucket4j rate limiting. Exceeding limits will result in a `429 Too Many Requests` response.

## Author

**Matheus de Sousa Almeida (MTR-S)**

---