const $ = (id) => document.getElementById(id);

async function load() {
  const q = $("q").value.trim();
  const res = await fetch("/api/books?q=" + encodeURIComponent(q));
  const books = await res.json();

  if (!books.length) {
    $("table").innerHTML = "<p style='color: #6b7280;'>No books found.</p>";
    return;
  }

  $("table").innerHTML = `
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Title</th>
          <th>Author</th>
          <th>ISBN</th>
          <th>Copies</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        ${books.map(b => `
          <tr>
            <td>${b.id}</td>
            <td><strong>${b.title}</strong></td>
            <td>${b.author}</td>
            <td><code>${b.isbn}</code></td>
            <td>${b.copies}</td>
            <td>
              <button class="small-btn edit" onclick="editBook(${b.id})">Edit</button>
              <button class="small-btn delete" onclick="delBook(${b.id})">Delete</button>
            </td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  `;

  window._books = books;
}

function editBook(id) {
  const b = (window._books || []).find(x => x.id === id);
  if (!b) return;
  $("id").value = b.id;
  $("title").value = b.title;
  $("author").value = b.author;
  $("isbn").value = b.isbn;
  $("copies").value = b.copies;
  $("msg").textContent = `Editing book #${b.id}`;
}

async function delBook(id) {
  if (!confirm("Delete this book?")) return;
  const res = await fetch("/api/books/" + id, { method: "DELETE" });
  if (res.ok) {
    $("msg").textContent = "Book deleted.";
    $("msg").classList.remove("error");
    $("msg").classList.add("success");
    await load();
  }
}

$("f").addEventListener("submit", async (e) => {
  e.preventDefault();
  const payload = {
    title: $("title").value.trim(),
    author: $("author").value.trim(),
    isbn: $("isbn").value.trim(),
    copies: Number($("copies").value)
  };

  const id = $("id").value;
  const method = id ? "PUT" : "POST";
  const url = id ? `/api/books/${id}` : "/api/books";

  const res = await fetch(url, {
    method,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  const body = await res.json();

  if (res.ok) {
    $("msg").textContent = id ? "Book updated." : "Book added.";
    $("msg").classList.remove("error");
    $("msg").classList.add("success");
    $("f").reset();
    $("id").value = "";
    $("copies").value = "1";
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
  $("copies").value = "1";
  $("msg").textContent = "";
});

$("q").addEventListener("input", load);

load();
