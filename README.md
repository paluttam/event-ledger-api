# Event Ledger API

## Overview

Event Ledger API is a Spring Boot REST application that manages financial transaction events from multiple upstream systems.

The application supports:

* Event ingestion
* Idempotent event processing
* Out-of-order event handling
* Balance computation
* Validation and error handling
* Swagger/OpenAPI documentation
* Automated testing

---

## Technology Stack

* Java 17
* Spring Boot 3.3
* Spring Data JPA
* H2 In-Memory Database
* Bean Validation
* Swagger / OpenAPI
* JUnit 5
* Mockito
* Maven

---

## Requirements Implemented

### Idempotency

Duplicate submissions with the same `eventId` do not create additional records or affect account balances.

Behavior:

* First request returns `201 Created`
* Duplicate request returns `200 OK`
* Original event is returned

---

### Out-of-Order Event Handling

Events may arrive in any order.

Example:

Event A timestamp = 10:00

Event B timestamp = 09:00

Even if Event A arrives before Event B, the API always returns events ordered by `eventTimestamp`.

---

### Balance Computation

Balance is calculated as:

```text
balance = SUM(CREDIT) - SUM(DEBIT)
```

Example:

```text
CREDIT 100
CREDIT 50
DEBIT 30

Balance = 120
```

---

### Validation

The API validates:

* eventId is required
* accountId is required
* type is required
* amount must be greater than zero
* currency is required
* eventTimestamp is required

Invalid requests return HTTP 400.

---

## API Endpoints

### Create Event

```http
POST /events
```

Request:

```json
{
  "eventId": "evt-001",
  "accountId": "acct-123",
  "type": "CREDIT",
  "amount": 150.00,
  "currency": "USD",
  "eventTimestamp": "2026-05-15T14:02:11Z",
  "metadata": "sample metadata"
}
```

Response:

```http
201 Created
```

Duplicate submission:

```http
200 OK
```

---

### Get Event By Id

```http
GET /events/{id}
```

Example:

```http
GET /events/evt-001
```

---

### Get Events By Account

```http
GET /events?account=acct-123
```

Returns all events ordered by event timestamp.

---

### Get Account Balance

```http
GET /accounts/{accountId}/balance
```

Example:

```http
GET /accounts/acct-123/balance
```

Response:

```json
{
  "accountId": "acct-123",
  "balance": 120.00
}
```

---

## Running the Application

### Prerequisites

* Java 17+
* Maven 3.9+

Verify installation:

```bash
java -version
mvn -version
```

---

### Start Application

```bash
mvn spring-boot:run
```

Application starts at:

```text
http://localhost:8080
```

---

## Swagger Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

---

## H2 Database Console

H2 Console:

```text
http://localhost:8080/h2-console
```

Connection details:

```text
JDBC URL: jdbc:h2:mem:eventdb
Username: sa
Password:
```

---

## Running Tests

Execute all tests:

```bash
mvn test
```

Generate test coverage report:

```bash
mvn clean verify
```

Coverage report:

```text
target/site/jacoco/index.html
```

---

## Design Decisions

### Idempotency Strategy

The `eventId` is used as the primary key.

A duplicate insert triggers a database constraint violation and the existing event is returned.

This approach is concurrency-safe and prevents duplicate balance updates.

---

### Out-of-Order Event Support

Events are stored exactly as received.

Queries retrieve events ordered by:

```text
eventTimestamp ASC
```

This guarantees chronological ordering regardless of arrival sequence.

---

### Balance Computation

Balance is computed dynamically from persisted events:

```text
CREDIT => positive amount
DEBIT => negative amount
```

This guarantees correctness even when events arrive out of order.

---

## Testing

The test suite covers:

* Event creation
* Duplicate event handling
* Event retrieval
* Account balance calculation
* Validation failures
* Invalid event types
* Resource not found scenarios
* Out-of-order event ordering
* Repository behavior
* Exception handlers
* Configuration classes

---

## Future Improvements

Possible production enhancements:

* Pagination for event listing
* Docker deployment
* PostgreSQL support
* Event versioning
* Authentication and authorization
* Account balance materialization for large datasets
* Distributed idempotency handling

---

## Author

Uttam Pal
