const http = require('http');
const path = require('path');
const fs = require('fs');
const config = require('./microservice-config');

// Create HTTP server for frontend microservice
const server = http.createServer((req, res) => {
  console.log(`[${new Date().toISOString()}] ${req.method} ${req.url}`);
  
  // Handle CORS preflight requests
  if (req.method === 'OPTIONS') {
    res.writeHead(200, {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
      'Access-Control-Allow-Headers': 'Content-Type, Authorization, Accept',
      'Access-Control-Max-Age': '86400'
    });
    res.end();
    return;
  }
  
  // Health check endpoint
  if (req.url === '/health') {
    res.writeHead(200, { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*'
    });
    res.end(JSON.stringify({
      service: config.service.name,
      version: config.service.version,
      status: 'healthy',
      timestamp: new Date().toISOString(),
      uptime: process.uptime()
    }));
    return;
  }
  
  // API proxy to backend microservice
  if (req.url.startsWith('/api/') || req.url.startsWith('/auth/')) {
    let body = '';
    
    req.on('data', chunk => {
      body += chunk.toString();
    });
    
    req.on('end', () => {
      const backendUrl = new URL(config.backend.baseUrl);
      const options = {
        hostname: backendUrl.hostname,
        port: backendUrl.port || 8080,
        path: req.url,
        method: req.method,
        headers: {
          ...req.headers,
          'Host': `${backendUrl.hostname}:${backendUrl.port || 8080}`
        }
      };

      const proxyReq = http.request(options, (proxyRes) => {
        // Set CORS headers
        res.writeHead(proxyRes.statusCode, {
          ...proxyRes.headers,
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
          'Access-Control-Allow-Headers': 'Content-Type, Authorization',
          'X-Frontend-Service': config.service.name,
          'X-Frontend-Version': config.service.version
        });
        
        proxyRes.pipe(res, { end: true });
      });

      proxyReq.on('error', (err) => {
        console.error(`Backend proxy error: ${err.message}`);
        res.writeHead(502, { 
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        });
        res.end(JSON.stringify({
          error: 'Backend Service Unavailable',
          message: 'The backend microservice is currently unavailable',
          service: config.service.name,
          timestamp: new Date().toISOString()
        }));
      });

      if (body) {
        proxyReq.write(body);
      }
      
      proxyReq.end();
    });
    
    return;
  }
  
  // Serve the frontend application
  const filePath = path.join(__dirname, 'professional-index.html');
  fs.readFile(filePath, 'utf8', (err, data) => {
    if (err) {
      console.error(`Error reading frontend file: ${err.message}`);
      res.writeHead(500, { 
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*' 
      });
      res.end(JSON.stringify({
        error: 'Frontend Service Error',
        message: 'Unable to serve frontend application'
      }));
      return;
    }
    
    // Inject microservice configuration into the HTML
    const injectedData = data.replace(
      '<!-- MICROSERVICE_CONFIG -->',
      `<script>
        window.MICROSERVICE_CONFIG = {
          frontend: {
            service: '${config.service.name}',
            version: '${config.service.version}'
          },
          backend: {
            baseUrl: '${config.backend.baseUrl}',
            endpoints: ${JSON.stringify(config.backend.endpoints)}
          }
        };
      </script>`
    );
    
    res.writeHead(200, { 
      'Content-Type': 'text/html',
      'X-Frontend-Service': config.service.name,
      'X-Frontend-Version': config.service.version,
      'Access-Control-Allow-Origin': '*'
    });
    res.end(injectedData);
  });
});

// Start the frontend microservice
server.listen(config.server.port, config.server.host, () => {
  console.log(`\n🚀 Frontend Microservice Started`);
  console.log(`   Service: ${config.service.name} v${config.service.version}`);
  console.log(`   URL: http://${config.server.host}:${config.server.port}`);
  console.log(`   Backend: ${config.backend.baseUrl}`);
  console.log(`   Health: http://${config.server.host}:${config.server.port}/health`);
  console.log(`   Status: Ready for connections\n`);
});

// Graceful shutdown
process.on('SIGTERM', () => {
  console.log('Frontend microservice shutting down gracefully...');
  process.exit(0);
});

process.on('SIGINT', () => {
  console.log('Frontend microservice shutting down gracefully...');
  process.exit(0);
});