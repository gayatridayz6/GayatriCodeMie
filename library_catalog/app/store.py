BOOKS = []
NEXT_ID = 1


def reset():
    global NEXT_ID
    BOOKS.clear()
    NEXT_ID = 1


def list_books(q=""):
    query = (q or "").strip().lower()
    if not query:
        return BOOKS
    return [b for b in BOOKS if query in b["title"].lower() or query in b["author"].lower() or query in b["isbn"].lower()]


def validate(payload):
    errors = []
    if not isinstance(payload.get("title"), str) or not payload["title"].strip():
        errors.append("title is required")
    if not isinstance(payload.get("author"), str) or not payload["author"].strip():
        errors.append("author is required")
    if not isinstance(payload.get("isbn"), str) or not payload["isbn"].strip():
        errors.append("isbn is required")
    if not isinstance(payload.get("copies", 1), int) or payload["copies"] < 0:
        errors.append("copies must be int >= 0")
    return errors


def find(book_id):
    for book in BOOKS:
        if book["id"] == book_id:
            return book
    return None


def create(payload):
    global NEXT_ID
    isbn = payload["isbn"].strip()
    if any(b["isbn"] == isbn for b in BOOKS):
        return None, ["duplicate isbn"]
    book = {
        "id": NEXT_ID,
        "title": payload["title"].strip(),
        "author": payload["author"].strip(),
        "isbn": isbn,
        "copies": payload.get("copies", 1),
    }
    BOOKS.append(book)
    NEXT_ID += 1
    return book, None


def update(book_id, payload):
    book = find(book_id)
    if not book:
        return None, ["not found"]

    isbn = payload["isbn"].strip()
    if any(b["isbn"] == isbn and b["id"] != book_id for b in BOOKS):
        return None, ["duplicate isbn"]

    book["title"] = payload["title"].strip()
    book["author"] = payload["author"].strip()
    book["isbn"] = isbn
    book["copies"] = payload.get("copies", 1)
    return book, None


def remove(book_id):
    book = find(book_id)
    if not book:
        return False
    BOOKS.remove(book)
    return True
