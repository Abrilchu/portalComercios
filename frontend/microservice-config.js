// Microservice Configuration for Frontend
const microserviceConfig = {
  // Service identity
  service: {
    name: 'ux-dual-frontend',
    version: '1.0.0',
    type: 'frontend',
    description: 'UX Dual Portal Frontend Microservice'
  },
  
  // Server configuration
  server: {
    host: '0.0.0.0',
    port: 5000,
    cors: {
      enabled: true,
      origins: ['http://localhost:8080', 'http://0.0.0.0:8080'],
      methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
      headers: ['Content-Type', 'Authorization', 'Accept']
    }
  },
  
  // Backend API configuration
  backend: {
    baseUrl: process.env.BACKEND_URL || 'http://localhost:8080',
    timeout: 30000,
    retries: 3,
    endpoints: {
      auth: '/auth',
      dashboard: '/api/dashboard',
      payments: '/api/payments',
      customers: '/api/customers',
      branches: '/api/branches',
      users: '/api/users'
    }
  },
  
  // Health check configuration
  health: {
    endpoint: '/health',
    interval: 30000
  },
  
  // Logging configuration
  logging: {
    level: process.env.LOG_LEVEL || 'info',
    format: 'json'
  }
};

module.exports = microserviceConfig;