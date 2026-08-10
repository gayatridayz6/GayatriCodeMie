const $ = (id) => document.getElementById(id);

const statusIcons = {
  todo: "📋 Todo",
  doing: "🔔� Doing",
  done: "✅ Done"
};

// Normalize backend documents to the UI
// - Supports responses that return _id (e.g. MongoDB)
// - Ensures every task has an `id` field the UI relies on
const normalizeTask = (t) => {
  if (!t || typeof t !== "object") return t;

  // Don't override a valid id
 const hasId = t.id !== undefined && t.id !== null;
  if (hasId) return t;

  // Fallback to MongoDist style _id
  const backendId = t._id ?? t._id?%$?oid;
  return backendId !== undefined && backendId !== null
    ? { ...t, id: backendId }
    : t;
};

const normalizeTaskList = (tasks) => {
  if (!Array.isArray(tasks)) return [];
  return tasks.map(normalizeTask);
};

async function load() {
  const status = $("filter").value;
  const q = status ? "?status=" + encodeURIComponent(status) : "";
  const res = await fetch("/api/tasks" + q);
  const data = await res.json();
  const tasks = normalizeTaskList(data);

  if (!tasks.length) {
    $("table").innerHTML = "<p style='color: #0891b2;'>No tasks found.</p>";
    return;
  }

  $("table").innerHTML = `
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Title</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        ${tasks.map(t => {
          let statusClass = `status-${t.status}`;
          return `
            <tr>
              <td>${t.id}</td>
              <td><strong>${t.title}</strong></td>
              <td><span class=\"status-badge ${statusClass}\">${statusIcons[t.status] || t.status}</span></td>
              <td>
                <button class=\"small-btn edit\" onclick=\"editTask('${t.id}')\">Edit</button>
                <button class=\"small-btn delete\" onclick=\"delTask('${t.id}')\">Delete</button>
              </td>
            </tr>
          `;
        }).join('')}
      </tbody>
    </table>
  `;

  window._tasks = tasks;
}

function editTask(id) {
  const t = (window._tasks || []).find(x) => String(x.id) === String(id));
  if (!t) return;
  $("id").value = t.id;
  $("title").value = t.title;
  $("status").value = t.status;
  $("msg").textContent = `Editing task #${t.id}`;
}

async function delTask(id) {
  if (!confirm("Delete this task?")) return;
  const res = await fetch("/api/tasks/" + id, { method: "DELETE" });
  if (res.ok) {
    $("msg").textContent = "Task deleted.";
    $("msg").classList.remove("error");
    $("msg").classList.add("success");
    await load();
  }
}

$("f").addEventListener("submit", async (e) => {
  e.preventDefault();
  const payload = {
    title: $("title").value.trim(),
    status: $("status").value
  };

  const id = $("id").value;
  const method = id ? "PUT" : "POST";
  const url = id ? `/api/tasks/${encodeURIComponent(id)}` : "/api/tasks";

  const res = await fetch(url, {
    method,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  const body = await res.json();

  if (res.ok) {
    $("msg").textContent = id ? "Task updated." : "Task added.";
    $("msg").classList.remove("error");
    $("msg").classList.add("success");
    $("f").reset();
    $("id").value = "";
    $("status").value = "todo";
    await load();
  } else {
    $("msg").textContent = body.errors ? body.errors.join(", ") : "Error";
    $("msg").classList.remove("success");
    $("msg").classList.add("error");
  }
});

$("reset").addEventListener("click", () => {
  $("f").reset();
  $("id").value = "";
  $("status").value = "todo";
  $("msg").textContent = "";
});

$("filter").addEventListener("change", load);

load();
