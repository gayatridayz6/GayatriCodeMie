# Inventory Tracker

A minimal inventory/asset tracking system built with Flask.

## Features

- ✅ Create, read, update, delete (CRUD) inventory items
- ✅ Search by name, SKU, or location
- ✅ Low-stock filtering (qty ≤ min_stock)
- ✅ Duplicate SKU protection
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
cd C:\Users\gayatri_mungarwadi\Documents\Capston\MyTestApp_CodeMie\inventory_tracker

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
.. 6 passed in 0.05s
```

### 3. Run App

```powershell
python run.py
```

Open browser to: `http://127.0.0.1:5000/`

## Project Structure

```
inventory_tracker/
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
| GET | `/api/items` | List items (supports `q` search, `low_stock` filter) |
| POST | `/api/items` | Create item |
| PUT | `/api/items/<id>` | Update item |
| DELETE | `/api/items/<id>` | Delete item |

## Data Model

Each item has:
- `id` (integer)
- `name` (string, required)
- `sku` (string, unique, required)
- `quantity` (integer ≥ 0)
- `min_stock` (integer ≥ 0, default: 0)
- `location` (string, optional)
- `updated_at` (ISO 8601 timestamp)

## Example Payload

```json
{
  "name": "Laptop",
  "sku": "TECH-001",
  "quantity": 5,
  "min_stock": 1,
  "location": "Server Room"
}
```

## Next Steps

- Replace in-memory store with SQLite + SQLAlchemy
- Add pagination and sorting
- Add user authentication
- Add CSV import/export
- Add barcode scanning

---

**Created:** August 2026  
**Running on:** Flask 3.0.3
