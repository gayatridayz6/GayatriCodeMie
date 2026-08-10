# Library Catalog - Java Spring Boot + React.js

A full-stack Java/Spring Boot and React application. The current feature branch adds accommodation search price display and advanced currency filtering for USD, EUR, and GBP using backend-provided currency reference metadata.

## Features

- ✅ Add, view, update, and delete books
- ✅ Search by title, author, or ISBN
- ✅ Track number of copies available
- ✅ Duplicate ISBN protection
- ✅ Accommodation search results with nightly and total price display
- ✅ Advanced currency selector for USD, EUR, GBP
- ✅ Currency labels sourced from the backend Currency_Codes data source (`<Code> <Numeric> <Currency name>`)
- ✅ Backend FX conversion flow with clear metadata/FX error contracts
- ✅ Modern React UI with Axios
- ✅ REST API with Spring Boot
- ✅ JUnit tests for backend
- ✅ Jest/React scripts configuration for frontend

## Tech Stack

- **Backend:** Java 11+, Spring Boot 3.1, Maven
- **Frontend:** React 18, JavaScript ES6, Axios, CSS3
- **Database:** H2 (in-memory) - easily swap to PostgreSQL
- **Testing:** JUnit 5, Jest, React Testing Library

## Project Structure

```
library-catalog/
├── backend/                    (Spring Boot Java)
│   ├── src/
│   │   ├── main/java/com/library/
│   │   │   ├── LibraryCatalogApplication.java
│   │   │   ├── config/CurrencyCodeDataLoader.java
│   │   │   ├── controller/
│   │   │   │   ├── AccommodationController.java
│   │   │   │   ├── ApiExceptionHandler.java
│   │   │   │   ├── BookController.java
│   │   │   │   └── CurrencyController.java
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   │   ├── Book.java
│   │   │   │   └── CurrencyCode.java
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── test/java/com/library/service/
│   ├── pom.xml
│   └── README.md
├── frontend/                   (React.js)
│   ├── public/index.html
│   ├── src/
│   │   ├── App.js
│   │   ├── App.css
│   │   ├── index.js
│   │   └── services/api.js
│   ├── package.json
│   └── README.md
└── README.md (this file)
```

## Quick Start

### Backend Setup (Java/Maven)

```powershell
cd backend

# Build
mvn clean install

# Run (starts on http://localhost:8080)
mvn spring-boot:run

# Run Tests
mvn test
```

### Frontend Setup (React)

```powershell
cd frontend

# Install dependencies
npm install

# Start dev server (http://localhost:3000)
npm start

# Build / lint
npm run build

# Run tests
npm test
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/health` | Health check |
| GET | `/api/books` | List books (supports `?search=query`) |
| POST | `/api/books` | Create new book |
| PUT | `/api/books/{id}` | Update book |
| DELETE | `/api/books/{id}` | Delete book |
| GET | `/api/v1/currencies?codes=USD,EUR,GBP` | Supported currency metadata |
| GET | `/api/v1/accommodations?currency=USD` | Accommodation results in selected display currency |

## Currency Response Example

```json
{
  "data": [
    { "code": "USD", "numeric": 840, "name": "United States dollar", "label": "USD 840 United States dollar" },
    { "code": "EUR", "numeric": 978, "name": "Euro", "label": "EUR 978 Euro" },
    { "code": "GBP", "numeric": 826, "name": "Pound sterling", "label": "GBP 826 Pound sterling" }
  ]
}
```

## Accommodation Response Example

```json
{
  "meta": {
    "appliedCurrency": { "code": "USD", "numeric": 840, "name": "United States dollar", "label": "USD 840 United States dollar" },
    "fxAsOf": "2026-08-07T00:00:00Z",
    "conversionUsed": false
  },
  "data": [
    {
      "id": "acc_123",
      "name": "Central Hotel",
      "price": {
        "nightly": { "amount": 199.99, "currencyCode": "USD" },
        "total": { "amount": 599.97, "currencyCode": "USD" }
      }
    }
  ]
}
```

## Development Workflow

1. **Start Backend:** `cd backend && mvn spring-boot:run`
2. **Start Frontend:** `cd frontend && npm start`
3. **Open Browser:** http://localhost:3000
4. **Backend APIs:** http://localhost:8080/api/books, http://localhost:8080/api/v1/accommodations
5. **Run Tests:**
   - Backend: `mvn test` (in backend folder)
   - Frontend: `npm test` (in frontend folder)

---

**Ready to use.** Both backend and frontend configured for local development with live reload.
