import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api/inventory';

export const getInventoryItems = async () => {
  const response = await axios.get(`${API_BASE_URL}/items`);
  return response.data; // Assuming the backend returns the list in `response.data`
};

export const addInventoryItem = async (item) => {
  const response = await axios.post(`${API_BASE_URL}/items`, item);
  return response.data; // Assuming the added item is returned in `response.data`
};

export const deleteInventoryItem = async (id) => {
  await axios.delete(`${API_BASE_URL}/items/${id}`);
};







