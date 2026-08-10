import axios from 'axios';

const api = axios.create({
  baseURL: '',
  headers: {
    Accept: 'application/json'
  }
});

export const fetchSupportedCurrencies = (signal) =>
  api.get('/api/v1/currencies', {
    params: { codes: 'USD,EUR,GBP' },
    signal
  });

export const fetchAccommodations = (currency, signal) =>
  api.get('/api/v1/accommodations', {
    params: { currency },
    signal
  });

export default api;
