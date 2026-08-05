from flask import Blueprint, jsonify, request, render_template
from . import store

bp = Blueprint("library", __name__)


@bp.get("/")
def home():
    return render_template("index.html")


@bp.get("/api/health")
def health():
    return jsonify({"status": "ok"}), 200


@bp.get("/api/books")
def books():
    q = request.args.get("q", "")
    return jsonify(store.list_books(q=q)), 200


@bp.post("/api/books")
def create():
    payload = request.get_json(silent=True) or {}
    errors = store.validate(payload)
    if errors:
        return jsonify({"errors": errors}), 400
    book, errs = store.create(payload)
    if errs:
        return jsonify({"errors": errs}), 409
    return jsonify(book), 201


@bp.put("/api/books/<int:book_id>")
def update(book_id):
    payload = request.get_json(silent=True) or {}
    errors = store.validate(payload)
    if errors:
        return jsonify({"errors": errors}), 400
    book, errs = store.update(book_id, payload)
    if errs:
        code = 404 if "not found" in errs else 409
        return jsonify({"errors": errs}), code
    return jsonify(book), 200


@bp.delete("/api/books/<int:book_id>")
def delete(book_id):
    ok = store.remove(book_id)
    if not ok:
        return jsonify({"error": "not found"}), 404
    return jsonify({"deleted": True, "id": book_id}), 200
