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


def test_create_list():
    app = create_app()
    client = app.test_client()

    c = client.post("/api/items", json={
        "name": "Laptop", "sku": "INV-1", "quantity": 5, "min_stock": 1, "location": "Rack A"
    })
    assert c.status_code == 201
    assert c.get_json()["sku"] == "INV-1"

    l = client.get("/api/items")
    assert l.status_code == 200
    assert len(l.get_json()) == 1


def test_update():
    app = create_app()
    client = app.test_client()

    client.post("/api/items", json={
        "name": "Mouse", "sku": "INV-2", "quantity": 10, "min_stock": 2, "location": "Shelf B"
    })

    u = client.put("/api/items/1", json={
        "name": "Wireless Mouse", "sku": "INV-2", "quantity": 8, "min_stock": 2, "location": "Shelf B"
    })
    assert u.status_code == 200
    assert u.get_json()["name"] == "Wireless Mouse"


def test_delete():
    app = create_app()
    client = app.test_client()

    client.post("/api/items", json={
        "name": "Monitor", "sku": "INV-3", "quantity": 3, "min_stock": 1, "location": "Shelf C"
    })

    d = client.delete("/api/items/1")
    assert d.status_code == 200

    l = client.get("/api/items")
    assert l.status_code == 200
    assert len(l.get_json()) == 0


def test_low_stock_filter():
    app = create_app()
    client = app.test_client()

    client.post("/api/items", json={
        "name": "Item A", "sku": "A1", "quantity": 2, "min_stock": 3, "location": "Loc A"
    })
    client.post("/api/items", json={
        "name": "Item B", "sku": "B1", "quantity": 10, "min_stock": 2, "location": "Loc B"
    })

    res = client.get("/api/items?low_stock=true")
    assert res.status_code == 200
    items = res.get_json()
    assert len(items) == 1
    assert items[0]["sku"] == "A1"


def test_search():
    app = create_app()
    client = app.test_client()

    client.post("/api/items", json={
        "name": "Laptop", "sku": "TECH-001", "quantity": 5, "min_stock": 1, "location": "Tech Shelf"
    })
    client.post("/api/items", json={
        "name": "Desk", "sku": "FURN-001", "quantity": 3, "min_stock": 1, "location": "Office"
    })

    res = client.get("/api/items?q=Laptop")
    assert res.status_code == 200
    items = res.get_json()
    assert len(items) == 1
    assert items[0]["name"] == "Laptop"
