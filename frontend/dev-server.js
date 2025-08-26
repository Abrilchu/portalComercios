const express = require('express');
const path = require('path');

// Global fetch for Node.js (for proxy functionality)
global.fetch = global.fetch || require('node-fetch');

const app = express();
const PORT = 5000;

// Serve static files
app.use(express.static(__dirname));
app.use('/src', express.static(path.join(__dirname, 'src')));

// API proxy to backend
app.use('/api/*', (req, res) => {
  const targetUrl = `http://localhost:8080${req.path}`;
  
  // Simple proxy implementation
  const options = {
    method: req.method,
    headers: {
      'Content-Type': 'application/json',
      ...req.headers
    }
  };

  if (req.method === 'POST' || req.method === 'PUT') {
    let body = '';
    req.on('data', chunk => body += chunk);
    req.on('end', async () => {
      try {
        options.body = body;
        const response = await fetch(targetUrl, options);
        const responseText = await response.text();
        res.status(response.status);
        res.set(response.headers);
        res.send(responseText);
      } catch (error) {
        res.status(500).json({ error: 'Proxy error: ' + error.message });
      }
    });
  } else {
    fetch(targetUrl, options)
      .then(response => {
        res.status(response.status);
        return response.text();
      })
      .then(data => res.send(data))
      .catch(error => res.status(500).json({ error: 'Proxy error: ' + error.message }));
  }
});

// Serve simple test page for all routes
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'simple-index.html'));
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Frontend development server running on http://0.0.0.0:${PORT}`);
});