# Taskboard Manager

A minimal personal task/kanban board built with Flask.

## Features

- ✅ Create, read, update, delete (CRUD) tasks
- ✅ Filter tasks by status (todo/doing/done)
- ✅ Status badge indicators with emojis
- ✅ Clean, interactive web UI
- ✅ Pytest test suite

## Tech Stack

- **Backend:** Flask 3.0.3
- **Frontend:** Vanilla HTML/CSS/JavaScript
- **Testing:** Pytest 8.3.2
- **Data:** In-memory (Python list)

## Quick Start

### 1. Setup Environment

```powershell
cd C:\Users\gayatri_mungarwadi\Documents\Capston\MyTestApp_CodeMie\taskboard_manager

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
taskboard_manager/
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
| GET | `/api/tasks` | List tasks (supports `status` filter) |
| POST | `/api/tasks` | Create task |
| PUT | `/api/tasks/<id>` | Update task |
| DELETE | `/api/tasks/<id>` | Delete task |

## Data Model

Each task has:
- `id` (integer)
- `title` (string, required)
- `status` (string: "todo", "doing", or "done")

## Example Payload

```json
{
  "title": "Review pull requests",
  "status": "doing"
}
```

## Status Badges

- 📋 **Todo** - Tasks not yet started
- 🔄 **Doing** - Tasks currently in progress
- ✅ **Done** - Completed tasks

## Next Steps

- Add due dates and priority levels
- Add user authentication
- Add task descriptions/notes
- Add time tracking
- Add recurring tasks
- Add task categories/projects
- Replace in-memory store with SQLite + SQLAlchemy

---

**Created:** August 2026  
**Running on:** Flask 3.0.3
