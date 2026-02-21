
---

## KKBackEnd `README.md`

```md
# KleverKapital Backend (KKBackEnd)

A Spring Boot (Java 17) backend service intended to power finance/pricing APIs used by the KleverKapital dashboard (and other clients).

It includes OpenAPI (Swagger UI) for interactive API documentation and uses quant/finance libraries for pricing and modeling.

## Tech stack
- Java 17
- Spring Boot (Web)
- OpenAPI / Swagger UI (springdoc)
- finmath-lib
- jQuantLib
- Lombok

## Run locally

### Prerequisites
- Java 17

### Start the server
```bash
./mvnw spring-boot:run
