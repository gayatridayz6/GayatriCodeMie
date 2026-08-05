import sys
import os
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))

from app import create_app
from app import store


def setup_function():
    store.reset()


def test_health():
    app = create_app()
    client = app.test_client()
    r = client.get("/api/health")
    assert r.status_code == 200
    assert r.get_json()["status"] == "ok"


def test_create_and_list():
    app = create_app()
    client = app.test_client()

    c = client.post("/api/tasks", json={"title": "Task A", "status": "todo"})
    assert c.status_code == 201
    assert c.get_json()["status"] == "todo"

    l = client.get("/api/tasks")
    assert l.status_code == 200
    assert len(l.get_json()) == 1


def test_filter_by_status():
    app = create_app()
    client = app.test_client()

    client.post("/api/tasks", json={"title": "Task A", "status": "todo"})
    client.post("/api/tasks", json={"title": "Task B", "status": "done"})

    todo_res = client.get("/api/tasks?status=todo")
    assert todo_res.status_code == 200
    assert len(todo_res.get_json()) == 1

    done_res = client.get("/api/tasks?status=done")
    assert done_res.status_code == 200
    assert len(done_res.get_json()) == 1


def test_update():
    app = create_app()
    client = app.test_client()

    client.post("/api/tasks", json={"title": "Original", "status": "todo"})

    u = client.put("/api/tasks/1", json={"status": "doing", "title": "Updated"})
    assert u.status_code == 200
    assert u.get_json()["status"] == "doing"


def test_delete():
    app = create_app()
    client = app.test_client()

    client.post("/api/tasks", json={"title": "Temp Task", "status": "todo"})

    d = client.delete("/api/tasks/1")
    assert d.status_code == 200

    l = client.get("/api/tasks")
    assert l.status_code == 200
    assert len(l.get_json()) == 0
