import { useCallback, useEffect, useRef, useState } from 'react';
import { fetchAccommodations, fetchSupportedCurrencies } from './services/api';

const SUPPORTED_FALLBACK_CODES = ['USD', 'EUR', 'GBP'];
const STORAGE_KEY = 'preferredAccommodationCurrency';

function readInitialCurrency() {
  const params = new URLSearchParams(window.location.search);
  const urlCurrency = params.get('currency')?.toUpperCase();
  const storedCurrency = sessionStorage.getItem(STORAGE_KEY)?.toUpperCase();

  if (SUPPORTED_FALLBACK_CODES.includes(urlCurrency)) {
    return urlCurrency;
  }
  if (SUPPORTED_FALLBACK_CODES.includes(storedCurrency)) {
    return storedCurrency;
  }
  return 'USD';
}

function isCanceled(error) {
  return error.name === 'CanceledError' || error.code === 'ERR_CANCELED';
}

function App() {
  const initialCurrency = readInitialCurrency();
  const [currencies, setCurrencies] = useState([]);
  const [selectedCurrency, setSelectedCurrency] = useState(initialCurrency);
  const [previousCurrency, setPreviousCurrency] = useState(initialCurrency);
  const [results, setResults] = useState([]);
  const [appliedCurrency, setAppliedCurrency] = useState(null);
  const [banner, setBanner] = useState('');
  const [loadingCurrencies, setLoadingCurrencies] = useState(true);
  const [loadingResults, setLoadingResults] = useState(false);
  const requestRef = useRef(null);

  useEffect(() => {
    const controller = new AbortController();

    fetchSupportedCurrencies(controller.signal)
      .then((response) => {
        setCurrencies(response.data.data);
        setBanner('');
      })
      .catch((error) => {
        if (isCanceled(error)) {
          return;
        }
        setBanner('Currency information is temporarily unavailable. Prices kept in ' + previousCurrency + '.');
      })
      .finally(() => setLoadingCurrencies(false));

    return () => controller.abort();
  }, [previousCurrency]);

  const updateUrlAndStorage = useCallback((currency) => {
    const params = new URLSearchParams(window.location.search);
    params.set('currency', currency);
    window.history.replaceState({}, '', window.location.pathname + '?' + params.toString());
    sessionStorage.setItem(STORAGE_KEY, currency);
  }, []);

  useEffect(() => {
    if (requestRef.current) {
      requestRef.current.abort();
    }

    const controller = new AbortController();
    requestRef.current = controller;
    setLoadingResults(true);

    fetchAccommodations(selectedCurrency, controller.signal)
      .then((response) => {
        setResults(response.data.data);
        setAppliedCurrency(response.data.meta.appliedCurrency);
        setPreviousCurrency(response.data.meta.appliedCurrency.code);
        updateUrlAndStorage(response.data.meta.appliedCurrency.code);
        setBanner('');
      })
      .catch((error) => {
        if (isCanceled(error)) {
          return;
        }
        const message = error.response?.data?.error?.message || 'Currency information is temporarily unavailable.';
        setSelectedCurrency(previousCurrency);
        setBanner(message + ' Prices kept in ' + previousCurrency + '.');
      })
      .finally(() => setLoadingResults(false));

    return () => controller.abort();
  }, [previousCurrency, selectedCurrency, updateUrlAndStorage]);

  const onCurrencyChange = (event) => {
    const currency = event.target.value;
    if (!SUPPORTED_FALLBACK_CODES.includes(currency)) {
      setBanner('Supported currencies are USD, EUR, GBP.');
      return;
    }
    setSelectedCurrency(currency);
  };

  const activeLabel = appliedCurrency?.label || currencies.find((currency) => currency.code === selectedCurrency)?.label || '';

  return (
    <main className="page-shell">
      <header className="hero">
        <div>
          <p className="eyebrow">Stays - Search Results</p>
          <h1>Find accommodations in your preferred currency</h1>
        </div>
        <div className="search-row" aria-label="Search criteria">
          <span>Destination <input aria-label="Destination" placeholder="City" /></span>
          <span>Dates <input aria-label="Dates" placeholder="Check-in - Check-out" /></span>
          <span>Guests <input aria-label="Guests" placeholder="2" /></span>
          <span>Sort <select aria-label="Sort"><option>Best</option></select></span>
        </div>
      </header>

      <section className="filters" aria-label="Advanced Filters">
        <h2>Advanced Filters</h2>
        <label className="currency-selector">
          Currency:
          <select value={selectedCurrency} onChange={onCurrencyChange} disabled={loadingCurrencies || currencies.length === 0}>
            {currencies.map((currency) => (
              <option key={currency.code} value={currency.code}>{currency.label}</option>
            ))}
          </select>
        </label>
        <div className="filter-grid">
          <label><input type="checkbox" /> Free Wi-Fi</label>
          <label><input type="checkbox" /> Breakfast Included</label>
          <label><input type="radio" name="property" /> Hotel</label>
          <label><input type="radio" name="property" /> Villa</label>
          <label><input type="radio" name="property" defaultChecked /> Any</label>
        </div>
        <div className="actions">
          <button type="button">Apply</button>
          <button type="button" className="ghost">Clear all</button>
        </div>
      </section>

      {banner && <div className="banner" role="alert">[!] {banner}</div>}

      <section className="results" aria-busy={loadingResults}>
        <div className="results-heading">
          <h2>Results</h2>
          {activeLabel && <span className="currency-pill">{activeLabel}</span>}
        </div>
        <div className="cards">
          {results.map((accommodation) => (
            <article className="card" key={accommodation.id}>
              <h3>{accommodation.name}</h3>
              <p className="rating">★★★★☆ ({accommodation.rating})</p>
              <p>Nightly: <strong>{Number(accommodation.price.nightly.amount).toFixed(2)} {accommodation.price.nightly.currencyCode}</strong></p>
              <p>Total: <strong>{Number(accommodation.price.total.amount).toFixed(2)} {accommodation.price.total.currencyCode}</strong></p>
              <p className="label">Label: {activeLabel}</p>
              <button type="button">View details</button>
            </article>
          ))}
        </div>
      </section>
    </main>
  );
}

export default App;
