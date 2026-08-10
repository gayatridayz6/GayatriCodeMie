# Library Catalog Backend (Java Spring Boot)

REST API for the Library Catalog system plus the EPMCDMETST-58713 accommodation currency display APIs.

## Tech Stack

- Java 17
- Spring Boot 3.1
- Spring Data JPA
- H2 Database (in-memory)
- JUnit 5

## Build & Run

### Prerequisites

- Java 17+
- Maven 3.6+

### Setup

```powershell
cd backend

# Build
mvn clean install

# Run (starts on http://localhost:8080)
mvn spring-boot:run
```

### Run Tests

```powershell
mvn test
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/health` | Health check |
| GET | `/api/books` | List all books (supports `?search=query`) |
| GET | `/api/books/{id}` | Get book by ID |
| POST | `/api/books` | Create new book |
| PUT | `/api/books/{id}` | Update book |
| DELETE | `/api/books/{id}` | Delete book |
| GET | `/api/v1/currencies?codes=USD,EUR,GBP` | Return supported currency metadata from Currency_Codes |
| GET | `/api/v1/accommodations?currency=USD` | Return accommodation prices converted to selected currency |

## Currency Metadata Example

```bash
curl "http://localhost:8080/api/v1/currencies?codes=USD,EUR,GBP"
```

```json
{
  "data": [
    { "code": "USD", "numeric": 840, "name": "United States dollar", "label": "USD 840 United States dollar" },
    { "code": "EUR", "numeric": 978, "name": "Euro", "label": "EUR 978 Euro" },
    { "code": "GBP", "numeric": 826, "name": "Pound sterling", "label": "GBP 826 Pound sterling" }
  ]
}
```

## Accommodation Example

```bash
curl "http://localhost:8080/api/v1/accommodations?currency=GBP"
```

The response includes `meta.appliedCurrency`, `meta.fxAsOf`, and each accommodation `price.nightly` / `price.total` in the selected currency.

---

**Port:** 8080  
**Database:** H2 (in-memory)  
**CORS:** Enabled for http://localhost:3000 (React frontend)
