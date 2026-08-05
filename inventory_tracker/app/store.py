from datetime import datetime

ITEMS = []
NEXT_ID = 1


def now_iso():
    return datetime.utcnow().replace(microsecond=0).isoformat() + "Z"


def reset():
    global NEXT_ID
    ITEMS.clear()
    NEXT_ID = 1


def list_items(q="", low_stock=False):
    query = (q or "").strip().lower()
    data = ITEMS
    if low_stock:
        data = [i for i in data if i["quantity"] <= i["min_stock"]]
    if query:
        data = [i for i in data if query in i["name"].lower() or query in i["sku"].lower() or query in i["location"].lower()]
    return data


def validate(payload):
    errors = []
    if not isinstance(payload.get("name"), str) or not payload["name"].strip():
        errors.append("name is required")
    if not isinstance(payload.get("sku"), str) or not payload["sku"].strip():
        errors.append("sku is required")
    if not isinstance(payload.get("quantity"), int) or payload["quantity"] < 0:
        errors.append("quantity must be int >= 0")
    if not isinstance(payload.get("min_stock", 0), int) or payload["min_stock"] < 0:
        errors.append("min_stock must be int >= 0")
    if not isinstance(payload.get("location", ""), str):
        errors.append("location must be string")
    return errors


def find(item_id):
    for item in ITEMS:
        if item["id"] == item_id:
            return item
    return None


def create(payload):
    global NEXT_ID
    sku = payload["sku"].strip().upper()
    if any(i["sku"] == sku for i in ITEMS):
        return None, ["duplicate sku"]

    item = {
        "id": NEXT_ID,
        "name": payload["name"].strip(),
        "sku": sku,
        "quantity": payload["quantity"],
        "min_stock": payload.get("min_stock", 0),
        "location": payload.get("location", "").strip(),
        "updated_at": now_iso(),
    }
    ITEMS.append(item)
    NEXT_ID += 1
    return item, None


def update(item_id, payload):
    item = find(item_id)
    if not item:
        return None, ["not found"]

    sku = payload["sku"].strip().upper()
    if any(i["sku"] == sku and i["id"] != item_id for i in ITEMS):
        return None, ["duplicate sku"]

    item["name"] = payload["name"].strip()
    item["sku"] = sku
    item["quantity"] = payload["quantity"]
    item["min_stock"] = payload.get("min_stock", 0)
    item["location"] = payload.get("location", "").strip()
    item["updated_at"] = now_iso()
    return item, None


def remove(item_id):
    item = find(item_id)
    if not item:
        return False
    ITEMS.remove(item)
    return True
