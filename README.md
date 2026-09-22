# Axlero Solutions - OrderFlow

OrderFlow is a high-performance order ingestion, matching, and trading platform designed with the LMAX Disruptor Ring Buffer architecture.

---

## Architecture Overview (Week 1 Integrated Flow)

```text
React Trading Terminal (Port 5173)
       │
       ▼ (HTTP POST /api/orders)
Spring Boot Order Controller (Port 8080)
       │
       ▼
OrderRequest DTO & Validation
       │
       ▼
OrderIngestionService (Maps to immutable Order domain model)
       │
       ▼
OrderDisruptorEngine (LMAX Ring Buffer Producer)
       │
       ▼
Ring Buffer Pre-Allocated Event Slots
       │
       ▼
OrderEventHandler (Asynchronous single-threaded consumer)
       │
       ▼
MatchingEngineBoundary (Week 2 Matching Engine Entrypoint)
```

---

## Quick Start & Execution Instructions

### 1. Backend (Spring Boot & LMAX Disruptor)

**Prerequisites:** Java 17+, Maven 3.8+

**Run Tests:**
```bash
cd backend
mvn test
```

**Start Application (Port 8080):**
```bash
cd backend
mvn spring-boot:run
```

### 2. Frontend (React 19 & Vite)

**Prerequisites:** Node.js 18+

**Install & Start Dev Server:**
```bash
cd frontend
npm install
npm run dev
```

**Build Production Bundle:**
```bash
cd frontend
npm run build
```

---

## API Reference

### Order Ingestion Endpoint

- **URL:** `POST /api/orders`
- **Content-Type:** `application/json`

**Sample Request Body:**
```json
{
  "accountId": "ACC001",
  "side": "BUY",
  "orderType": "LIMIT",
  "price": 100.50,
  "quantity": 10
}
```

**Sample Response Body (HTTP 201 Created):**
```json
{
  "orderId": 1001,
  "accountId": "ACC001",
  "side": "BUY",
  "orderType": "LIMIT",
  "price": 100.50,
  "quantity": 10,
  "status": "SUBMITTED",
  "timestamp": 1790074754388,
  "sequence": 0
}
```
