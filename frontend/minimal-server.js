const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 5000;

const server = http.createServer((req, res) => {
  console.log(`${req.method} ${req.url}`);
  
  // Handle API proxy
  if (req.url.startsWith('/api/')) {
    // Simple proxy response for testing
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ message: 'API proxy working, connect to backend at localhost:8080' }));
    return;
  }
  
  // Serve the test page
  const filePath = path.join(__dirname, 'simple-index.html');
  fs.readFile(filePath, (err, data) => {
    if (err) {
      res.writeHead(404);
      res.end('404 - File not found');
      return;
    }
    res.writeHead(200, { 'Content-Type': 'text/html' });
    res.end(data);
  });
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Frontend development server running on http://0.0.0.0:${PORT}`);
});