# Library Catalog

A minimal book library catalog system built with Flask.

## Features

- ✅ Create, read, update, delete (CRUD) books
- ✅ Search by title, author, or ISBN
- ✅ Track number of copies
- ✅ Duplicate ISBN protection
- ✅ Clean web UI with real-time updates
- ✅ Pytest test suite

## Tech Stack

- **Backend:** Flask 3.0.3
- **Frontend:** Vanilla HTML/CSS/JavaScript
- **Testing:** Pytest 8.3.2
- **Data:** In-memory (Python list)

## Quick Start

### 1. Setup Environment

```powershell
cd C:\Users\gayatri_mungarwadi\Documents\Capston\MyTestApp_CodeMie\library_catalog

# Create virtual environment
python -m venv .venv

# Activate it
.\.venv\Scripts\Activate.ps1

# Install dependencies
pip install -r requirements.txt
```

### 2. Run Tests

```powershell
pytest -q
```

Expected output:
```
5 passed in 0.05s
```

### 3. Run App

```powershell
python run.py
```

Open browser to: `http://127.0.0.1:5000/`

## Project Structure

```
library_catalog/
├── app/
│   ├── __init__.py         (Flask app factory)
│   ├── routes.py           (API routes)
│   ├── store.py            (Data & validation logic)
│   ├── templates/
│   │   └── index.html      (UI template)
│   └── static/
│       ├── app.js          (Frontend logic)
│       └── styles.css      (Styling)
├── tests/
│   └── test_api.py         (Pytest tests)
├── run.py                  (Entry point)
├── requirements.txt        (Dependencies)
└── README.md               (This file)
```

## API Routes

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Health check |
| GET | `/api/books` | List books (supports `q` search) |
| POST | `/api/books` | Create book |
| PUT | `/api/books/<id>` | Update book |
| DELETE | `/api/books/<id>` | Delete book |

## Data Model

Each book has:
- `id` (integer)
- `title` (string, required)
- `author` (string, required)
- `isbn` (string, unique, required)
- `copies` (integer ≥ 0, default: 1)

## Example Payload

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "copies": 5
}
```

## Next Steps

- Replace in-memory store with SQLite + SQLAlchemy
- Add borrower records (who borrowed which book, return dates)
- Add overdue book notifications
- Add CSV import/export
- Add book cover images
- Add category/genre filtering

---

**Created:** August 2026  
**Running on:** Flask 3.0.3
