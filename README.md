# MyTestApp_CodeMie - Three Flask Starter Projects

A collection of three independent, minimal Flask applications demonstrating full-stack development with Python/Flask.

## Projects

### 1. Inventory Tracker
A basic inventory/asset tracking system with CRUD operations, low-stock filtering, and search.
- **Path:** `inventory_tracker/`
- **Features:** Create/read/update/delete items, search by name/SKU/location, low-stock alerts
- **Tech:** Flask, SQLite (in-memory for MVP), Pytest

### 2. Library Catalog
A book library catalog with search and borrowing records.
- **Path:** `library_catalog/`
- **Features:** Add books, search by title/author/ISBN, track copies
- **Tech:** Flask, Pytest

### 3. Taskboard Manager
A personal task/kanban board with status filtering.
- **Path:** `taskboard_manager/`
- **Features:** Create tasks, filter by status (todo/doing/done)
- **Tech:** Flask, Pytest

## Quick Start (All Projects)

Each project is independent and follows the same pattern:

```powershell
# Navigate to a project
cd C:\Users\gayatri_mungarwadi\Documents\Capston\MyTestApp_CodeMie\inventory_tracker

# Create and activate virtual environment
python -m venv .venv
.\.venv\Scripts\Activate.ps1

# Install dependencies
pip install -r requirements.txt

# Run tests
pytest -q

# Run app (opens http://127.0.0.1:5000/)
python run.py
```

Repeat for `library_catalog/` and `taskboard_manager/`.

## Project Structure

Each project contains:
```
project_name/
├── app/
│   ├── __init__.py          (Flask app factory)
│   ├── routes.py            (API routes + home)
│   ├── store.py             (In-memory data + validators)
│   ├── templates/
│   │   └── index.html       (Simple web UI)
│   └── static/
│       ├── app.js           (Frontend logic)
│       └── styles.css       (Styling)
├── tests/
│   └── test_api.py          (Pytest tests)
├── run.py                   (Entry point)
├── requirements.txt         (Dependencies)
└── README.md                (Project-specific guide)
```

## Notes

- All projects use **in-memory storage** for MVP; swap to SQLAlchemy + SQLite/PostgreSQL later.
- Each app runs on `http://127.0.0.1:5000/` independently.
- Tests are lightweight and focus on core API paths.
- No external frontend framework; vanilla HTML/CSS/JS for simplicity.

---

**Created:** August 2026  
**IntelliJ Ready:** Yes, each app can be run as a separate Flask run configuration.
