# Library Catalog - Java Spring Boot + React.js

A full-stack book library management system with Java backend and React frontend.

## Features

- ✅ Add, view, update, and delete books
- ✅ Search by title, author, or ISBN
- ✅ Track number of copies available
- ✅ Duplicate ISBN protection
- ✅ Modern React UI with Axios
- ✅ REST API with Spring Boot
- ✅ JUnit tests for backend
- ✅ Jest tests for frontend

## Tech Stack

- **Backend:** Java 11+, Spring Boot 3.0, Maven
- **Frontend:** React 18, JavaScript ES6, Axios, CSS3
- **Database:** H2 (in-memory) - easily swap to PostgreSQL
- **Testing:** JUnit 5, Jest, React Testing Library

## Project Structure

```
library-catalog/
├── backend/                    (Spring Boot Java)
│   ├── src/
│   │   ├── main/java/com/library/
│   │   │   ├── Application.java
│   │   │   ├── controller/BookController.java
│   │   │   ├── service/BookService.java
│   │   │   ├── repository/BookRepository.java
│   │   │   └── model/Book.java
│   │   └── test/java/com/library/BookServiceTest.java
│   ├── pom.xml
│   └── README.md
├── frontend/                   (React.js)
│   ├── public/index.html
│   ├── src/
│   │   ├── App.js
│   │   ├── components/
│   │   │   ├── BookForm.js
│   │   │   ├── BookList.js
│   │   │   └── SearchBar.js
│   │   ├── services/api.js
│   │   ├── App.css
│   │   └── index.js
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

## Example Request

```bash
POST http://localhost:8080/api/books
Content-Type: application/json

{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "copies": 3
}
```

## Development Workflow

1. **Start Backend:** `cd backend && mvn spring-boot:run`
2. **Start Frontend:** `cd frontend && npm start`
3. **Open Browser:** http://localhost:3000
4. **Backend API:** http://localhost:8080/api/books
5. **Run Tests:**
   - Backend: `mvn test` (in backend folder)
   - Frontend: `npm test` (in frontend folder)

---

**Ready to use.** Both backend and frontend configured for local development with live reload.
