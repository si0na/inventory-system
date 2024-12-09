import React from 'react';
import ReactDOM from 'react-dom/client'; // Import from 'react-dom/client'
import { BrowserRouter as Router } from 'react-router-dom'; // Import Router
import App from './App'; // Your App component

// Create a root and render the app
const root = ReactDOM.createRoot(document.getElementById('root')); // Using createRoot in React 18+
root.render(
  <Router>
    <App />
  </Router>
);

