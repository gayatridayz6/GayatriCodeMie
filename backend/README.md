# Library Catalog Backend (Java Spring Boot)

REST API for the Library Catalog system.

## Tech Stack

- Java 11
- Spring Boot 3.1
- Spring Data JPA
- H2 Database (in-memory)
- JUnit 5

## Build & Run

### Prerequisites

- Java 11+
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

Expected output: 5 tests passed

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/health` | Health check |
| GET | `/api/books` | List all books (supports `?search=query`) |
| GET | `/api/books/{id}` | Get book by ID |
| POST | `/api/books` | Create new book |
| PUT | `/api/books/{id}` | Update book |
| DELETE | `/api/books/{id}` | Delete book |

## Example Requests

### Create Book
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "9780132350884",
    "copies": 3
  }'
```

### List Books
```bash
curl http://localhost:8080/api/books
```

### Search Books
```bash
curl "http://localhost:8080/api/books?search=clean"
```

### Update Book
```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code (2nd Ed)",
    "copies": 5
  }'
```

### Delete Book
```bash
curl -X DELETE http://localhost:8080/api/books/1
```

## Project Structure

```
src/
├── main/java/com/library/
│   ├── LibraryCatalogApplication.java
│   ├── controller/BookController.java
│   ├── service/BookService.java
│   ├── repository/BookRepository.java
│   └── model/Book.java
└── test/java/com/library/
    └── service/BookServiceTest.java
```

---

**Port:** 8080  
**Database:** H2 (in-memory)  
**CORS:** Enabled for http://localhost:3000 (React frontend)
