const $ = (id) => document.getElementById(id);

async function load() {
  const params = new URLSearchParams();
  if ($("q").value.trim()) params.set("q", $("q").value.trim());
  if ($("low").checked) params.set("low_stock", "true");

  const res = await fetch("/api/items?" + params.toString());
  const items = await res.json();

  if (!items.length) {
    $("table").innerHTML = "<p style='color: #6b7280;'>No items found.</p>";
    return;
  }

  $("table").innerHTML = `
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>SKU</th>
          <th>Qty</th>
          <th>Min</th>
          <th>Location</th>
          <th>Updated</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        ${items.map(i => `
          <tr>
            <td>${i.id}</td>
            <td>${i.name}</td>
            <td><strong>${i.sku}</strong></td>
            <td class="${i.quantity <= i.min_stock ? 'low-stock' : ''}">${i.quantity}</td>
            <td>${i.min_stock}</td>
            <td>${i.location || '-'}</td>
            <td>${i.updated_at}</td>
            <td>
              <button class="small-btn edit" onclick="editItem(${i.id})">Edit</button>
              <button class="small-btn delete" onclick="delItem(${i.id})">Delete</button>
            </td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  `;

  window._items = items;
}

function editItem(id) {
  const i = (window._items || []).find(x => x.id === id);
  if (!i) return;
  $("id").value = i.id;
  $("name").value = i.name;
  $("sku").value = i.sku;
  $("quantity").value = i.quantity;
  $("min_stock").value = i.min_stock;
  $("location").value = i.location;
  $("msg").textContent = `Editing item #${i.id}`;
}

async function delItem(id) {
  if (!confirm("Delete this item?")) return;
  const res = await fetch("/api/items/" + id, { method: "DELETE" });
  if (res.ok) {
    $("msg").textContent = "Item deleted.";
    $("msg").classList.remove("error");
    $("msg").classList.add("success");
    await load();
  }
}

$("f").addEventListener("submit", async (e) => {
  e.preventDefault();
  const payload = {
    name: $("name").value.trim(),
    sku: $("sku").value.trim(),
    quantity: Number($("quantity").value),
    min_stock: Number($("min_stock").value),
    location: $("location").value.trim()
  };

  const id = $("id").value;
  const method = id ? "PUT" : "POST";
  const url = id ? `/api/items/${id}` : "/api/items";

  const res = await fetch(url, {
    method,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  const body = await res.json();

  if (res.ok) {
    $("msg").textContent = id ? "Item updated." : "Item created.";
    $("msg").classList.remove("error");
    $("msg").classList.add("success");
    $("f").reset();
    $("id").value = "";
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
  $("msg").textContent = "";
});

$("q").addEventListener("input", load);
$("low").addEventListener("change", load);

load();
