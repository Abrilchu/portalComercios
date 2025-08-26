const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 5000;
const BACKEND_HOST = 'localhost';
const BACKEND_PORT = 8080;

const server = http.createServer((req, res) => {
  console.log(`${req.method} ${req.url}`);
  
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
  
  // Handle API proxy to backend (following best practices)
  if (req.url.startsWith('/api/')) {
    let body = '';
    
    req.on('data', chunk => {
      body += chunk.toString();
    });
    
    req.on('end', () => {
      // Redirect specific endpoints to working dev endpoints
      let apiPath = req.url;
      if (req.url === '/api/payments' || req.url.startsWith('/api/payments?')) {
        apiPath = req.url.replace('/api/payments', '/api/dev/payments');
      }
      if (req.url === '/api/dashboard/stats') {
        apiPath = '/api/dev/dashboard/stats';
      }
      
      const options = {
        hostname: BACKEND_HOST,
        port: BACKEND_PORT,
        path: apiPath,
        method: req.method,
        headers: {
          ...req.headers,
          'Host': `${BACKEND_HOST}:${BACKEND_PORT}`
        }
      };

      const proxyReq = http.request(options, (proxyRes) => {
        // Set CORS headers
        res.writeHead(proxyRes.statusCode, {
          ...proxyRes.headers,
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
          'Access-Control-Allow-Headers': 'Content-Type, Authorization'
        });
        
        proxyRes.pipe(res, { end: true });
      });

      proxyReq.on('error', async (err) => {
        console.error('Backend connection error:', err.message);
        console.log('Attempting direct database fallback...');
        
        // Fallback: Direct database access for development when backend is down
        try {
          const { Pool } = require('pg');
          const pool = new Pool({
            connectionString: process.env.DATABASE_URL
          });

          if (req.url === '/api/payments') {
            const query = `
              SELECT 
                p.id,
                p.payment_id,
                p.order_id,
                p.amount,
                p.method,
                p.status,
                p.created_at,
                p.updated_at,
                c.name as customer_name
              FROM payments p
              LEFT JOIN customers c ON p.customer_id = c.id
              ORDER BY p.created_at DESC
            `;
            
            const result = await pool.query(query);
            
            res.writeHead(200, {
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*'
            });
            
            res.end(JSON.stringify(result.rows));
            await pool.end();
            return;
          }

          if (req.url === '/api/dashboard/stats') {
            const queries = await Promise.all([
              pool.query("SELECT COUNT(*) as total, COALESCE(SUM(amount), 0) as total_amount FROM payments"),
              pool.query("SELECT COUNT(*) as count, COALESCE(SUM(amount), 0) as amount FROM payments WHERE status = 'APROBADA'"),
              pool.query("SELECT COUNT(*) as count, COALESCE(SUM(amount), 0) as amount FROM payments WHERE status = 'PEND_LIQ'"),
              pool.query("SELECT COUNT(*) as count, COALESCE(SUM(amount), 0) as amount FROM payments WHERE status = 'LIQUIDADA'")
            ]);

            const stats = {
              totalPayments: parseInt(queries[0].rows[0].total),
              totalAmount: parseFloat(queries[0].rows[0].total_amount),
              approvedPayments: parseInt(queries[1].rows[0].count),
              approvedAmount: parseFloat(queries[1].rows[0].amount),
              pendingPayments: parseInt(queries[2].rows[0].count),
              pendingAmount: parseFloat(queries[2].rows[0].amount),
              liquidatedPayments: parseInt(queries[3].rows[0].count),
              liquidatedAmount: parseFloat(queries[3].rows[0].amount)
            };

            res.writeHead(200, {
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*'
            });
            
            res.end(JSON.stringify(stats));
            await pool.end();
            return;
          }

          await pool.end();
        } catch (dbError) {
          console.error('Database fallback error:', dbError.message);
        }

        // If fallback fails, return error
        res.writeHead(502, { 
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        });
        res.end(JSON.stringify({ 
          error: 'Backend connection failed', 
          details: 'Backend server is not running on port 8080' 
        }));
      });

      if (body) {
        proxyReq.write(body);
      }
      
      proxyReq.end();
    });
    
    return;
  }
  
  // Serve the professional dashboard
  const filePath = path.join(__dirname, 'professional-index.html');
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
  console.log(`Frontend proxy server running on http://0.0.0.0:${PORT}`);
  console.log(`Proxying API calls to backend on ${BACKEND_HOST}:${BACKEND_PORT}`);
});