import React, { useState, useEffect } from "react";
import "./App.css";
import logo from './logo.png';
import AnalysisPage from "./AnalysisPage.js"; // Import the Analysis Page
import { Route, Routes, Link } from "react-router-dom"; // Import Route and Link

function App() {
  const [inventoryList, setInventoryList] = useState([]);
  const [newProduct, setNewProduct] = useState({
    name: "",
    quantity: "",
    price: "",
  });
  const [editProductId, setEditProductId] = useState(null);

  // Fetch inventory items
  const getInventoryItems = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/products");
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const data = await response.json();
      setInventoryList(data);
    } catch (error) {
      console.error("Error fetching inventory items:", error);
    }
  };

  // Add or Edit a product
  const handleProductSubmit = async (e) => {
    e.preventDefault();
    const method = editProductId ? "PUT" : "POST";
    const url = editProductId
      ? `http://localhost:8080/api/products/${editProductId}`
      : "http://localhost:8080/api/products";

    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newProduct),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      setNewProduct({ name: "", quantity: "", price: "" });
      setEditProductId(null); // Clear edit state after submitting
      getInventoryItems(); // Refresh the list after adding or editing
    } catch (error) {
      console.error("Error saving inventory item:", error);
    }
  };

  // Delete a product
  const deleteInventoryItem = async (id) => {
    try {
      const response = await fetch(`http://localhost:8080/api/products/${id}`, {
        method: "DELETE",
      });
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      getInventoryItems(); // Refresh the list after deletion
    } catch (error) {
      console.error("Error deleting inventory item:", error);
    }
  };

  // Set form values when editing an item
  const handleEditClick = (item) => {
    setNewProduct({
      name: item.name,
      quantity: item.quantity,
      price: item.price,
    });
    setEditProductId(item.id); // Set the product to be edited
  };

  // Fetch inventory items on component mount
  useEffect(() => {
    getInventoryItems();
  }, []);

  return (
    <div className="App">
      <head>
        <title>Inventory Dashboard</title>
      </head>
      <aside className="sidebar">
        <img src={logo} alt="Logo" className="logo" />
        <h2>DASHBOARD</h2>
        <ul>
        <li>
            {/* Link to Google Search for Inventory Management Products */}
            <a href="https://des.mizoram.gov.in/uploads/attachments/67796fee7f93c58969f4a7fdcc7cdd8e/pages-153-retail-prices-of-essential-commodities-.pdf" target="_blank" rel="noopener noreferrer">
              PRICING
            </a>
          </li>
          {/* Link to Products Page */}
          <li>
            <Link to="/">PRODUCTS</Link>
          </li>
          <li>
            <Link to="/analysis">ANALYSIS</Link>
          </li>
        </ul>
      </aside>
      <main className="content">
        <Routes>
          <Route
            path="/"
            element={
              <div>
                <header className="header">
                  <center>
                  <h1>Inventory Management System</h1>
                  </center>
                </header>
                <section className="add-product">
                  <h2>{editProductId ? "Edit Product" : "Add Product"}</h2>
                  <form onSubmit={handleProductSubmit}>
                    <input
                      type="text"
                      placeholder="Name"
                      value={newProduct.name}
                      onChange={(e) =>
                        setNewProduct({ ...newProduct, name: e.target.value })
                      }
                      required
                    />
                    <input
                      type="number"
                      placeholder="Quantity"
                      value={newProduct.quantity}
                      onChange={(e) =>
                        setNewProduct({ ...newProduct, quantity: e.target.value })
                      }
                      required
                    />
                    <input
                      type="number"
                      placeholder="Price"
                      value={newProduct.price}
                      onChange={(e) =>
                        setNewProduct({ ...newProduct, price: e.target.value })
                      }
                      required
                    />
                    <button type="submit">{editProductId ? "Save" : "Add"}</button>
                  </form>
                </section>
                <section className="inventory-list">
                  <h2>Inventory List</h2>
                  {inventoryList.length > 0 ? (
                    inventoryList.map((item) => (
                      <div key={item.id} className="inventory-item">
                        <h3>{item.name}</h3>
                        <p>Quantity: {item.quantity}</p>
                        <p>Price: ${item.price}</p>
                        <button onClick={() => handleEditClick(item)}>Edit</button>
                        <button onClick={() => deleteInventoryItem(item.id)}>
                          Delete
                        </button>
                      </div>
                    ))
                  ) : (
                    <p>No products available.</p>
                  )}
                </section>
              </div>
            }
          />
          <Route path="/analysis" element={<AnalysisPage inventory={inventoryList} />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;






