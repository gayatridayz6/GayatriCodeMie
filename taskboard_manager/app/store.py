TASKS = []
NEXT_ID = 1


def reset():
    global NEXT_ID
    TASKS.clear()
    NEXT_ID = 1


def validate(payload):
    errors = []
    if not isinstance(payload.get("title"), str) or not payload["title"].strip():
        errors.append("title is required")
    if payload.get("status") not in ("todo", "doing", "done"):
        errors.append("status must be one of todo/doing/done")
    return errors


def list_tasks(status=None):
    if status in ("todo", "doing", "done"):
        return [t for t in TASKS if t["status"] == status]
    return TASKS


def create(payload):
    global NEXT_ID
    task = {"id": NEXT_ID, "title": payload["title"].strip(), "status": payload["status"]}
    TASKS.append(task)
    NEXT_ID += 1
    return task


def find(task_id):
    for task in TASKS:
        if task["id"] == task_id:
            return task
    return None


def update(task_id, payload):
    task = find(task_id)
    if not task:
        return None, ["not found"]

    if payload.get("status") not in ("todo", "doing", "done"):
        return None, ["status must be one of todo/doing/done"]

    task["title"] = payload["title"].strip() if "title" in payload else task["title"]
    task["status"] = payload["status"]
    return task, None


def remove(task_id):
    task = find(task_id)
    if not task:
        return False
    TASKS.remove(task)
    return True
