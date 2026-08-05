from flask import Blueprint, jsonify, request, render_template
from . import store

bp = Blueprint("taskboard", __name__)


@bp.get("/")
def home():
    return render_template("index.html")


@bp.get("/api/health")
def health():
    return jsonify({"status": "ok"}), 200


@bp.get("/api/tasks")
def tasks():
    status = request.args.get("status")
    return jsonify(store.list_tasks(status=status)), 200


@bp.post("/api/tasks")
def create():
    payload = request.get_json(silent=True) or {}
    errors = store.validate(payload)
    if errors:
        return jsonify({"errors": errors}), 400
    return jsonify(store.create(payload)), 201


@bp.put("/api/tasks/<int:task_id>")
def update(task_id):
    payload = request.get_json(silent=True) or {}
    if "status" not in payload:
        return jsonify({"errors": ["status is required"]}), 400
    task, errs = store.update(task_id, payload)
    if errs:
        return jsonify({"errors": errs}), 404 if "not found" in errs else 400
    return jsonify(task), 200


@bp.delete("/api/tasks/<int:task_id>")
def delete(task_id):
    ok = store.remove(task_id)
    if not ok:
        return jsonify({"error": "not found"}), 404
    return jsonify({"deleted": True, "id": task_id}), 200
