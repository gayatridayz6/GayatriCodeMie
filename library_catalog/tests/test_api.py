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

    c = client.post("/api/books", json={
        "title": "Clean Code",
        "author": "Robert C. Martin",
        "isbn": "9780132350884",
        "copies": 2
    })
    assert c.status_code == 201
    assert c.get_json()["isbn"] == "9780132350884"

    l = client.get("/api/books")
    assert l.status_code == 200
    assert len(l.get_json()) == 1


def test_search():
    app = create_app()
    client = app.test_client()

    client.post("/api/books", json={
        "title": "Clean Code", "author": "Robert C. Martin", "isbn": "ISBN-1", "copies": 1
    })
    client.post("/api/books", json={
        "title": "Design Patterns", "author": "Gang of Four", "isbn": "ISBN-2", "copies": 1
    })

    res = client.get("/api/books?q=Clean")
    assert res.status_code == 200
    assert len(res.get_json()) == 1


def test_update():
    app = create_app()
    client = app.test_client()

    client.post("/api/books", json={
        "title": "Python 101",
        "author": "John Doe",
        "isbn": "ISBN-3",
        "copies": 1
    })

    u = client.put("/api/books/1", json={
        "title": "Python 201",
        "author": "John Doe",
        "isbn": "ISBN-3",
        "copies": 3
    })
    assert u.status_code == 200
    assert u.get_json()["title"] == "Python 201"


def test_delete():
    app = create_app()
    client = app.test_client()

    client.post("/api/books", json={
        "title": "Book A", "author": "Author A", "isbn": "ISBN-4", "copies": 1
    })

    d = client.delete("/api/books/1")
    assert d.status_code == 200

    l = client.get("/api/books")
    assert l.status_code == 200
    assert len(l.get_json()) == 0
