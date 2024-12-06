import React, { useState, useEffect } from "react";
import { Line } from "react-chartjs-2";
import { Pie } from "react-chartjs-2";
import { Bar } from "react-chartjs-2"; // Import the Bar chart for total value
import Chart from "chart.js/auto"; // Ensure to import chart.js

const AnalysisPage = () => {
  const [inventoryList, setInventoryList] = useState([]);

  useEffect(() => {
    // Fetch inventory data from the backend
    fetch("http://localhost:8081/api/products")
      .then((response) => response.json())
      .then((data) => setInventoryList(data))
      .catch((error) => console.error("Error fetching inventory data:", error));
  }, []);

  // Process the inventory data for the analysis
  const getChartData = () => {
    const productNames = inventoryList.map((item) => item.name);
    const quantities = inventoryList.map((item) => item.quantity);
    const prices = inventoryList.map((item) => item.price);
    const totalValues = quantities.map((quantity, index) => quantity * prices[index]); // Calculate total value for each product
    const averagePrice = prices.reduce((sum, price) => sum + price, 0) / prices.length; // Calculate average price

    return {
      productNames,
      quantities,
      prices,
      totalValues,
      averagePrice,
    };
  };

  const { productNames, quantities, prices, totalValues, averagePrice } = getChartData();

  // Line chart data for quantities
  const lineChartData = {
    labels: productNames,
    datasets: [
      {
        label: "Quantity of Products",
        data: quantities,
        fill: false,
        borderColor: "rgba(75,192,192,1)",
        tension: 0.1,
      },
    ],
  };

  // Pie chart data for product price distribution
  const pieChartData = {
    labels: productNames,
    datasets: [
      {
        label: "Price Distribution",
        data: prices,
        backgroundColor: [
          "rgba(255, 99, 132, 0.2)",
          "rgba(54, 162, 235, 0.2)",
          "rgba(255, 206, 86, 0.2)",
          "rgba(75, 192, 192, 0.2)",
          "rgba(153, 102, 255, 0.2)",
        ],
        borderColor: [
          "rgba(255, 99, 132, 1)",
          "rgba(54, 162, 235, 1)",
          "rgba(255, 206, 86, 1)",
          "rgba(75, 192, 192, 1)",
          "rgba(153, 102, 255, 1)",
        ],
        borderWidth: 1,
      },
    ],
  };

  // Bar chart data for total product value (quantity * price)
  const barChartData = {
    labels: productNames,
    datasets: [
      {
        label: "Total Value of Products",
        data: totalValues,
        backgroundColor: "rgba(75, 192, 192, 0.5)", // Light blue for the bars
        borderColor: "rgba(75, 192, 192, 1)",
        borderWidth: 1,
      },
    ],
  };

  // Line chart data for average product price
  const lineAveragePriceData = {
    labels: productNames,
    datasets: [
      {
        label: "Average Price",
        data: new Array(productNames.length).fill(averagePrice), // Set the same average price for all products
        fill: false,
        borderColor: "rgba(255, 159, 64, 1)", // Orange line for average price
        tension: 0.1,
      },
    ],
  };

  return (
    <div className="analysis-page">
      <h2>Product Analysis</h2>

      <div className="chart-container" style={{ display: "flex", flexWrap: "wrap", gap: "20px" }}>
        {/* Line chart for Quantity of Products */}
        <div className="chart" style={{ width: "48%", height: "300px" }}>
          <h3>Product Quantity Line Chart</h3>
          <Line data={lineChartData} options={{ responsive: true, maintainAspectRatio: true }} />
        </div>

        {/* Pie chart for Price Distribution */}
        <div className="chart" style={{ width: "48%", height: "300px" }}>
          <h3>Price Distribution Pie Chart</h3>
          <Pie data={pieChartData} options={{ responsive: true, maintainAspectRatio: true }} />
        </div>
      </div>

      {/* Add margin-top to create space between rows */}
      <div className="chart-container" style={{ display: "flex", marginTop: "40px" }}>
        {/* Bar chart for Total Value of Products */}
        <div className="chart" style={{ width: "48%", height: "200px" }}>
          <h3>Total Value of Products</h3>
          <Bar data={barChartData} options={{ responsive: true, maintainAspectRatio: true }} />
        </div>

        {/* Line chart for Average Product Price */}
        <div className="chart" style={{ width: "48%", height: "200px" }}>
          <h3>Average Product Price</h3>
          <Line data={lineAveragePriceData} options={{ responsive: true, maintainAspectRatio: true }} />
        </div>
      </div>
    </div>
  );
};

export default AnalysisPage;









