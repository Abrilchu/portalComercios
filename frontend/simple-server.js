const http = require('http');
const fs = require('fs');
const path = require('path');
const { Pool } = require('pg');

const PORT = 5000;

// PostgreSQL connection using environment variables
const pool = new Pool({
  connectionString: process.env.DATABASE_URL
});

const server = http.createServer(async (req, res) => {
  console.log(`${req.method} ${req.url}`);
  
  // Handle CORS
  const corsHeaders = {
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
    'Access-Control-Allow-Headers': 'Content-Type, Authorization, Accept',
    'Access-Control-Max-Age': '86400'
  };

  if (req.method === 'OPTIONS') {
    res.writeHead(200, corsHeaders);
    res.end();
    return;
  }

  // API Routes for real data
  if (req.url === '/api/payments') {
    try {
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
          c.name as customer_name,
          b.name as branch_name
        FROM payments p
        LEFT JOIN customers c ON p.customer_id = c.id
        LEFT JOIN branches b ON p.branch_id = b.id
        ORDER BY p.created_at DESC
      `;
      
      const result = await pool.query(query);
      
      res.writeHead(200, {
        'Content-Type': 'application/json',
        ...corsHeaders
      });
      
      res.end(JSON.stringify(result.rows));
    } catch (error) {
      console.error('Database error:', error);
      res.writeHead(500, {
        'Content-Type': 'application/json',
        ...corsHeaders
      });
      res.end(JSON.stringify({ error: 'Database error', details: error.message }));
    }
    return;
  }

  if (req.url === '/api/dashboard/stats') {
    try {
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
        ...corsHeaders
      });
      
      res.end(JSON.stringify(stats));
    } catch (error) {
      console.error('Database error:', error);
      res.writeHead(500, {
        'Content-Type': 'application/json',
        ...corsHeaders
      });
      res.end(JSON.stringify({ error: 'Database error', details: error.message }));
    }
    return;
  }

  if (req.url === '/api/customers') {
    try {
      const query = `
        SELECT 
          id,
          name,
          phone,
          email,
          customer_id,
          created_at
        FROM customers
        ORDER BY created_at DESC
      `;
      
      const result = await pool.query(query);
      
      res.writeHead(200, {
        'Content-Type': 'application/json',
        ...corsHeaders
      });
      
      res.end(JSON.stringify(result.rows));
    } catch (error) {
      console.error('Database error:', error);
      res.writeHead(500, {
        'Content-Type': 'application/json',
        ...corsHeaders
      });
      res.end(JSON.stringify({ error: 'Database error', details: error.message }));
    }
    return;
  }

  // Serve the main HTML file
  const filePath = path.join(__dirname, 'professional-index.html');
  fs.readFile(filePath, (err, data) => {
    if (err) {
      res.writeHead(404, corsHeaders);
      res.end('404 - File not found');
      return;
    }
    res.writeHead(200, { 
      'Content-Type': 'text/html',
      ...corsHeaders
    });
    res.end(data);
  });
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Simple server running on http://0.0.0.0:${PORT}`);
  console.log(`Database connected: ${process.env.DATABASE_URL ? 'Yes' : 'No'}`);
});