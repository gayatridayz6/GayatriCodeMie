from flask import Blueprint, jsonify, request, render_template
from . import store

bp = Blueprint("inventory", __name__)


@bp.get("/")
def home():
    return render_template("index.html")


@bp.get("/api/health")
def health():
    return jsonify({"status": "ok"}), 200


@bp.get("/api/items")
def get_items():
    q = request.args.get("q", "")
    low = request.args.get("low_stock") == "true"
    return jsonify(store.list_items(q=q, low_stock=low)), 200


@bp.post("/api/items")
def create_item():
    payload = request.get_json(silent=True) or {}
    errors = store.validate(payload)
    if errors:
        return jsonify({"errors": errors}), 400
    item, errs = store.create(payload)
    if errs:
        return jsonify({"errors": errs}), 409
    return jsonify(item), 201


@bp.put("/api/items/<int:item_id>")
def update_item(item_id):
    payload = request.get_json(silent=True) or {}
    errors = store.validate(payload)
    if errors:
        return jsonify({"errors": errors}), 400
    item, errs = store.update(item_id, payload)
    if errs:
        code = 404 if "not found" in errs else 409
        return jsonify({"errors": errs}), code
    return jsonify(item), 200


@bp.delete("/api/items/<int:item_id>")
def delete_item(item_id):
    ok = store.remove(item_id)
    if not ok:
        return jsonify({"error": "not found"}), 404
    return jsonify({"deleted": True, "id": item_id}), 200
