# UX Dual Portal - Arquitectura de Microservicios

## Resumen

UX Dual Portal ha sido reestructurado para implementar una arquitectura de microservicios completa, separando el frontend y backend en servicios independientes y escalables.

## Arquitectura de Microservicios

### 🎯 Frontend Microservice (Puerto 5000)
- **Servicio**: `ux-dual-frontend`
- **Tecnología**: Node.js + HTML/CSS/JavaScript
- **Puerto**: 5000
- **Responsabilidades**:
  - Servir la aplicación web (SPA)
  - Proxy de API hacia el backend
  - Manejo de CORS
  - Health checks del frontend
  - Inyección de configuración dinámica

### 🎯 Backend Microservice (Puerto 8080)
- **Servicio**: `ux-dual-backend-api`
- **Tecnología**: Java 17 + Micronaut
- **Puerto**: 8080
- **Responsabilidades**:
  - API REST para datos de negocio
  - Autenticación JWT
  - Gestión de base de datos
  - Lógica de negocio
  - Health checks del backend

## Comunicación Entre Servicios

```
Cliente Web (Browser)
    ↓
Frontend Microservice (5000)
    ↓ Proxy API
Backend Microservice (8080)
    ↓
PostgreSQL Database
```

## Endpoints de Microservicio

### Frontend Microservice (http://localhost:5000)
- `GET /` - Aplicación web principal
- `GET /health` - Health check del frontend
- `ALL /api/*` - Proxy hacia backend
- `ALL /auth/*` - Proxy hacia autenticación

### Backend Microservice (http://localhost:8080)
- `GET /health` - Health check del backend
- `GET /info` - Información del servicio
- `GET /metrics` - Métricas del sistema
- `ALL /api/*` - API de datos de negocio
- `ALL /auth/*` - API de autenticación

## Configuración

### Frontend (frontend/microservice-config.js)
```javascript
{
  service: {
    name: 'ux-dual-frontend',
    version: '1.0.0',
    type: 'frontend'
  },
  server: {
    host: '0.0.0.0',
    port: 5000
  },
  backend: {
    baseUrl: 'http://localhost:8080'
  }
}
```

### Backend (src/main/resources/application.yml)
```yaml
micronaut:
  application:
    name: ux-dual-backend-api
  server:
    port: 8080
    cors:
      enabled: true
```

## Scripts de Ejecución

### Desarrollo
```bash
# Backend Microservice
mvn compile exec:java

# Frontend Microservice
cd frontend && npm run microservice
```

### Producción
Los microservicios pueden desplegarse independientemente:
- Frontend: Como servicio estático + proxy
- Backend: Como aplicación Java independiente

## Ventajas de Esta Arquitectura

### ✅ Escalabilidad
- Cada servicio puede escalar independientemente
- Recursos asignados según necesidad específica

### ✅ Desarrollo Independiente
- Equipos pueden trabajar en paralelo
- Despliegues independientes
- Tecnologías diferentes por servicio

### ✅ Resiliencia
- Fallo de un servicio no afecta al otro
- Health checks independientes
- Circuit breakers implementables

### ✅ Mantenimiento
- Actualizaciones independientes
- Debugging más fácil
- Logs separados por servicio

## Monitoreo y Health Checks

### Health Check Frontend
```bash
curl http://localhost:5000/health
```

### Health Check Backend
```bash
curl http://localhost:8080/health
curl http://localhost:8080/info
curl http://localhost:8080/metrics
```

## Base de Datos

La base de datos PostgreSQL permanece centralizada pero es accedida únicamente por el backend microservice, manteniendo la separación de responsabilidades.

## Seguridad

- CORS configurado entre servicios
- JWT tokens manejados por el backend
- Proxy seguro en el frontend
- Headers de microservicio para trazabilidad

## Próximos Pasos

1. **Service Discovery**: Implementar registro de servicios
2. **Load Balancing**: Balanceadores para múltiples instancias
3. **API Gateway**: Gateway unificado para múltiples microservicios
4. **Container Orchestration**: Docker + Kubernetes
5. **Distributed Tracing**: Trazabilidad entre servicios