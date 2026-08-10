# Accommodation Currency UI

React UI for EPMCDMETST-58713. It renders the accommodation search results screen with an advanced Currency selector and price labels sourced from backend currency metadata.

## Behavior

- Loads supported currency options from `GET /api/v1/currencies?codes=USD,EUR,GBP`.
- Persists the preferred currency in `sessionStorage` and the `currency` URL query parameter.
- Refetches accommodation results from `GET /api/v1/accommodations?currency=<code>` when the user changes currency.
- Aborts stale in-flight accommodation requests to avoid mixed currency rendering.
- Displays a non-blocking banner and keeps the previous currency when metadata or conversion calls fail.

## Scripts

```powershell
npm install
npm start
npm run build
npm test
```

The development server proxies API calls to `http://localhost:8080`.
